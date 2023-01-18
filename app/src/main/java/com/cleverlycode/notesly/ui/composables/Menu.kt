package com.cleverlycode.notesly.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
    isExpanded: Boolean,
    isStarred: Boolean,
    isRecentlyDeleted: Boolean,
    closeMenu: () -> Unit,
    navigateToNotes: () -> Unit,
    onDelete: () -> Unit,
    onRecover: (() -> Unit) -> Unit,
    onAddOrRemoveStarred: () -> Unit,
    onMoveToTrash: (() -> Unit) -> Unit
) {
    DropdownMenu(
        expanded = isExpanded,
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
                onClick = { onRecover(navigateToNotes) },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Refresh,
                        contentDescription = stringResource(id = R.string.recover_note_label)
                    )
                }
            )
        } else {
            DropdownMenuItem(
                text = {
                    Text(
                        text = if (isStarred) stringResource(id = R.string.remove_from_starred_label)
                        else stringResource(id = R.string.move_to_starred_label),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                onClick = { onAddOrRemoveStarred() },
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
                onClick = { onMoveToTrash(navigateToNotes) },
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

