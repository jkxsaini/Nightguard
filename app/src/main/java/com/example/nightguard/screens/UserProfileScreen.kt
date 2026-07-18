package com.example.nightguard.screens

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nightguard.data.SecureStorage

// Hilfsfunktion: Liest den Namen des ausgewählten Kontakts aus der Android-Datenbank
fun getContactNameFromUri(context: Context, uri: Uri): String? {
    var name: String? = null
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val nameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            if (nameIndex >= 0) {
                name = it.getString(nameIndex)
            }
        }
    }
    return name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(onBackToHome: () -> Unit) {
    val context = LocalContext.current
    val secureStorage = remember { SecureStorage(context) }

    // States für die UI
    var currentPin by remember { mutableStateOf(secureStorage.getPin()) }
    var newPinInput by remember { mutableStateOf("") }

    var contacts by remember { mutableStateOf(secureStorage.getContacts()) }

    // Farben
    val deepWine = Color(0xFF1B040D)
    val cardColor = Color(0xFF2C2C2C)
    val buttonGrey = Color(0xFF333333)

    // DER NATIVE CONTACT PICKER (Öffnet das echte Android-Telefonbuch)
    val contactPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickContact()
    ) { uri: Uri? ->
        uri?.let {
            // Holt den Namen aus dem Adressbuch
            val name = getContactNameFromUri(context, it)
            if (name != null) {
                // Speichert den Namen dauerhaft in eurer App
                val updatedList = contacts.toMutableList()
                if (!updatedList.contains(name)) { // Verhindert Duplikate
                    updatedList.add(name)
                    secureStorage.saveContacts(updatedList)
                    contacts = updatedList
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mein Profil", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackToHome) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Zurück", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = deepWine)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(deepWine)
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            // --- PIN BEREICH (Bleibt erhalten) ---
            Text("Sicherheits-PIN", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Aktuelle PIN: $currentPin", color = Color.LightGray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = newPinInput,
                    onValueChange = { if (it.length <= 4) newPinInput = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = { Text("Neue 4-stellige PIN") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                    )
                )
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = {
                        if (newPinInput.length == 4) {
                            secureStorage.savePin(newPinInput)
                            currentPin = newPinInput
                            newPinInput = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonGrey)
                ) {
                    Text("Speichern")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            HorizontalDivider(color = Color.DarkGray)
            Spacer(modifier = Modifier.height(32.dp))

            // --- KONTAKTE BEREICH (Jetzt mit echtem Adressbuch!) ---
            Text("Notfallkontakte", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            // Native Kontakt-Auswahl Button
            Button(
                onClick = { contactPickerLauncher.launch(null) }, // Startet das Android-Telefonbuch
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonGrey)
            ) {
                Icon(Icons.Filled.PersonAdd, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Aus Telefonbuch auswählen", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Kontaktliste anzeigen
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(contacts) { contact ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = cardColor)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(contact, color = Color.White, fontSize = 16.sp)
                            IconButton(
                                onClick = {
                                    val updatedList = contacts.toMutableList()
                                    updatedList.remove(contact)
                                    secureStorage.saveContacts(updatedList)
                                    contacts = updatedList
                                }
                            ) {
                                Icon(Icons.Filled.Delete, contentDescription = "Löschen", tint = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}