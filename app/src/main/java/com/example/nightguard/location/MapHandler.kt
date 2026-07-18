package com.example.nightguard.location

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapHandler(
    modifier: Modifier = Modifier,
    latitude: Double = 50.9375,
    longitude: Double = 6.9603,
    zoom: Double = 15.0,
    markerTitle: String = "Dein Standort"
) {
    val context = LocalContext.current
    val geoPoint = remember(latitude, longitude) {
        GeoPoint(latitude, longitude)
    }

    val mapView = remember {
        Configuration.getInstance().load(
            context,
            context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
        )
        Configuration.getInstance().userAgentValue = context.packageName

        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            minZoomLevel = 3.0
            maxZoomLevel = 20.0
            controller.setZoom(zoom)
            controller.setCenter(geoPoint)
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

            map.overlays.removeAll { overlay -> overlay is Marker }

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
