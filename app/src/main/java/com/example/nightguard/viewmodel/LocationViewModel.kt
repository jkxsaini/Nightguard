package com.example.nightguard.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.example.nightguard.location.LocationProvider
import com.example.nightguard.location.LocationUIState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class LocationViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val locationProvider = LocationProvider(
        context = application.applicationContext
    )

    var locationUiState by mutableStateOf(LocationUIState.LocationUIState())
        private set

    fun loadCurrentLocation() {
        locationUiState = LocationUIState.LocationUIState(
            isLoading = true
        )

        locationProvider.requestCurrentLocation(
            onLocationReceived = { location ->
                locationUiState = LocationUIState.LocationUIState(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    isLoading = false,
                    errorMessage = null
                )
            },
            onError = { message ->
                locationUiState = LocationUIState.LocationUIState(
                    isLoading = false,
                    errorMessage = message
                )
            }
        )
    }

    fun onLocationPermissionDenied() {
        locationUiState = LocationUIState.LocationUIState(
            isLoading = false,
            errorMessage = "Standortberechtigung wurde abgelehnt."
        )
    }
}