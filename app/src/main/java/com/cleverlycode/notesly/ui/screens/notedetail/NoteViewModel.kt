package com.cleverlycode.notesly.ui.screens.notedetail

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cleverlycode.notesly.domain.model.Note
import com.cleverlycode.notesly.domain.repository.NotesRepository
import com.cleverlycode.notesly.ui.screens.notes.NoteType
import com.cleverlycode.notesly.util.Resource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle, private val repository: NotesRepository
) : ViewModel() {
    private var noteId: Long = checkNotNull(savedStateHandle["noteId"])
    private val originalNoteType: String = savedStateHandle["noteType"] ?: NoteType.ALL.value

    val noteUiState = mutableStateOf(NoteUiState())

    private var tasks: SnapshotStateList<Task> = mutableStateListOf(Task())
    private val _tasksFlow = MutableStateFlow(tasks)
    val tasksFlow: StateFlow<List<Task>> = _tasksFlow.asStateFlow()

    private val title get() = noteUiState.value.title
    private val description get() = noteUiState.value.description
    private val noteType get() = noteUiState.value.noteType
    private val isRecentlyDeleted get() = noteUiState.value.isRecentlyDeleted
    private val isNoteChanged get() = noteUiState.value.isNoteChanged

    init {
        if (isNewNote()) {
            noteUiState.value = noteUiState.value.copy(noteType = originalNoteType)
        } else {
            getNote(noteId)
        }
    }

    fun onTitleChange(newValue: String) {
        noteUiState.value = noteUiState.value.copy(title = newValue, isNoteChanged = true)
    }

    fun onNoteChange(newValue: String) {
        noteUiState.value = noteUiState.value.copy(description = newValue, isNoteChanged = true)
    }

    fun onTaskNameChange(taskId: Int, newValue: String) {
        tasks[taskId] = tasks[taskId].copy(name = newValue)
        noteUiState.value = noteUiState.value.copy(isNoteChanged = true)

        if (newValue.isBlank()) {
            if (taskId != 0) {
                removeTaskItem(taskId = taskId)
            } else {
                tasks[taskId] = tasks[taskId].copy(isVisible = false)
            }
        }
    }

    fun onFocused(taskId: Int) {
        if (taskId == 0 || tasks[taskId - 1].name.isNotBlank()) {    //Check if previous task is not empty
            if (!tasks[taskId].isVisible) {
                tasks[taskId] = tasks[taskId].copy(isVisible = true, name = "")
            }

            createNewTaskItem(taskId)
        }
    }

    fun onTaskStatusChange(taskId: Int, isDone: Boolean) {
        tasks[taskId] = tasks[taskId].copy(isDone = isDone)
        noteUiState.value = noteUiState.value.copy(isNoteChanged = true)
    }

    fun onBackButtonClick(navigateToNotes: () -> Unit) {
        if (isNoteChanged) {
            if (isEmptyNote()) {
                if (isSavedNote()) {
                    deleteNote(note = Note(id = noteId))
                }
            } else {
                if (noteType == NoteType.TODO.value) {
                    saveTasksToDescription()
                }
                saveOrUpdateNote()
            }
        }

        navigateToNotes()
    }

    fun onDoneClick() {
        if (isNoteChanged) {
            if (noteType == NoteType.TODO.value) {
                saveTasksToDescription()
            }
            saveOrUpdateNote()
            noteUiState.value = noteUiState.value.copy(isNoteChanged = false)
        }
    }

    fun onAddOrRemoveStarred() {
        closeMenu()
        noteUiState.value = noteUiState.value.copy(noteType = getChangedNoteType())
        saveOrUpdateNote()
    }

    fun moveToTrash(navigateToNotes: () -> Unit) {
        closeMenu()
        noteUiState.value = noteUiState.value.copy(isRecentlyDeleted = true)
        saveOrUpdateNote()
        navigateToNotes()
    }

    fun onRecoverClick() {
        closeMenu()
        val note = Note(
            id = noteId,
            title = title,
            description = description,
            noteType = noteType,
            isRecentlyDeleted = false
        )
        updateNote(note)
    }

    fun onDeleteConfirmClick(navigateToNotes: () -> Unit) {
        closeDialog()
        deleteNote(note = Note(id = noteId))
        navigateToNotes()
    }

    fun isNewNote() = noteId == 0L

    fun isEmptyNote() = title.isBlank() && description.isBlank()

    fun openDialog() {
        noteUiState.value = noteUiState.value.copy(showDialog = true)
    }

    fun closeDialog() {
        noteUiState.value = noteUiState.value.copy(showDialog = false)
    }

    fun openMenu() {
        noteUiState.value = noteUiState.value.copy(isMenuExpanded = true)
    }

    fun closeMenu() {
        noteUiState.value = noteUiState.value.copy(isMenuExpanded = false)
    }

    private fun getChangedNoteType(): String =
        if (noteType == NoteType.ALL.value) NoteType.STARRED.value else NoteType.ALL.value

    private fun saveOrUpdateNote() {
        if (isNewNote()) {
            viewModelScope.launch {
                noteId = saveNoteAsync(
                    note = Note(
                        title = title,
                        description = description,
                        noteType = noteType,
                        isRecentlyDeleted = isRecentlyDeleted
                    )
                ).await()
            }
        } else {
            updateNote(
                note = Note(
                    id = noteId,
                    title = title,
                    description = description,
                    noteType = noteType,
                    isRecentlyDeleted = isRecentlyDeleted
                )
            )
        }
    }

    private fun getNote(noteId: Long) {
        viewModelScope.launch {
            val result = repository.getNote(id = noteId)

            if (result is Resource.Success) {
                val note = result.data
                if (note != null) {
                    noteUiState.value = NoteUiState(
                        title = note.title,
                        description = note.description,
                        noteType = note.noteType,
                        isRecentlyDeleted = note.isRecentlyDeleted,
                        dateUpdated = formatDate(note.dateUpdated)
                    )

                    if (note.noteType == NoteType.TODO.value) {
                        getTasksFromDescription()
                    }
                }
            } else if (result is Resource.Error) {
                noteUiState.value = noteUiState.value.copy(isError = true)
            }
        }
    }

    private fun saveNoteAsync(note: Note) =
        viewModelScope.async {
            repository.save(note = note)
        }

    private fun updateNote(note: Note) {
        viewModelScope.launch {
            repository.update(note = note)
        }
    }

    private fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.delete(note = note)
        }
    }

    private fun formatDate(date: Date): String {
        return "${
            SimpleDateFormat.getDateInstance().format(date)
        } at ${SimpleDateFormat.getTimeInstance(DateFormat.SHORT).format(date)}"
    }

    private fun isSavedNote() = noteId != 0L

    private fun createNewTaskItem(taskId: Int) {
        if (tasks.size == taskId + 1) {         // To create a new task in the end.
            tasks.add(taskId + 1, Task())
        }
    }

    private fun removeTaskItem(taskId: Int) {
        tasks.removeAt(index = taskId)
    }

    private fun saveTasksToDescription() {
        val tasksJsonString = Gson().toJson(tasks.filter { it.name.isNotBlank() })
        noteUiState.value = noteUiState.value.copy(description = tasksJsonString)
    }

    private fun getTasksFromDescription() {
        val tasksList = Gson()
            .fromJson<List<Task>>(
                description,
                object : TypeToken<List<Task>>() {}.type
            )
        if (tasksList != null && tasksList.isNotEmpty()) {
            tasks = tasksList.toMutableStateList()
            tasks.add(tasks.size, Task())   // add additional empty task which will be invisible
            _tasksFlow.value = tasks
        }
    }
}
