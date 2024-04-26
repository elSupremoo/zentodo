package com.thesupreme.ZenToDo

sealed interface NoteEvent {
    object SaveNote: NoteEvent
    data class setTitle(val title: String) : NoteEvent
    object ShowDialog: NoteEvent
    object HideDialog: NoteEvent
    data class DeleteNote(val note: Note): NoteEvent
    data class SortNotes(val sortType: SortType): NoteEvent
}