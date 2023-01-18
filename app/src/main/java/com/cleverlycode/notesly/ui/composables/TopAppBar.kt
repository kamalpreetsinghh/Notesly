package com.cleverlycode.notesly.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.cleverlycode.notesly.R
import com.cleverlycode.notesly.ui.theme.AppTheme
import com.cleverlycode.notesly.ui.theme.LightRed
import com.cleverlycode.notesly.ui.theme.RedCard

@Composable
fun TopAppBar(
    title: String,
    isEmptyNote: Boolean,
    isStarred: Boolean,
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
    onAddOrRemoveStarred: () -> Unit,
    onMoveToTrash: (() -> Unit) -> Unit
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
            IconButton(
                onClick = {
                    onBackButtonClick(navigateToNotes)
                    keyboardController?.hide()
                },
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .padding(AppTheme.dimens.extra_small_margin),
                colors = IconButtonDefaults.iconButtonColors(containerColor = LightRed)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back_button_label)
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
                IconButton(
                    onClick = { openMenu() },
                    modifier = Modifier
                        .clip(shape = CircleShape)
                        .padding(AppTheme.dimens.extra_small_margin),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = LightRed,
                        disabledContainerColor = LightRed
                    ),
                    enabled = !isMenuExpanded
                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = stringResource(id = R.string.menu_note_description)
                    )
                }
            }

            Menu(
                isExpanded = isMenuExpanded,
                isStarred = isStarred,
                isRecentlyDeleted = isRecentlyDeleted,
                navigateToNotes = navigateToNotes,
                closeMenu = closeMenu,
                onDelete = openDialog,
                onRecover = onRecover,
                onAddOrRemoveStarred = onAddOrRemoveStarred,
                onMoveToTrash = onMoveToTrash
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
