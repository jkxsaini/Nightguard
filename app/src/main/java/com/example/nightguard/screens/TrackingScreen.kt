package com.example.nightguard.screens

import android.Manifest
import android.content.pm.PackageManager
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
import com.example.nightguard.location.ShakeDetector

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

    val locationProvider = remember { LocationProvider(context) }
    var currentLatitude by remember { mutableStateOf<Double?>(null) }
    var currentLongitude by remember { mutableStateOf<Double?>(null) }

    val deepWine = Color(0xFF1B040D)
    val buttonGrey = Color(0xFF333333)
    val policeRed = Color(0xFFD32F2F)
    val sosRed = Color(0xFFEF5350)

    val contactParts = contactName.split(" - ")
    val displayContactName = contactParts.getOrNull(0) ?: contactName

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationProvider.requestCurrentLocation(
                onLocationReceived = { location ->
                    currentLatitude = location.latitude
                    currentLongitude = location.longitude
                },
                onError = {}
            )
        }
    }

    DisposableEffect(Unit) {
        val shakeDetector = ShakeDetector(context)
        shakeDetector.startListening {
            onShakeTriggered()
        }
        onDispose { shakeDetector.stopListening() }
    }

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
                    onShakeTriggered()
                }
        )

        Spacer(modifier = Modifier.height(16.dp))

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
                    onSosClick()
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