package com.example.nightguard.location

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState

@Composable
fun MapHandler(
    modifier: Modifier = Modifier,
    latitude: Double = 50.9375,
    longitude: Double = 6.9603,
    zoom: Double = 15.0,
    markerTitle: String = "Dein Standort"
) {
    val currentPosition = LatLng(
        latitude,
        longitude
    )

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            currentPosition,
            zoom.toFloat()
        )
    }

    val markerState = rememberUpdatedMarkerState(
        position = currentPosition
    )

    LaunchedEffect(latitude, longitude, zoom) {
        cameraPositionState.animate(
            CameraUpdateFactory.newLatLngZoom(
                currentPosition,
                zoom.toFloat()
            )
        )
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = markerState,
            title = markerTitle
        )
    }
}