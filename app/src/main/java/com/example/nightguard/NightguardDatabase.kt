package com.example.nightguard

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [User::class],
    version = 1
)
abstract class NightguardDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

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