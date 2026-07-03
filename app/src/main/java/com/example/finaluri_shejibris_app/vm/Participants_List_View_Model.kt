package com.example.finaluri_shejibris_app.vm
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finaluri_shejibris_app.Data.DatabaseProvider
import com.example.finaluri_shejibris_app.Data.Participant
import com.example.finaluri_shejibris_app.Data.ParticipantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.collections.emptyList
import kotlin.random.Random


class ParticipantsViewModel( private val repository: ParticipantRepository) : ViewModel() {
    var showFinishDialog by mutableStateOf(false)

    private val _participantsWon = MutableStateFlow(List(15) { "" })
    val participantsWon = _participantsWon.asStateFlow()


    private val _inputText = MutableStateFlow("")


    val inputText: StateFlow<String> = _inputText.asStateFlow()
    private val _participantsItems = MutableStateFlow<List<String>>(emptyList())

    val participantsItems: StateFlow<List<String>> = _participantsItems.asStateFlow()
    private val _seededParticipantsItems = MutableStateFlow<List<String>>(emptyList())

    val seededParticipantsItems: StateFlow<List<String>> = _seededParticipantsItems.asStateFlow()

    fun onTextChange(newText: String) {

        _inputText.value = newText
    }

    fun addItem() {

        val currentText = _inputText.value.trim()
        if (currentText.isNotBlank()) {
            _participantsItems.value = _participantsItems.value + currentText


            _inputText.value = ""
        }
    }

    fun addSeededItem() {

        val currentText = _inputText.value.trim()
        if (currentText.isNotBlank()) {
            _seededParticipantsItems.value = _seededParticipantsItems.value + currentText


            _inputText.value = ""
        }
    }


    fun transferItems(str: String) {

        if (str.trim().isNotBlank()) {

            _seededParticipantsItems.value = _seededParticipantsItems.value + str.trim()
        }
    }


    fun updateText(index: Int, newText: String) {
        val currentList = _participantsItems.value.toMutableList()

        if (index in currentList.indices) {
            currentList[index] = newText
            _participantsItems.value = currentList
        }
    }


    fun updateSeededText(index: Int, newText: String) {
        val currentList = _seededParticipantsItems.value.toMutableList()

        if (index in currentList.indices) {
            currentList[index] = newText
            _seededParticipantsItems.value = currentList
        }
    }

    fun removeItem(item: String) {

        _participantsItems.value = _participantsItems.value - item
    }

    fun removeSeededItem(item: String) {

        _seededParticipantsItems.value = _seededParticipantsItems.value - item
    }

    fun startCompetition() {
        val temp = _participantsItems.value.toMutableList()

        while (temp.isNotEmpty()) {
            val index = Random.nextInt(temp.size)
            val item = temp.removeAt(index)

            transferItems(item)
        }

        _participantsItems.value = emptyList()
    }

    fun addParticipants(player: String) {


        if (player.isNotBlank()) {
            _participantsItems.value = _participantsItems.value + player

        }
    }

    var lastCheckedWinner by mutableStateOf("")
    fun checkTournamentFinished() {
        val winner = participantsWon.value.getOrNull(0) ?: return

        if (winner.isNotBlank() && winner != lastCheckedWinner) {
            lastCheckedWinner = winner
            showFinishDialog = true
        }
    }

    fun setWinner(index: Int, winner: String) {
        val current = _participantsWon.value.toMutableList()

        current[index] =
            if (current[index] == winner) ""   // toggle (მეორედ დაჭერით ქრება)
            else winner

        _participantsWon.value = current
        checkTournamentFinished()
    }

    val allParticipants: StateFlow<List<Participant>> =
        repository.getAllParticipants().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )





    fun saveParticipants() {

        viewModelScope.launch {

            repository.saveParticipants(
                participantsItems.value
            )

        }
    }



    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    val searchResults = _query
        .debounce(200)
        .flatMapLatest { repository.searchParticipants(it) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun onQueryChange(text: String) {
        _query.value = text
    }

    fun addParticipant(name: String) {
        viewModelScope.launch {

            val exists = repository.participantExists(name.trim())

            if (!exists) {
                repository.addParticipant(name.trim())
            }
        }
    }
    fun resetCompetition() {
        _participantsItems.value = emptyList()
        _seededParticipantsItems.value = emptyList()
        _participantsWon.value = List(15) { "" }
    }

}




