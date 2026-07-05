package com.example.nightguard

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import androidx.core.content.ContextCompat

class LocationProvider(
    private val context: Context
) {
    fun hasLocationPermission(): Boolean {
        val fineLocationGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return fineLocationGranted || coarseLocationGranted
    }

    @SuppressLint("MissingPermission")
    fun requestCurrentLocation(
        onLocationReceived: (Location) -> Unit,
        onError: (String) -> Unit
    ) {
        if (!hasLocationPermission()) {
            onError("Standortberechtigung fehlt.")
            return
        }

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val provider = when {
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) -> {
                LocationManager.GPS_PROVIDER
            }

            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) -> {
                LocationManager.NETWORK_PROVIDER
            }

            else -> {
                onError("Kein Standortanbieter aktiv.")
                return
            }
        }

        val lastKnownLocation = locationManager.getLastKnownLocation(provider)

        if (lastKnownLocation != null) {
            onLocationReceived(lastKnownLocation)
            return
        }

        val listener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                onLocationReceived(location)
                locationManager.removeUpdates(this)
            }

            override fun onProviderEnabled(provider: String) = Unit

            override fun onProviderDisabled(provider: String) = Unit

            @Deprecated("Deprecated in Java")
            override fun onStatusChanged(
                provider: String?,
                status: Int,
                extras: Bundle?
            ) = Unit
        }

        locationManager.requestLocationUpdates(
            provider,
            0L,
            0f,
            listener,
            Looper.getMainLooper()
        )
    }
}