package com.example.nightguard.data

data class UnsafeArea(
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val radiusMeters: Double = 120.0,
    val label: String = "Unsicherer Bereich",
    val message: String = ""
)
