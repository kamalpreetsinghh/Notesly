package com.cleverlycode.notesly.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cleverlycode.notesly.ui.screens.notes.NoteType
import java.util.*

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String = "",
    val note: String = "",
    val noteType: String = NoteType.ALL.value,
    val isRecentlyDeleted: Boolean = false,
    val dateUpdated: Date = Calendar.getInstance().time
)