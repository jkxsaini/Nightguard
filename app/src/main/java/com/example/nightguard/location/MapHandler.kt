package com.example.nightguard.location

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.nightguard.data.UnsafeArea
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon

@Composable
fun MapHandler(
    modifier: Modifier = Modifier,
    latitude: Double = 50.9375,
    longitude: Double = 6.9603,
    zoom: Double = 15.0,
    markerTitle: String = "Dein Standort",
    unsafeAreas: List<UnsafeArea> = emptyList(),
    onUnsafeAreaLongPress: ((latitude: Double, longitude: Double) -> Unit)? = null
) {
    val context = LocalContext.current
    val latestLongPressHandler = rememberUpdatedState(onUnsafeAreaLongPress)
    val geoPoint = remember(latitude, longitude) {
        GeoPoint(latitude, longitude)
    }

    val mapView = remember {
        val config = Configuration.getInstance()
        config.load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
        config.userAgentValue = "NightguardApp/1.0 (com.example.nightguard; Android)"

        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            minZoomLevel = 3.0
            maxZoomLevel = 20.0
            controller.setZoom(zoom)
            controller.setCenter(geoPoint)

            overlays.add(
                MapEventsOverlay(
                    object : MapEventsReceiver {
                        override fun singleTapConfirmedHelper(p: GeoPoint): Boolean = false

                        override fun longPressHelper(p: GeoPoint): Boolean {
                            val handler = latestLongPressHandler.value ?: return false
                            handler(p.latitude, p.longitude)
                            return true
                        }
                    }
                )
            )
        }
    }

    DisposableEffect(mapView) {
        mapView.onResume()
        onDispose {
            mapView.onPause()
            mapView.onDetach()
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { mapView },
        update = { map ->
            map.controller.setZoom(zoom)
            map.controller.animateTo(geoPoint)

            // Nur dynamische Standort-/Gefahrenoverlays neu aufbauen.
            // Das MapEventsOverlay für lange Klicks bleibt bestehen.
            map.overlays.removeAll { overlay ->
                overlay is Marker || overlay is Polygon
            }

            unsafeAreas.forEach { area ->
                val center = GeoPoint(area.latitude, area.longitude)
                val dangerZone = Polygon(map).apply {
                    points = Polygon.pointsAsCircle(center, area.radiusMeters)
                    title = area.label
                    snippet = "Radius: ${area.radiusMeters.toInt()} m"
                    fillColor = android.graphics.Color.argb(70, 220, 35, 35)
                    strokeColor = android.graphics.Color.rgb(220, 35, 35)
                    strokeWidth = 4f
                }
                map.overlays.add(dangerZone)
            }

            val marker = Marker(map).apply {
                position = geoPoint
                title = markerTitle
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            }

            map.overlays.add(marker)
            map.invalidate()
        }
    )
}
