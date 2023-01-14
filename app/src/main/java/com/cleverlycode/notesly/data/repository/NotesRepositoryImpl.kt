package com.cleverlycode.notesly.data.repository

import com.cleverlycode.notesly.data.local.NoteEntity
import com.cleverlycode.notesly.data.local.NotesDatabase
import com.cleverlycode.notesly.data.mapper.toNote
import com.cleverlycode.notesly.data.mapper.toNoteEntity
import com.cleverlycode.notesly.domain.model.Note
import com.cleverlycode.notesly.domain.repository.NotesRepository
import com.cleverlycode.notesly.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(
    db: NotesDatabase
) : NotesRepository {
    private val dao = db.dao

    override fun getNotesStream(): Flow<Resource<List<Note>>> {
        return dao.observeNotes().map { noteEntities ->
            Resource.Success(noteEntities.map { noteEntity ->
                noteEntity.toNote()
            })
        }
    }

    override fun getNoteStream(id: Long): Flow<Resource<Note>> {
        return dao.observeNote(id = id).map { noteEntity ->
            Resource.Success(noteEntity.toNote())
        }
    }

    override suspend fun getNotes(): List<Note> {
        return dao.getNotes().map { noteEntity ->
            noteEntity.toNote()
        }
    }

    override suspend fun getNote(id: Long): Resource<Note> =
        coroutineScope {
            try {
                val noteEntity = dao.getNote(id)
                if (noteEntity != null) {
                    return@coroutineScope Resource.Success(data = noteEntity.toNote())
                } else {
                    return@coroutineScope Resource.Error(message = "Note not found")
                }
            } catch (exception: Exception) {
                return@coroutineScope Resource.Error(message = exception.message.toString())
            }
        }

    override suspend fun save(note: Note) =
        coroutineScope {
            dao.insert(
                NoteEntity(
                    title = note.title,
                    note = note.description,
                    noteType = note.noteType,
                    isRecentlyDeleted = note.isRecentlyDeleted,
                    dateUpdated = Calendar.getInstance().time
                )
            )
        }

    override suspend fun update(note: Note) {
        coroutineScope {
            dao.update(note.toNoteEntity())
        }
    }

    override suspend fun delete(note: Note) {
        coroutineScope {
            dao.delete(note.toNoteEntity())
        }
    }

    override suspend fun moveToTrash(noteType: String) {
        coroutineScope {
            dao.moveToTrash(noteType)
        }
    }

    override suspend fun moveAllToTrash() {
        coroutineScope {
            dao.moveAllToTrash()
        }
    }

    override suspend fun emptyTrash() {
        coroutineScope {
            dao.emptyTrash()
        }
    }

    override suspend fun deleteOldNotes(noteIds: List<Long>) {
        withContext(Dispatchers.IO) {
            dao.deleteOldNotes(noteIds = noteIds)
        }
    }
}