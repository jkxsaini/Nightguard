package com.example.nightguard.screens

import android.Manifest
import android.content.pm.PackageManager
import android.telephony.SmsManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.nightguard.data.SecureStorage
import com.example.nightguard.location.LocationProvider

@Composable
fun TrackingScreen(
    contactName: String,
    onStopTracking: () -> Unit,
    onFakeCallClick: () -> Unit,
    onPoliceCallClick: () -> Unit,
    onSosClick: () -> Unit,
    onShakeTriggered: () -> Unit = {}
) {
    val context = LocalContext.current
    val secureStorage = remember { SecureStorage(context) }
    val correctPin = remember { secureStorage.getPin() }

    var showPinDialog by remember { mutableStateOf(false) }
    var pinInput by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }

    // --- NEU: Eigener GPS-Abfrager für den Tracking Screen ---
    val locationProvider = remember { LocationProvider(context) }
    var currentLatitude by remember { mutableStateOf<Double?>(null) }
    var currentLongitude by remember { mutableStateOf<Double?>(null) }

    val deepWine = Color(0xFF1B040D)
    val buttonGrey = Color(0xFF333333)
    val policeRed = Color(0xFFD32F2F)
    val sosRed = Color(0xFFEF5350)

    // Trennt den String in Name für die UI und Nummer für die SMS
    val contactParts = contactName.split(" - ")
    val displayContactName = contactParts.getOrNull(0) ?: contactName
    val contactNumber = contactParts.getOrNull(1) ?: ""

    // SMS Hilfsfunktion
    fun sendSms(number: String, message: String) {
        if (number.isNotEmpty()) {
            try {
                val smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(number, null, message, null, null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Erstellt die SOS SMS mit Live-Koordinaten
    val triggerSosSms = {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            val locationString = if (currentLatitude != null && currentLongitude != null) {
                "Aktueller Standort: https://maps.google.com/?q=$currentLatitude,$currentLongitude"
            } else {
                "Standort wird gerade noch ermittelt..."
            }
            // TIPP: Ersetze "Jasmin" hier durch den Namen, der gesendet werden soll
            sendSms(contactNumber, "🚨 SOS! Jasmin befindet sich in Gefahr! $locationString")
        }
    }

    // Fragt beim Öffnen nach SMS Rechten und sendet die Start-SMS
    val smsPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            sendSms(contactNumber, "Hey, ich bin gerade unterwegs und teile meinen Standort mit dir über die Nightguard App.")
        }
    }

    // Wird automatisch ausgeführt, sobald der Screen öffnet
    LaunchedEffect(Unit) {
        // 1. GPS Position abrufen (sofern erlaubt)
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationProvider.requestCurrentLocation(
                onLocationReceived = { location ->
                    currentLatitude = location.latitude
                    currentLongitude = location.longitude
                },
                onError = {}
            )
        }

        // 2. SMS Rechte prüfen und Info-SMS senden
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            smsPermissionLauncher.launch(Manifest.permission.SEND_SMS)
        } else {
            sendSms(contactNumber, "Hey, ich bin gerade unterwegs und teile meinen Standort mit dir über die Nightguard App.")
        }
    }

    // Hardware-Schüttelsensor
    DisposableEffect(Unit) {
        val shakeDetector = com.example.nightguard.location.ShakeDetector(context)
        shakeDetector.startListening {
            triggerSosSms() // Sendet die SMS beim Schütteln
            onShakeTriggered()
        }
        onDispose { shakeDetector.stopListening() }
    }

    // UI-Dialog für PIN
    if (showPinDialog) {
        AlertDialog(
            onDismissRequest = { showPinDialog = false; pinInput = ""; pinError = false },
            title = { Text("Tracking beenden") },
            text = {
                Column {
                    Text("Bitte PIN eingeben, um das Tracking sicher zu beenden:")
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = pinInput,
                        onValueChange = { pinInput = it; pinError = false },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        visualTransformation = PasswordVisualTransformation(),
                        isError = pinError,
                        singleLine = true,
                        label = { Text("PIN") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (pinInput == correctPin) {
                        showPinDialog = false
                        pinInput = ""
                        onStopTracking()
                    } else {
                        pinError = true
                    }
                }) { Text("Bestätigen") }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(deepWine)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Icon(
            imageVector = Icons.Filled.LocationOn,
            contentDescription = "Active Tracking",
            tint = Color(0xFF4CAF50),
            modifier = Modifier
                .size(84.dp)
                .clickable {
                    triggerSosSms() // SMS auch bei Klick auf Icon senden
                    onShakeTriggered()
                }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // UI nutzt wieder nur den Namen ohne Nummer
        Text(text = "Standort wird geteilt", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
        Text(text = "mit $displayContactName", color = Color.LightGray, fontSize = 18.sp, fontWeight = FontWeight.Medium)

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { showPinDialog = true },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = buttonGrey, contentColor = Color.White)
        ) {
            Text("Sicher angekommen (Beenden)", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
            OutlinedButton(
                onClick = onFakeCallClick,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, Color.Gray),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
            ) {
                Icon(Icons.Filled.Call, null); Spacer(Modifier.width(12.dp)); Text("Fake Call auslösen")
            }

            Button(
                onClick = onPoliceCallClick,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = policeRed, contentColor = Color.White)
            ) {
                Icon(Icons.Filled.LocalPolice, null); Spacer(Modifier.width(12.dp)); Text("Polizei Notruf (110)")
            }

            Button(
                onClick = {
                    triggerSosSms() // Sendet die SMS
                    onSosClick() // Navigiert normal zum AlarmScreen
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = sosRed, contentColor = Color.White)
            ) {
                Icon(Icons.Filled.Warning, null); Spacer(Modifier.width(12.dp)); Text("SOS an $displayContactName")
            }
        }
    }
}