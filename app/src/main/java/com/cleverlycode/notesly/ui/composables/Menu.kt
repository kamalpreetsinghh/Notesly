package com.cleverlycode.notesly.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.PopupProperties
import com.cleverlycode.notesly.R
import com.cleverlycode.notesly.ui.screens.notes.NoteType

@Composable
fun Menu(
    expanded: Boolean,
    selectedNoteChip: String,
    selectedNoteType: String,
    isRecentlyDeleted: Boolean,
    closeMenu: () -> Unit,
    navigateToNotes: () -> Unit,
    onDelete: () -> Unit,
    onRecover: (() -> Unit) -> Unit,
    onMoveTo: (String, () -> Unit) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { closeMenu() },
        properties = PopupProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        if (isRecentlyDeleted) {
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
                        contentDescription = stringResource(id = R.string.delete_note_description)
                    )
                })

            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.recover_note_label)) },
                onClick = { onRecover(navigateToNotes) },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Refresh,
                        contentDescription = null
                    )
                }
            )
        } else {
            if (selectedNoteType == NoteType.STARRED.value) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(id = R.string.remove_from_starred_label),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    onClick = { onMoveTo(NoteType.ALL.value, navigateToNotes) },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = stringResource(id = R.string.delete_note_description)
                        )
                    }
                )
            } else if (selectedNoteType == NoteType.ALL.value && selectedNoteChip != NoteType.TODO.value) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(id = R.string.move_to_starred_label),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    onClick = { onMoveTo(NoteType.STARRED.value, navigateToNotes) },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = stringResource(id = R.string.delete_note_description)
                        )
                    }
                )
            }

            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(
                            id = R.string.move_to_label, NoteType.TRASH.value,
                        ),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                onClick = { onMoveTo(NoteType.TRASH.value, navigateToNotes) },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = stringResource(id = R.string.delete_note_description)
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
    isGridView: Boolean,
    isNotesEmpty: Boolean,
    onDismissRequest: () -> Unit,
    moveToTrash: () -> Unit,
    openDialog: () -> Unit,
    changeNotesLayout: () -> Unit
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
                    text = if (isGridView) "List View" else "Grid View",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            onClick = { changeNotesLayout() },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = if (isGridView) R.drawable.ic_list else R.drawable.ic_grid),
                    contentDescription = stringResource(id = R.string.delete_all_notes_menu_label)
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
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(id = R.string.move_all_notes_trash_menu_label),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    onClick = { moveToTrash() },
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

