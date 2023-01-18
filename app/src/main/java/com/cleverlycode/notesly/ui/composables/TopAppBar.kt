package com.cleverlycode.notesly.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.cleverlycode.notesly.R

@Composable
fun TopAppBar(
    title: String,
    isEmptyNote: Boolean,
    selectedNoteChip: String,
    noteType: String,
    isNoteChanged: Boolean,
    isRecentlyDeleted: Boolean,
    isMenuExpanded: Boolean,
    focusManager: FocusManager,
    openMenu: () -> Unit,
    closeMenu: () -> Unit,
    openDialog: () -> Unit,
    onDoneClick: () -> Unit,
    onBackButtonClick: (() -> Unit) -> Unit,
    navigateToNotes: () -> Unit,
    onRecover: (() -> Unit) -> Unit,
    onMoveTo: (String, () -> Unit) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                onBackButtonClick(navigateToNotes)
                keyboardController?.hide()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },
        actions = {
            if (!isEmptyNote && isNoteChanged) {
                IconButton(
                    onClick = {
                        focusManager.clearFocus()
                        onDoneClick()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = stringResource(id = R.string.done_note_description)
                    )
                }
            }

            if (!isEmptyNote) {
                IconButton(onClick = { openMenu() }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = stringResource(id = R.string.menu_note_description)
                    )
                }
            }

            Menu(
                expanded = isMenuExpanded,
                selectedNoteChip = selectedNoteChip,
                selectedNoteType = noteType,
                isRecentlyDeleted = isRecentlyDeleted,
                navigateToNotes = navigateToNotes,
                closeMenu = closeMenu,
                onDelete = openDialog,
                onRecover = onRecover,
                onMoveTo = onMoveTo
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            navigationIconContentColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = MaterialTheme.colorScheme.primary
        )
    )
}
