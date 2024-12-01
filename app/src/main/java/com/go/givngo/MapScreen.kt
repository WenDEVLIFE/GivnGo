package com.go.givngo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.go.givngo.Model.MapViewModel
import com.go.givngo.TrackingMap
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.Point

@Composable
fun Map(
    modifier: Modifier = Modifier,
    onMapReady: (MapboxMap) -> Unit
) {
    val context = LocalContext.current
    var mapReady by remember { mutableStateOf(false) }
    var permissionGranted by remember { mutableStateOf(false) }
    val defaultAddress = "2089 Santiago St. Fortune 1 Hen T. De Leon Valenzuela City"

    // ViewModel
    val mapModel: MapViewModel = viewModel()

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        permissionGranted = isGranted
        if (isGranted) {
            mapModel.getUserLocation(context) { location ->
                mapModel.userLocation.value = location
                mapReady = true
            }
        } else {
            Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    // Check for location permission
    LaunchedEffect(Unit) {
        when {
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                permissionGranted = true
                mapModel.getUserLocation(context) { location ->
                    mapModel.userLocation.value = location
                    mapReady = true
                }
            }
            else -> {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    // MapView component
    val mapView = remember { MapView(context) }

    // State for address search
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var mapboxMap by remember { mutableStateOf<MapboxMap?>(null) }
    var isMapReady by remember { mutableStateOf(false) }

    // AndroidView to display the MapView
    AndroidView(
    factory = {
        try {
            mapView.apply { onCreate(null) }
        } catch (e: Exception) {
            Log.e("MapDebug", "Error initializing MapView: ${e.message}", e)
            throw e
        }
    },
    modifier = modifier.fillMaxSize(),
    update = { mapView ->
        try {
            mapView.getMapAsync { mapbox ->
                try {
                    Log.d("MapDebug", "MapboxMap is initialized")
                    isMapReady = true

                    mapbox.setStyle(
                        "https://api.maptiler.com/maps/streets-v2/style.json?key=${BuildConfig.MAPTILER_API_KEY}"
                    ) { style ->
                        try {
                            if (permissionGranted) {
                                mapModel.enableLocationComponent(mapbox, context)
                            }
                            
                            mapModel.getUserLocation(context) { location ->
                mapModel.userLocation.value = location
                mapReady = true
            }

                            // Automatically search for the default address
                            mapModel.searchAddressAndRoute(mapbox, context, defaultAddress) { targetLatLng ->
                                try {
                                    mapModel.userLocation.value?.let { location ->
                                        val userLatLng = LatLng(location.latitude, location.longitude)

                                        // Convert userLatLng to Point (Mapbox expects Point, not LatLng)
                                        val userLocation = Point.fromLngLat(userLatLng.longitude, userLatLng.latitude)

                                        // Convert targetLatLng to Point (same as above)
                                        val targetLocation = Point.fromLngLat(targetLatLng.longitude, targetLatLng.latitude)

                                        // Now, call drawRoute with userLocation and targetLocation as Points
                                       mapModel.drawRoute(mapbox, userLatLng, targetLatLng, style, context)

                                        // Toast message for loaded address
                                        (context as? TrackingMap)?.runOnUiThread {
                                            Toast.makeText(context, "Current address: $userLatLng", Toast.LENGTH_SHORT).show()
                                            Toast.makeText(context, "Target Location: $targetLatLng", Toast.LENGTH_SHORT).show()
                                        }
                                    } ?: run {
                                        (context as? TrackingMap)?.runOnUiThread {
                                            Toast.makeText(context, "User location not available.", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } catch (e: Exception) {
                                    Log.e("MapDebug", "Error searching address or drawing route: ${e.message}", e)
                                    (context as? TrackingMap)?.runOnUiThread {
                                        Toast.makeText(context, "Error fetching route.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("MapDebug", "Error setting style or enabling location: ${e.message}", e)
                        }
                    }

                    mapboxMap = mapbox
                    onMapReady(mapbox)
                } catch (e: Exception) {
                    Log.e("MapDebug", "Error initializing MapboxMap: ${e.message}", e)
                }
            }
        } catch (e: Exception) {
            Log.e("MapDebug", "Error in MapView update block: ${e.message}", e)
        }
    }
)


    // Lifecycle management
    DisposableEffect(Unit) {
        onDispose {
            mapView.onStop()
            mapView.onPause()
            mapView.onLowMemory()
            mapView.onDestroy()
        }
    }

    // Save instance state
    LaunchedEffect(Unit) {
        mapView.onSaveInstanceState(Bundle())
    }
}

@Composable
fun MainMap(modifier: Modifier = Modifier) {
    val mapModel: MapViewModel = viewModel()
    val context = LocalContext.current
    var mapboxMap by remember { mutableStateOf<MapboxMap?>(null) }
    Box(modifier = modifier) {
        // Map component should be below everything, including the FAB and SearchBar
        Map(
            modifier = Modifier.fillMaxSize(),
            onMapReady = { map ->
                mapboxMap = map
            }
        )
    }
}

@Composable
fun MapScreen(navController: NavHostController) {
    Scaffold(
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            // Default or initial content
            MainMap()
        }
    }
}

@Preview
@Composable
fun MapPreview() {
    MainMap()
}
