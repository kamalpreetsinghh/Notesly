package com.cleverlycode.notesly.data.mapper

import com.cleverlycode.notesly.data.local.NoteEntity
import com.cleverlycode.notesly.domain.model.Note
import com.cleverlycode.notesly.ui.screens.notedetail.Task
import com.cleverlycode.notesly.ui.screens.notes.NoteType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

fun NoteEntity.toNote(): Note {
    return Note(
        id = id,
        title = title,
        description = note,
        noteType = noteType,
        tasks = getTasksFromString(taskString = note, noteType = noteType),
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

private fun getTasksFromString(taskString: String, noteType: String): List<Task> {
    var tasks: List<Task> = emptyList()
    if (noteType == NoteType.TODO.value && taskString.isNotBlank()) {
        tasks = Gson()
            .fromJson(
                taskString,
                object : TypeToken<List<Task>>() {}.type
            )
    }

    return if (tasks != null) {
        tasks
    } else {
        emptyList()
    }
}
