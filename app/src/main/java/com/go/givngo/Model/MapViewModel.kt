package com.go.givngo.Model

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.go.givngo.R
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource

class MapViewModel: ViewModel() {

    // This a query search for the map
    val querySearch   = mutableStateOf("")
    val searchQuery:State<String> = querySearch

    // LiveData to hold the user's location
    private val _userLocation = MutableLiveData<Location?>()
    val userLocation: MutableLiveData<Location?> = _userLocation


    // Function to enable location component
    @SuppressLint("MissingPermission")
    fun enableLocationComponent(mapboxMap: MapboxMap, context: Context) {

        // Get the location component
        val locationComponent = mapboxMap.locationComponent

        // Set the location component options
        val locationComponentOptions = LocationComponentOptions.builder(context).build()
        val locationComponentActivationOptions = LocationComponentActivationOptions.builder(context, mapboxMap.style!!).apply {
            locationComponentOptions(locationComponentOptions)
        }.build()

        // Activate the location component
        locationComponent.activateLocationComponent(locationComponentActivationOptions)

        // Enable the location component
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationComponent.isLocationComponentEnabled = true
            locationComponent.cameraMode = CameraMode.TRACKING
            locationComponent.renderMode = RenderMode.COMPASS

            // Get the last known location
            val lastLocation = locationComponent.lastKnownLocation
            lastLocation?.let {
                _userLocation.value = it
                val position = LatLng(it.latitude, it.longitude)
                mapboxMap.addMarker(
                    com.mapbox.mapboxsdk.annotations.MarkerOptions()
                        .position(position)
                        .title("You are here")
                )

                // Zoom to the user's location
                mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 14.0))


            }
        }
    }

    // Inside MapViewModel
    @SuppressLint("MissingPermission")
    fun getUserLocation(context: Context, onLocationFetched: (Location?) -> Unit) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val locationTask: Task<Location> = fusedLocationClient.lastLocation

        locationTask.addOnSuccessListener { location: Location? ->
            if (location != null) {
                _userLocation.value = location
                onLocationFetched(location)  // Call the callback with the fetched location
            } else {
                requestNewLocationData(context)
            }
        }.addOnFailureListener { e ->
            Log.e("MapDebug", "Error getting last known location: ${e.message}")
            Toast.makeText(context, "Error getting location", Toast.LENGTH_SHORT).show()
        }
    }


    // Function to request new location data
    @SuppressLint("MissingPermission")
    private fun requestNewLocationData(context: Context) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10000
            fastestInterval = 5000
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.let { locations ->
                    for (location in locations) {
                        _userLocation.value = location
                        fusedLocationClient.removeLocationUpdates(this)
                        break
                    }
                }
            }
        }


        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    // Function to add marker and zoom to user location
    fun addMarkerAndZoom(mapboxMap: MapboxMap, style: Style, context: Context, userLatLng: LatLng) {
        if (style.getSource("marker-source") == null) {
            val sourceId = "marker-source"
            val source = GeoJsonSource(
                sourceId,
                FeatureCollection.fromFeatures(
                    arrayOf(Feature.fromGeometry(Point.fromLngLat(userLatLng.longitude, userLatLng.latitude)))
                )
            )
            style.addSource(source)
        }

        if (style.getLayer("marker-layer") == null) {
            val markerImageId = "custom-marker"
            val drawableResource = BitmapFactory.decodeResource(context.resources, R.drawable.ic_browsedonations)
            style.addImage(markerImageId, drawableResource)

            val layerId = "marker-layer"
            val symbolLayer = SymbolLayer(layerId, "marker-source").withProperties(
                PropertyFactory.iconImage(markerImageId),
                PropertyFactory.iconSize(1.0f)
            )
            style.addLayer(symbolLayer)
        }

        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 14.0))
    }

}