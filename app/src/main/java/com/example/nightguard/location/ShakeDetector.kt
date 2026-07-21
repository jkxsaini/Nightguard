package com.example.nightguard.location

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

class ShakeDetector(context: Context) : SensorEventListener {

    // Holt den System-Dienst für die Hardware-Sensoren des Handys
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    // Wählt spezifisch den Beschleunigungssensor (Accelerometer) aus
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var onShake: (() -> Unit)? = null
    private var lastShakeTime: Long = 0

    // Wird vom TrackingScreen aufgerufen, um den Sensor scharf zu schalten
    fun startListening(onShakeTriggered: () -> Unit) {
        onShake = onShakeTriggered
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    // Wird vom TrackingScreen aufgerufen, um Akku zu sparen, wenn die App im Hintergrund ist
    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    // Diese Funktion wird vom Handy hunderte Male pro Sekunde aufgerufen, wenn es sich bewegt
    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            // Berechnet die Fliehkräfte (G-Force) in alle 3 Richtungen (X, Y, Z)
            val gX = x / SensorManager.GRAVITY_EARTH
            val gY = y / SensorManager.GRAVITY_EARTH
            val gZ = z / SensorManager.GRAVITY_EARTH

            // Mathematische Formel für die absolute Gesamtkraft
            val gForce = sqrt((gX * gX + gY * gY + gZ * gZ).toDouble()).toFloat()

            // 2.7 ist ein bewährter Schwellenwert.
            // Er ist stark genug, damit normales Laufen ihn nicht auslöst,
            // aber leicht genug, um in Panik schnell auszulösen.
            if (gForce > 2.7f) {
                val now = System.currentTimeMillis()
                // Verhindert, dass ein einziges Schütteln den Alarm 5x hintereinander auslöst (500ms Cooldown)
                if (now - lastShakeTime > 500) {
                    lastShakeTime = now
                    onShake?.invoke() // Teilt dem TrackingScreen mit: "Es wurde geschüttelt!"
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Wird für diesen simplen Schüttel-Alarm nicht benötigt
    }
}