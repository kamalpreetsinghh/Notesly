package com.cleverlycode.notesly.domain.repository

import com.cleverlycode.notesly.domain.model.Note
import com.cleverlycode.notesly.util.Resource
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun getNotesStream(): Flow<Resource<List<Note>>>
    fun getNoteStream(id: Long): Flow<Resource<Note>>
    suspend fun getNotes(): List<Note>
    suspend fun getNote(id: Long): Resource<Note>
    suspend fun save(note: Note): Long
    suspend fun update(note: Note)
    suspend fun delete(note: Note)
    suspend fun moveToTrash(noteType: String)
    suspend fun moveAllToTrash()
    suspend fun emptyTrash()
    suspend fun deleteOldNotes(noteIds: List<Long>)
}