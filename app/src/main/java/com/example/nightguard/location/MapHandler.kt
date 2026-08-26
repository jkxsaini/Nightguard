package com.example.nightguard.location

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.nightguard.data.UnsafeArea
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapHandler(
    modifier: Modifier = Modifier,
    latitude: Double,
    longitude: Double,
    zoom: Float = 15f,
    markerTitle: String,
    // --- NEU: Eure Parameter für die unsicheren Bereiche sind wieder da ---
    unsafeAreas: List<UnsafeArea> = emptyList(),
    onUnsafeAreaLongPress: (Double, Double) -> Unit = { _, _ -> }
) {
    val location = LatLng(latitude, longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, zoom)
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        // Erkennt langes Drücken auf die Karte und gibt die Koordinaten an deinen MainScreen weiter
        onMapLongClick = { latLng ->
            onUnsafeAreaLongPress(latLng.latitude, latLng.longitude)
        }
    ) {
        // Dein eigener Standort (Pin)
        Marker(
            state = MarkerState(position = location),
            title = markerTitle
        )

        // Zeichnet alle unsicheren Bereiche als rote, leicht transparente Kreise auf die Karte
        unsafeAreas.forEach { area ->
            Circle(
                center = LatLng(area.latitude, area.longitude),
                radius = 100.0,  // <-- HIER: Einfach eine feste Zahl wie 100.0 eintragen (100 Meter)
                fillColor = Color(0x44FF0000),   // 44 ist der Transparenz-Wert, FF0000 ist Rot
                strokeColor = Color.Red,
                strokeWidth = 3f
            )
        }
    }
}