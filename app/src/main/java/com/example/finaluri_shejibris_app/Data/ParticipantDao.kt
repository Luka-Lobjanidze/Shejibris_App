package com.example.finaluri_shejibris_app.Data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ParticipantDao {

    @Insert
    suspend fun insert(participant: Participant)

    @Insert
    suspend fun insertAll(participants: List<Participant>)

    @Query("SELECT * FROM participants")
    fun getAllParticipants(): Flow<List<Participant>>

    @Query("SELECT * FROM participants WHERE name LIKE '%' || :query || '%' LIMIT 10")
    fun searchParticipants(query: String): Flow<List<Participant>>

    @Query("SELECT EXISTS(SELECT 1 FROM participants WHERE name = :name)")
    suspend fun participantExists(name: String): Boolean
}