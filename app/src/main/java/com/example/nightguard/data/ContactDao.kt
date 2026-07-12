package com.example.nightguard.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Query("SELECT * FROM emergency_contacts LIMIT 1")
    fun getContact(): Flow<EmergencyContact?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveContact(contact: EmergencyContact)
}