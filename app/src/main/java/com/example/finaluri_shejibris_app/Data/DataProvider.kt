package com.example.finaluri_shejibris_app.Data

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    lateinit var db: AppDatabase
        private set

    fun init(context: Context) {
        db = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }
}