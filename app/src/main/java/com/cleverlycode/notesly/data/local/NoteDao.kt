package com.cleverlycode.notesly.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * from notes ORDER BY dateUpdated DESC")
    fun observeNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * from notes WHERE id = :id")
    fun observeNote(id: Long): Flow<NoteEntity>

    @Query("SELECT * from notes ORDER BY dateUpdated DESC")
    suspend fun getNotes(): List<NoteEntity>

    @Query("SELECT * from notes WHERE id = :id")
    suspend fun getNote(id: Long): NoteEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: NoteEntity): Long

    @Update
    suspend fun update(note: NoteEntity)

    @Delete
    suspend fun delete(note: NoteEntity)

    @Query("UPDATE notes SET isRecentlyDeleted = :isRecentlyDeleted WHERE noteType = :noteType")
    suspend fun moveToTrash(noteType: String, isRecentlyDeleted: Boolean = true)

    @Query("UPDATE notes SET isRecentlyDeleted = :isRecentlyDeleted WHERE NOT isRecentlyDeleted")
    suspend fun moveAllToTrash(isRecentlyDeleted: Boolean = true)

    @Query("DELETE FROM notes WHERE isRecentlyDeleted")
    suspend fun emptyTrash()

    @Query("DELETE FROM notes WHERE id in (:noteIds)")
    suspend fun deleteOldNotes(noteIds: List<Long>)
}