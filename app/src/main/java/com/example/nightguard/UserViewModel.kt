package com.example.nightguard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val database = NightguardDatabase.getDatabase(application)
    private val userDao = database.userDao()

    val latestUser = userDao.getLatestUser().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val allUsers = userDao.getAllUsers().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun saveUser(
        name: String,
        phoneNumber: String,
        emergencyContactName: String,
        emergencyContactPhone: String
    ) {
        viewModelScope.launch {
            userDao.insertUser(
                User(
                    name = name,
                    phoneNumber = phoneNumber,
                    emergencyContactName = emergencyContactName,
                    emergencyContactPhone = emergencyContactPhone
                )
            )
        }
    }
}