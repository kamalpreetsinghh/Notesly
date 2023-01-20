package com.cleverlycode.notesly.ui.screens.notes

import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import com.cleverlycode.notesly.domain.model.Note

data class NotesUiState(
    val selectedChip: String = NoteType.ALL.value,
    val notes: List<Note> = emptyList(),
    val isMenuExpanded: Boolean = false,
    val showDialog: Boolean = false,
    val isLoading: Boolean = false,
    val search: String = "",
    val listState: LazyStaggeredGridState = LazyStaggeredGridState(),
    val isGridLayout: Boolean = true,
    val showCreateNote: Boolean = true
)

enum class NoteType(val value: String) {
    ALL("All"),
    STARRED("Starred"),
    TODO("To-do"),
    TRASH("Trash"),
}

fun getNoteTypes(): List<String> =
    listOf(NoteType.ALL.value, NoteType.STARRED.value, NoteType.TODO.value, NoteType.TRASH.value)

