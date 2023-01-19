package com.cleverlycode.notesly

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Stable
class NoteslyAppState(
    val navController: NavHostController,
    val snackbarHostState: SnackbarHostState,
    private val coroutineScope: CoroutineScope
) {
    fun navigate(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
        }
    }

    fun navigateAndPopBackStack(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
            navController.popBackStack()
        }
    }

    fun showSnackbar(message: String, duration: SnackbarDuration = SnackbarDuration.Short) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                duration = duration
            )
        }
    }
}