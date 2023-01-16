package com.cleverlycode.notesly.ui.screens.notes

import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cleverlycode.notesly.domain.model.Note
import com.cleverlycode.notesly.domain.repository.NotesRepository
import com.cleverlycode.notesly.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repository: NotesRepository
) : ViewModel() {
    val notesUiState = mutableStateOf(NotesUiState())
    private val noteType get() = notesUiState.value.noteType
    private val isGridView get() = notesUiState.value.isGridView
    private var allNotes: List<Note> = emptyList()

    init {
        initializeNotes()
    }

    private fun initializeNotes() {
        showLoader()
        viewModelScope.launch {
            repository.getNotesStream().collect { result ->
                if (result is Resource.Success) {
                    hideLoader()
                    result.data?.let { notes ->
                        allNotes = notes
                        val filteredNotes = getFilteredNotes(allNotes, noteType)
                        notesUiState.value = notesUiState.value.copy(
                            notes = filteredNotes,
                            listState = LazyStaggeredGridState(initialFirstVisibleItemIndex = 0)
                        )
                        deleteOldNotes(notes)
                    }
                }
            }
        }
    }

    fun onClickNoteTypeChip(noteType: String) {
        val filteredNotes = getFilteredNotes(allNotes, noteType)
        notesUiState.value = notesUiState.value.copy(
            notes = filteredNotes,
            noteType = noteType,
            listState = LazyStaggeredGridState(initialFirstVisibleItemIndex = 0)
        )
    }

    fun changeNotesLayout() {
        notesUiState.value =
            notesUiState.value.copy(isGridView = !isGridView, isMenuExpanded = false)
    }

    fun onCreateNoteButtonClick(navigateToNoteDetail: (String, Long) -> Unit) {
        navigateToNoteDetail(noteType, 0)
    }

    fun onClickNote(noteId: Long, navigateToNoteDetail: (String, Long) -> Unit) {
        navigateToNoteDetail(noteType, noteId)
    }

    fun onSearchTextChange(newValue: String) {
        notesUiState.value = notesUiState.value.copy(
            search = newValue,
            notes = searchedNotes(search = newValue)
        )
    }

    fun deleteNotes() {
        when (noteType) {
            NoteType.ALL.value -> moveAllToTrash()
            else -> moveToTrash(noteType)
        }
        closeMenu()
    }

    fun emptyTrash() {
        viewModelScope.launch {
            repository.emptyTrash()
        }
        closeMenu()
        closeDialog()
    }

    fun openDialog() {
        notesUiState.value = notesUiState.value.copy(showDialog = true)
    }

    fun closeDialog() {
        notesUiState.value = notesUiState.value.copy(showDialog = false)
    }

    fun openMenu() {
        notesUiState.value = notesUiState.value.copy(isMenuExpanded = true)
    }

    fun closeMenu() {
        notesUiState.value = notesUiState.value.copy(isMenuExpanded = false)
    }

    private fun showLoader() {
        notesUiState.value = notesUiState.value.copy(isLoading = true)
    }

    private fun hideLoader() {
        notesUiState.value = notesUiState.value.copy(isLoading = false)
    }

    private fun moveToTrash(noteType: String) {
        viewModelScope.launch {
            repository.moveToTrash(noteType = noteType)
        }
    }

    private fun moveAllToTrash() {
        viewModelScope.launch {
            repository.moveAllToTrash()
        }
    }

    private fun getFilteredNotes(notes: List<Note>, noteType: String): List<Note> {
        return when (noteType) {
            NoteType.ALL.value -> {
                notes.filter { note -> !note.isRecentlyDeleted }
            }
            NoteType.TRASH.value -> {
                notes.filter { note -> note.isRecentlyDeleted }
            }
            else -> {
                notes.filter { note -> note.noteType == noteType && !note.isRecentlyDeleted }
            }
        }
    }

    private fun searchedNotes(search: String): List<Note> =
        getFilteredNotes(notes = allNotes, noteType = noteType).filter { note ->
            note.title.contains(
                other = search,
                ignoreCase = true
            )
        }

    private fun deleteOldNotes(notes: List<Note>) {
        val noteIds = notes.filter { note ->
            Period.between(
                note.dateUpdated.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                LocalDate.now()
            ).days > 30
        }.map {
            it.id
        }

        viewModelScope.launch {
            repository.deleteOldNotes(noteIds = noteIds)
        }
    }
}