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
        onMapLongClick = { latLng ->
            onUnsafeAreaLongPress(latLng.latitude, latLng.longitude)
        }
    ) {
        // Eigener Standort
        Marker(
            state = MarkerState(position = location),
            title = markerTitle
        )

        unsafeAreas.forEach { area ->
            val areaPosition = LatLng(area.latitude, area.longitude)

            // Der gespeicherte Radius wird als halbtransparenter roter Bereich dargestellt.
            Circle(
                center = areaPosition,
                radius = area.radiusMeters,
                fillColor = Color(0x44FF0000),
                strokeColor = Color.Red,
                strokeWidth = 3f
            )

            // Der Marker in der Mitte zeigt Radius und optionale Nachricht beim Antippen.
            Marker(
                state = MarkerState(position = areaPosition),
                title = area.label,
                snippet = buildString {
                    append("Radius: ${area.radiusMeters.toInt()} m")
                    if (area.message.isNotBlank()) {
                        append(" · ")
                        append(area.message)
                    }
                }
            )
        }
    }
}
