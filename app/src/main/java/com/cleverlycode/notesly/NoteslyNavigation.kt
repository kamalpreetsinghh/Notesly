package com.cleverlycode.notesly

import com.cleverlycode.notesly.DestinationsArgs.NOTE_ID_ARG
import com.cleverlycode.notesly.DestinationsArgs.NOTE_TYPE_ARG
import com.cleverlycode.notesly.Screens.NOTES_SCREEN
import com.cleverlycode.notesly.Screens.NOTE_DETAIL_SCREEN

class NoteslyNavigation(private val appState: NoteslyAppState) {
    fun navigateToNotes() {
        appState.navigateAndPopBackStack(NOTES_SCREEN)
    }

    fun navigateToNoteDetail(noteType: String, noteId: Long = 0L) {
        appState.navigate("$NOTE_DETAIL_SCREEN/$noteType/$noteId")
    }
}

private object Screens {
    const val NOTES_SCREEN = "notes"
    const val NOTE_DETAIL_SCREEN = "noteDetail"
}

object DestinationsArgs {
    const val NOTE_ID_ARG = "noteId"
    const val NOTE_TYPE_ARG = "noteType"
}

sealed class Routes(val route: String) {
    object Notes : Routes(route = NOTES_SCREEN)
    object NoteDetail : Routes(route = "$NOTE_DETAIL_SCREEN/{$NOTE_TYPE_ARG}/{$NOTE_ID_ARG}")
}