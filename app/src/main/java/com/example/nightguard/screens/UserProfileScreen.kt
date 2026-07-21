package com.example.nightguard.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nightguard.data.SecureStorage

// Liest jetzt Name UND Nummer aus dem Android-Telefonbuch
fun getContactNameAndNumber(context: Context, uri: Uri): String? {
    var result: String? = null
    val projection = arrayOf(
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER
    )
    val cursor = context.contentResolver.query(uri, projection, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val name = it.getString(0)
            val number = it.getString(1).replace(" ", "") // Entfernt Leerzeichen aus der Nummer
            result = "$name - $number"
        }
    }
    return result
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(onBackToHome: () -> Unit) {
    val context = LocalContext.current
    val secureStorage = remember { SecureStorage(context) }

    var currentPin by remember { mutableStateOf(secureStorage.getPin()) }
    var newPinInput by remember { mutableStateOf("") }
    var contacts by remember { mutableStateOf(secureStorage.getContacts()) }

    val deepWine = Color(0xFF1B040D)
    val cardColor = Color(0xFF2C2C2C)
    val buttonGrey = Color(0xFF333333)

    // Neuer Launcher, der gezielt Telefonnummern abfragt
    val contactPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                val nameAndNumber = getContactNameAndNumber(context, uri)
                if (nameAndNumber != null) {
                    val updatedList = contacts.toMutableList()
                    if (!updatedList.contains(nameAndNumber)) {
                        updatedList.add(nameAndNumber)
                        secureStorage.saveContacts(updatedList)
                        contacts = updatedList
                    }
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

            Text("Notfallkontakte", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Öffnet das Adressbuch gefiltert nach Telefonnummern
                    val intent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
                    contactPickerLauncher.launch(intent)
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonGrey)
            ) {
                Icon(Icons.Filled.PersonAdd, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Aus Telefonbuch auswählen", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

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
                            // Schneidet bei langen Nummern den Text ab, damit das Layout nicht kaputt geht
                            Text(contact, color = Color.White, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
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