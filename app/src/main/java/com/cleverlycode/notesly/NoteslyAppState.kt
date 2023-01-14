package com.cleverlycode.notesly

import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController

@Stable
class NoteslyAppState(
    var navController: NavHostController,
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
}