package com.cleverlycode.notesly

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.cleverlycode.notesly.ui.screens.notes.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteslyActivity() : ComponentActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val viewModel: NotesViewModel by viewModels()
        installSplashScreen().setKeepOnScreenCondition(condition = {
            viewModel.notesUiState.value.isLoading
        })

        if (resources.getBoolean(R.bool.portrait_only)) {   //In phones only portrait mode
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }

        setContent {
            NoteslyApp()
        }
    }
}