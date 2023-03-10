package com.cleverlycode.notesly

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.cleverlycode.notesly.ui.theme.NoteslyTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.CoroutineScope

@Composable
fun NoteslyApp() {
    NoteslyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NoteslyNavGraph()
        }
    }
}

@Composable
fun rememberAppState(
    navController: NavHostController = rememberAnimatedNavController(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) = remember(navController, snackbarHostState, coroutineScope) {
    NoteslyAppState(
        navController = navController,
        snackbarHostState = snackbarHostState,
        coroutineScope = coroutineScope
    )
}