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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.go.givngo.R
import com.go.givngo.BuildConfig
import com.google.android.gms.location.*
import com.mapbox.geojson.*
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
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

import android.os.Handler

import com.mapbox.mapboxsdk.style.layers.LineLayer

import android.graphics.Bitmap
import android.graphics.Canvas



class MapViewModel : ViewModel() {

    // Query search for the map
    val querySearch = mutableStateOf("")
    val searchQuery: State<String> = querySearch

    // LiveData to hold the user's location
    private val _userLocation = MutableLiveData<Location?>()
    val userLocation: MutableLiveData<Location?> = _userLocation

// Function to enable location component
    @SuppressLint("MissingPermission")
    fun enableLocationComponent(mapboxMap: MapboxMap, context: Context) {
        val locationComponent = mapboxMap.locationComponent
        val locationComponentOptions = LocationComponentOptions.builder(context).build()
        val locationComponentActivationOptions = LocationComponentActivationOptions.builder(context, mapboxMap.style!!)
            .locationComponentOptions(locationComponentOptions)
            .build()

        locationComponent.activateLocationComponent(locationComponentActivationOptions)

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationComponent.isLocationComponentEnabled = true
            locationComponent.cameraMode = CameraMode.TRACKING
            locationComponent.renderMode = RenderMode.COMPASS

            val lastLocation = locationComponent.lastKnownLocation
            lastLocation?.let {
                _userLocation.value = it
                val position = LatLng(it.latitude, it.longitude)
                mapboxMap.addMarker(
                    com.mapbox.mapboxsdk.annotations.MarkerOptions()
                        .position(position)
                        .title("You are here")
                )
                mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 14.0))
            }
        }
    }


    @SuppressLint("MissingPermission")
    fun getUserLocation(context: Context, onLocationFetched: (Location?) -> Unit) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val locationTask = fusedLocationClient.lastLocation

        locationTask.addOnSuccessListener { location ->
            if (location != null) {
                _userLocation.value = location
                onLocationFetched(location)
            } else {
                requestNewLocationData(context)
            }
        }.addOnFailureListener { e ->
            Log.e("MapDebug", "Error getting last known location: ${e.message}")
            Toast.makeText(context, "Error getting location", Toast.LENGTH_SHORT).show()
        }
    }
    
    

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

    fun searchAddressAndRoute(
    mapboxMap: MapboxMap,
    context: Context,
    address: String,
    onRouteReady: (LatLng) -> Unit
) {
    // Retrieve API key from resources or BuildConfig
    // val apiKey = context.getString(R.string.opencage_api_key) // If using strings.xml
    // or
    
    val geocodingUrl =
        "https://api.opencagedata.com/geocode/v1/json?q=${address}&key=${BuildConfig.OPENCAGE_API_KEY}"

    val client = OkHttpClient()
    val request = Request.Builder().url(geocodingUrl).build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("MapDebug", "Geocoding API call failed: ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                response.body?.string()?.let { responseBody ->
                    val json = JSONObject(responseBody)
                    val results = json.getJSONArray("results")
                    if (results.length() > 0) {
                        val firstResult = results.getJSONObject(0)
                        val geometry = firstResult.getJSONObject("geometry")
                        val lng = geometry.getDouble("lng")
                        val lat = geometry.getDouble("lat")

                        val targetLatLng = LatLng(lat, lng)
                        onRouteReady(targetLatLng)

                        addMarkerAndZoom(mapboxMap, mapboxMap.style!!, context, targetLatLng)
                    } else {
                        Log.e("MapDebug", "No results found for the address.")
                    }
                }
            } else {
                Log.e("MapDebug", "Geocoding API call failed: ${response.message}")
            }
        }
    })
}

fun drawRoute(
    mapboxMap: MapboxMap,
    userLocation: LatLng,
    targetLocation: LatLng,
    style: Style,
    context: Context
) {
    val client = OkHttpClient()

    // Construct the directions URL using LatLng coordinates
    val directions = "https://api.openrouteservice.org/v2/directions/driving-car?api_key=5b3ce3597851110001cf62486164064072d74c0b94ef38d362b6f277&start=${userLocation.longitude},${userLocation.latitude}&end=${targetLocation.longitude},${targetLocation.latitude}"
    val request = Request.Builder()
        .url(directions)
        .header("Accept", "application/json")
        .build()

    client.newCall(request).enqueue(object : okhttp3.Callback {
        override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
            Log.e("OpenRouteService", "Failed to fetch route: ${e.message}")
        }

        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            if (response.isSuccessful) {
                response.body?.string()?.let { responseBody ->
                    try {
                        val json = JSONObject(responseBody)
                        val routes = json.getJSONArray("features")
                        if (routes.length() > 0) {
                            val geometry = routes.getJSONObject(0)
                                .getJSONObject("geometry")
                            val routeCoordinates = LineString.fromJson(geometry.toString())
                            updateRouteLayer(mapboxMap, routeCoordinates, style)
                        } else {
                            Log.e("OpenRouteService", "No routes found in response")
                        }
                    } catch (e: Exception) {
                        Log.e("OpenRouteService", "Error parsing route response: ${e.message}")
                    }
                }
            } else {
                Log.e("OpenRouteService", "Route fetch failed: ${response.message}")
            }
        }
    })
}


private fun updateRouteLayer(mapboxMap: MapboxMap, route: LineString, style: Style) {
    Handler(Looper.getMainLooper()).post {
        val routeSourceId = "route-source"
        val routeLayerId = "route-layer"

        // Add route source
        if (style.getSource(routeSourceId) == null) {
            style.addSource(
                GeoJsonSource(routeSourceId, FeatureCollection.fromFeature(Feature.fromGeometry(route)))
            )
        } else {
            val source = style.getSourceAs<GeoJsonSource>(routeSourceId)
            source?.setGeoJson(FeatureCollection.fromFeature(Feature.fromGeometry(route)))
        }

        // Add route layer
        if (style.getLayer(routeLayerId) == null) {
            style.addLayer(
                LineLayer(routeLayerId, routeSourceId).withProperties(
                    PropertyFactory.lineColor("#3bb2d0"),
                    PropertyFactory.lineWidth(5f),
                    PropertyFactory.lineOpacity(0.8f)
                )
            )
        } else {
            Log.d("MapDebug", "Route layer already exists")
        }
    }
}



    fun addMarkerAndZoom(mapboxMap: MapboxMap, style: Style, context: Context, userLatLng: LatLng) {
    Handler(Looper.getMainLooper()).post {
        // Check if the source for the marker exists
        val sourceId = "marker-source"
        val existingSource = style.getSourceAs<GeoJsonSource>(sourceId)

        if (existingSource == null) {
            // If the source does not exist, create a new source and add it to the style
            val source = GeoJsonSource(
                sourceId,
                FeatureCollection.fromFeatures(
                    arrayOf(Feature.fromGeometry(Point.fromLngLat(userLatLng.longitude, userLatLng.latitude)))
                )
            )
            style.addSource(source)

            // Add the marker layer if it doesn't already exist
            if (style.getLayer("marker-layer") == null) {
                val markerImageId = "custom-marker"
                val vectorDrawable = ContextCompat.getDrawable(context, R.drawable.ic_browsedonations)

                if (vectorDrawable != null) {
                    // Convert vector drawable to bitmap
                    val bitmap = Bitmap.createBitmap(
                        vectorDrawable.intrinsicWidth,
                        vectorDrawable.intrinsicHeight,
                        Bitmap.Config.ARGB_8888
                    )
                    val canvas = Canvas(bitmap)
                    vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
                    vectorDrawable.draw(canvas)

                    // Add the bitmap to the style
                    style.addImage(markerImageId, bitmap)

                    val layerId = "marker-layer"
                    val symbolLayer = SymbolLayer(layerId, "marker-source").withProperties(
                        PropertyFactory.iconImage(markerImageId),
                        PropertyFactory.iconSize(1.0f)
                    )
                    style.addLayer(symbolLayer)
                } else {
                    Log.e("Mapbox", "Failed to load vector drawable R.drawable.ic_browsedonations")
                }
            }
        } else {
            // If the source exists, just update the location of the marker
            val feature = Feature.fromGeometry(Point.fromLngLat(userLatLng.longitude, userLatLng.latitude))
            existingSource.setGeoJson(FeatureCollection.fromFeature(feature))
        }

        // Animate the camera to the new location
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 14.0))
    }
}



}



