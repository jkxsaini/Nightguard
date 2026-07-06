package com.example.nightguard

class LocationUIState {
    data class LocationUIState(
        val latitude: Double? = null,
        val longitude: Double? = null,
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    )
}