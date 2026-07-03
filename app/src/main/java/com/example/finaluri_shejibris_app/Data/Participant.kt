package com.example.finaluri_shejibris_app.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "participants")
data class Participant(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String
)