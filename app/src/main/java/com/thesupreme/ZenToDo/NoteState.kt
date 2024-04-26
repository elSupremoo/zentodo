package com.thesupreme.ZenToDo

data class NoteState(
    val notes: List<Note> = emptyList(),
    val title: String = "",
    val isAddingNote: Boolean = false,
    val sortType: SortType = SortType.TITLE
)
