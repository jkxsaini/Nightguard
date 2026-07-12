package com.example.nightguard.location

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapHandler(
    modifier: Modifier = Modifier,
    locationUiState: Any // Wir behalten das Any, damit es stabil bleibt
) {
    // Beispiel-Koordinate für Köln (dein Wohnort)
    val cologne = LatLng(50.9375, 6.9603)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(cologne, 15f)
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        // Ein Marker an deinem Standort (Beispiel)
        Marker(
            state = MarkerState(position = cologne),
            title = "Dein Standort"
        )
    }
}