package com.example.finaluri_shejibris_app.Data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Participant::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun participantDao(): ParticipantDao
}