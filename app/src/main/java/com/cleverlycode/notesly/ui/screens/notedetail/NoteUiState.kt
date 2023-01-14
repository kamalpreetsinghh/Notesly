package com.cleverlycode.notesly.ui.screens.notedetail

import com.cleverlycode.notesly.ui.screens.notes.NoteType

data class NoteUiState(
    val title: String = "",
    val description: String = "",
    val noteType: String = NoteType.ALL.value,
    val isRecentlyDeleted: Boolean = false,
    val dateUpdated: String = "",
    val isNoteChanged: Boolean = false,
    val showDialog: Boolean = false,
    val isMenuExpanded: Boolean = false,
    val isError: Boolean = false
)

data class Task(
    var name: String = "",
    val isDone: Boolean = false,
    val isVisible: Boolean = false
)