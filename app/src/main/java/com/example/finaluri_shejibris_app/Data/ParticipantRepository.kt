package com.example.finaluri_shejibris_app.Data

import kotlinx.coroutines.flow.Flow

class ParticipantRepository(
    private val dao: ParticipantDao
) {

    suspend fun saveParticipants(names: List<String>) {

        val list = names.map {
            Participant(name = it)
        }

        dao.insertAll(list)
    }
    suspend fun addParticipant(name: String) {
        dao.insert(Participant(name = name))
    }

    fun getAllParticipants(): Flow<List<Participant>> {
        return dao.getAllParticipants()
    }

    fun searchParticipants(query: String): Flow<List<Participant>> {
        return dao.searchParticipants(query)
    }

    suspend fun participantExists(name: String): Boolean {
        return dao.participantExists(name)
    }
}