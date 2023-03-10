package com.cleverlycode.notesly.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.PopupProperties
import com.cleverlycode.notesly.R
import com.cleverlycode.notesly.ui.screens.notes.NoteType

@Composable
fun Menu(
    isExpanded: Boolean,
    isStarred: Boolean,
    isRecentlyDeleted: Boolean,
    closeMenu: () -> Unit,
    navigateToNotes: () -> Unit,
    onDelete: () -> Unit,
    onRecover: () -> Unit,
    onAddOrRemoveStarred: () -> Unit,
    onMoveToTrash: (() -> Unit) -> Unit,
    showSnackbar: (String, SnackbarDuration) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = { closeMenu() },
        properties = PopupProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        if (isRecentlyDeleted) {
            val recoveredMessage = stringResource(id = R.string.note_recovered_snackbar_message)

            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(id = R.string.delete_note_label),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                onClick = { onDelete() },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = stringResource(id = R.string.delete_note_label)
                    )
                })

            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(id = R.string.recover_note_label),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                onClick = {
                    keyboardController?.hide()
                    onRecover()
                    showSnackbar(recoveredMessage, SnackbarDuration.Short)
                },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Refresh,
                        contentDescription = stringResource(id = R.string.recover_note_label)
                    )
                }
            )
        } else {
            val starredMessage =
                if (isStarred) stringResource(id = R.string.remove_from_starred_label)
                else stringResource(id = R.string.move_to_starred_label)
            val movedToTrashMessage = stringResource(id = R.string.moved_to_trash_snackbar_message)

            DropdownMenuItem(
                text = {
                    Text(
                        text = if (isStarred) stringResource(id = R.string.remove_from_starred_label)
                        else stringResource(id = R.string.move_to_starred_label),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                onClick = {
                    onAddOrRemoveStarred()
                    showSnackbar(starredMessage, SnackbarDuration.Short)
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(
                            id = if (isStarred) R.drawable.ic_star_outline
                            else R.drawable.ic_star
                        ),
                        contentDescription = if (isStarred) stringResource(id = R.string.remove_from_starred_label)
                        else stringResource(id = R.string.move_to_starred_label)
                    )
                }
            )

            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(id = R.string.move_to_trash_label),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                onClick = {
                    keyboardController?.hide()
                    onMoveToTrash(navigateToNotes)
                    showSnackbar(movedToTrashMessage, SnackbarDuration.Short)
                },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = stringResource(id = R.string.move_to_trash_label)
                    )
                }
            )
        }
    }
}

@Composable
fun NotesMenu(
    expanded: Boolean,
    noteType: String,
    isGridLayout: Boolean,
    isNotesEmpty: Boolean,
    onDismissRequest: () -> Unit,
    moveToTrash: () -> Unit,
    openDialog: () -> Unit,
    changeNotesLayout: () -> Unit,
    showSnackbar: (String, SnackbarDuration) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismissRequest() },
        properties = PopupProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(id = if (isGridLayout) R.string.list_view_label else R.string.grid_view_label),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            onClick = { changeNotesLayout() },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = if (isGridLayout) R.drawable.ic_list else R.drawable.ic_grid),
                    contentDescription = stringResource(id = if (isGridLayout) R.string.list_view_label else R.string.grid_view_label)
                )
            }
        )

        if (!isNotesEmpty) {
            if (noteType == NoteType.TRASH.value) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(id = R.string.delete_all_notes_menu_label),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    onClick = { openDialog() },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = stringResource(id = R.string.delete_all_notes_menu_label)
                        )
                    }
                )
            } else {
                val message = stringResource(id = R.string.all_moved_to_trash_snackbar_message)
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(id = R.string.move_all_notes_trash_menu_label),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    onClick = {
                        moveToTrash()
                        showSnackbar(message, SnackbarDuration.Short)
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = stringResource(id = R.string.move_all_notes_trash_menu_label)
                        )
                    }
                )
            }
        }
    }
}

