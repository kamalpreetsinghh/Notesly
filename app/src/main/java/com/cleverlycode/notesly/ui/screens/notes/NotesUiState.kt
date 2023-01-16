package com.cleverlycode.notesly.ui.screens.notes

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import com.cleverlycode.notesly.domain.model.Note

data class NotesUiState(
    val noteType: String = NoteType.ALL.value,
    val notes: List<Note> = emptyList(),
    val isMenuExpanded: Boolean = false,
    val showDialog: Boolean = false,
    val isLoading: Boolean = false,
    val search: String = "",
    val listState: LazyGridState = LazyGridState(),
    val isGridView: Boolean = false
)

enum class NoteType(val value: String) {
    ALL("All"),
    STARRED("Starred"),
    TODO("To-do"),
    TRASH("Trash"),
}

fun getNoteTypes(): List<NoteType> {
    return listOf(NoteType.ALL, NoteType.STARRED, NoteType.TODO, NoteType.TRASH)
}
