package com.example.nightguard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nightguard.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val _latestUser = MutableStateFlow<User?>(null)
    val latestUser: StateFlow<User?> = _latestUser

    fun saveUser(
        name: String,
        phoneNumber: String,
        emergencyContactName: String,
        emergencyContactPhone: String
    ) {
        viewModelScope.launch {
            // HIER SIND DIE FEHLENDEN PARAMETER:
            val newUser = User(
                name = name,
                phoneNumber = phoneNumber,
                emergencyContactName = emergencyContactName,
                emergencyContactPhone = emergencyContactPhone
            )
            _latestUser.value = newUser
        }
    }
}