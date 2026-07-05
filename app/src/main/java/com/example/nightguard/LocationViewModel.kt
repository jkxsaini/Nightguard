package com.example.nightguard

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.nightguard.LocationUIState.LocationUIState

class LocationViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val locationProvider = LocationProvider(
        context = application.applicationContext
    )

    var locationUiState by mutableStateOf(LocationUIState())
        private set

    fun loadCurrentLocation() {
        locationUiState = LocationUIState(
            isLoading = true
        )

        locationProvider.requestCurrentLocation(
            onLocationReceived = { location ->
                locationUiState = LocationUIState(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    isLoading = false,
                    errorMessage = null
                )
            },
            onError = { message ->
                locationUiState = LocationUIState(
                    isLoading = false,
                    errorMessage = message
                )
            }
        )
    }

    fun onLocationPermissionDenied() {
        locationUiState = LocationUIState(
            isLoading = false,
            errorMessage = "Standortberechtigung wurde abgelehnt."
        )
    }
}