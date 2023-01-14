package com.cleverlycode.notesly.data.mapper

import com.cleverlycode.notesly.data.local.NoteEntity
import com.cleverlycode.notesly.domain.model.Note
import java.util.*

fun NoteEntity.toNote(): Note {
    return Note(
        id = id,
        title = title,
        description = note,
        noteType = noteType,
        isRecentlyDeleted = isRecentlyDeleted,
        dateUpdated = dateUpdated
    )
}

fun Note.toNoteEntity(): NoteEntity {
    return NoteEntity(
        id = id,
        title = title,
        note = description,
        noteType = noteType,
        isRecentlyDeleted = isRecentlyDeleted,
        dateUpdated = Calendar.getInstance().time
    )
}