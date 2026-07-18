package com.example.nightguard.data

import android.content.Context
import android.content.SharedPreferences

class SecureStorage(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("NightGuardPrefs", Context.MODE_PRIVATE)

    // --- PIN FUNKTIONEN ---
    fun savePin(pin: String) {
        prefs.edit().putString("EMERGENCY_PIN", pin).apply()
    }

    fun getPin(): String {
        return prefs.getString("EMERGENCY_PIN", "1234") ?: "1234"
    }

    // --- KONTAKT FUNKTIONEN ---
    // Speichert die Liste als simplen Text (durch Kommas getrennt)
    fun saveContacts(contacts: List<String>) {
        val contactsString = contacts.joinToString(",")
        prefs.edit().putString("CONTACTS_LIST", contactsString).apply()
    }

    // Ruft die Kontakte ab und wandelt sie wieder in eine Liste um
    fun getContacts(): List<String> {
        val defaultContacts = "Mama,Papa,Anton,Mitbewohnerin"
        val savedString = prefs.getString("CONTACTS_LIST", defaultContacts) ?: defaultContacts

        return if (savedString.isEmpty()) {
            emptyList()
        } else {
            savedString.split(",")
        }
    }
}