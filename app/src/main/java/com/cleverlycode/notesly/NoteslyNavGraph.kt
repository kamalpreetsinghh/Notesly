package com.cleverlycode.notesly

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.cleverlycode.notesly.ui.screens.notedetail.NoteScreen
import com.cleverlycode.notesly.ui.screens.notes.NotesScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

@Composable
fun NoteslyNavGraph(
    modifier: Modifier = Modifier,
    appState: NoteslyAppState = rememberAppState(),
    startDestination: String = Routes.Notes.route,
    noteslyNavigation: NoteslyNavigation = NoteslyNavigation(appState)
) {
    AnimatedNavHost(
        navController = appState.navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(
            route = Routes.Notes.route,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            NotesScreen(
                snackbarHostState = appState.snackbarHostState,
                navigateToNoteDetail = { noteType, noteId ->
                    noteslyNavigation.navigateToNoteDetail(
                        noteType = noteType,
                        noteId = noteId
                    )
                },
                showSnackbar = { message, duration ->
                    appState.showSnackbar(
                        message = message,
                        duration = duration
                    )
                }
            )
        }
        composable(
            route = Routes.NoteDetail.route,
            arguments = listOf(
                navArgument(DestinationsArgs.NOTE_TYPE_ARG) { type = NavType.StringType },
                navArgument(DestinationsArgs.NOTE_ID_ARG) { type = NavType.LongType }
            ),
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
        ) {
            NoteScreen(
                snackbarHostState = appState.snackbarHostState,
                navigateToNotes = { noteslyNavigation.navigateToNotes() },
                showSnackbar = { message, duration ->
                    appState.showSnackbar(
                        message = message,
                        duration = duration
                    )
                }
            )
        }
    }
}