package com.example.nightguard.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val phoneNumber: String,
    val emergencyContactName: String,
    val emergencyContactPhone: String
)