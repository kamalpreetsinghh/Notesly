package com.cleverlycode.notesly.domain.model

import com.cleverlycode.notesly.ui.screens.notes.NoteType
import java.util.*

data class Note(
    val id: Long = 0L,
    val title: String = "",
    val description: String = "",
    var noteType: String = NoteType.ALL.value,
    val isRecentlyDeleted: Boolean = false,
    var dateUpdated: Date = Calendar.getInstance().time
)
