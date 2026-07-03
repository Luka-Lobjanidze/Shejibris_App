package com.example.finaluri_shejibris_app.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finaluri_shejibris_app.Data.ParticipantRepository

class ParticipantsViewModelFactory(
    private val repository: ParticipantRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ParticipantsViewModel(repository) as T
    }


}