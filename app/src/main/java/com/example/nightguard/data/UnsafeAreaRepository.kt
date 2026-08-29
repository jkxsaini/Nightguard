package com.example.nightguard.data

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class UnsafeAreaRepository(
    private val context: Context
) {
    companion object {
        private const val COLLECTION_UNSAFE_AREAS = "unsafeAreas"
        private const val DEFAULT_LABEL = "Unsicherer Bereich"
        private const val MAX_MESSAGE_LENGTH = 300
    }

    private fun firestoreOrNull(): FirebaseFirestore? {
        return runCatching {
            val app = FirebaseApp.getApps(context).firstOrNull()
                ?: FirebaseApp.initializeApp(context)
                ?: return null
            FirebaseFirestore.getInstance(app)
        }.getOrNull()
    }

    fun listenToUnsafeAreas(
        onAreasChanged: (List<UnsafeArea>) -> Unit,
        onError: (String) -> Unit
    ): ListenerRegistration? {
        val firestore = firestoreOrNull()
        if (firestore == null) {
            onError(
                "Firebase ist noch nicht konfiguriert. " +
                    "Lege google-services.json im app-Ordner ab und starte die App neu."
            )
            return null
        }

        return firestore.collection(COLLECTION_UNSAFE_AREAS)
            .whereEqualTo("active", true)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    onError(exception.localizedMessage ?: "Unsichere Bereiche konnten nicht geladen werden.")
                    return@addSnapshotListener
                }

                val areas = snapshot?.documents.orEmpty().mapNotNull { document ->
                    val latitude = document.getDouble("latitude") ?: return@mapNotNull null
                    val longitude = document.getDouble("longitude") ?: return@mapNotNull null
                    val radius = document.getDouble("radiusMeters") ?: 120.0
                    val label = document.getString("label").orEmpty().ifBlank { DEFAULT_LABEL }
                    val message = document.getString("message").orEmpty().take(MAX_MESSAGE_LENGTH)

                    UnsafeArea(
                        id = document.id,
                        latitude = latitude,
                        longitude = longitude,
                        radiusMeters = radius.coerceIn(20.0, 2_000.0),
                        label = label,
                        message = message
                    )
                }

                onAreasChanged(areas)
            }
    }

    fun addUnsafeArea(
        latitude: Double,
        longitude: Double,
        radiusMeters: Double = 120.0,
        label: String = DEFAULT_LABEL,
        message: String = "",
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        val firestore = firestoreOrNull()
        if (firestore == null) {
            onError(
                "Firebase ist noch nicht konfiguriert. " +
                    "Lege google-services.json im app-Ordner ab."
            )
            return
        }

        val area = hashMapOf(
            "latitude" to latitude.coerceIn(-90.0, 90.0),
            "longitude" to longitude.coerceIn(-180.0, 180.0),
            "radiusMeters" to radiusMeters.coerceIn(20.0, 2_000.0),
            "label" to label.ifBlank { DEFAULT_LABEL }.take(80),
            "message" to message.trim().take(MAX_MESSAGE_LENGTH),
            "active" to true,
            "createdAt" to FieldValue.serverTimestamp()
        )

        firestore.collection(COLLECTION_UNSAFE_AREAS)
            .add(area)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception ->
                onError(exception.localizedMessage ?: "Bereich konnte nicht gespeichert werden.")
            }
    }
}
