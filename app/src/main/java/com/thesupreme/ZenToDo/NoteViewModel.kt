package com.thesupreme.ZenToDo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NoteViewModel(
    private val dao: NoteDao
): ViewModel() {

    private val _state = MutableStateFlow(NoteState())
    private val _sortType = MutableStateFlow(SortType.TITLE)
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _notes = _sortType.flatMapLatest {
        sortType->
        when(sortType) {
            SortType.TITLE -> dao.getNotesOrderedByTitle()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    }

    val state = combine(_state, _sortType, _notes) { state, sortType, notes ->
        state.copy(
            notes = notes,
            sortType = sortType,

        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteState())
    fun onEvent(event: NoteEvent) {
        when(event) {
            NoteEvent.HideDialog -> {
                _state.update { it.copy(
                    isAddingNote = false
                ) }
            }
            NoteEvent.SaveNote -> {
                val title = state.value.title

                if(title.isBlank()) {
                    return
                }

                val note = Note(
                    title = title,
                )

                viewModelScope.launch {
                    dao.upsertNote(note)
                }

                _state.update { it.copy(
                    isAddingNote = false,
                    title = "",
                ) }
            }
            NoteEvent.ShowDialog -> {
                _state.update { it.copy(
                    isAddingNote = true
                ) }
            }

            is NoteEvent.setTitle -> {
                _state.update { it.copy(
                    title = event.title
                ) }
            }

            is NoteEvent.DeleteNote -> {
                viewModelScope.launch {
                    dao.deleteNote(event.note)
                }
            }

            is NoteEvent.SortNotes -> {
                _sortType.value = event.sortType
            }
        }
    }
}