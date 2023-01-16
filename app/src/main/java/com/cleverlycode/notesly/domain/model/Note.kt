package com.cleverlycode.notesly.domain.model

import com.cleverlycode.notesly.ui.screens.notedetail.Task
import com.cleverlycode.notesly.ui.screens.notes.NoteType
import java.util.*

data class Note(
    val id: Long = 0L,
    val title: String = "",
    val description: String = "",
    var noteType: String = NoteType.ALL.value,
    val tasks: List<Task> = emptyList(),
    val isRecentlyDeleted: Boolean = false,
    var dateUpdated: Date = Calendar.getInstance().time
)
