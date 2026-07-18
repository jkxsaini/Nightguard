package com.example.nightguard.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [EmergencyContact::class], version = 1, exportSchema = false)
abstract class NightguardDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao

    companion object {
        @Volatile
        private var INSTANCE: NightguardDatabase? = null

        fun getDatabase(context: Context): NightguardDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NightguardDatabase::class.java,
                    "nightguard_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}