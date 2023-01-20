package com.cleverlycode.notesly.ui.screens.notedetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cleverlycode.notesly.R
import com.cleverlycode.notesly.ui.composables.AlertDialog
import com.cleverlycode.notesly.ui.composables.TodoItems
import com.cleverlycode.notesly.ui.composables.TopAppBar
import com.cleverlycode.notesly.ui.screens.notes.NoteType
import com.cleverlycode.notesly.ui.theme.AppTheme

@Composable
fun NoteScreen(
    snackbarHostState: SnackbarHostState,
    navigateToNotes: () -> Unit,
    showSnackbar: (String, SnackbarDuration) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NoteViewModel = hiltViewModel()
) {
    val noteUiState by viewModel.noteUiState
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = Unit) {
        if (viewModel.isEmptyNote()) {
            focusRequester.requestFocus()
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = AppTheme.dimens.horizontal_margin),
        topBar = {
            TopAppBar(
                title = stringResource(
                    id = if (viewModel.isNewNote()) R.string.add_note_screen_title
                    else R.string.edit_note_screen_title
                ),
                isEmptyNote = viewModel.isEmptyNote(),
                isStarred = noteUiState.noteType == NoteType.STARRED.value,
                isNoteChanged = noteUiState.isNoteChanged,
                isRecentlyDeleted = noteUiState.isRecentlyDeleted,
                isMenuExpanded = noteUiState.isMenuExpanded,
                focusManager = focusManager,
                openMenu = { viewModel.openMenu() },
                closeMenu = { viewModel.closeMenu() },
                openDialog = { viewModel.openDialog() },
                onDoneClick = { viewModel.onDoneClick() },
                onBackButtonClick = { navigate -> viewModel.onBackButtonClick(navigate) },
                navigateToNotes = navigateToNotes,
                onRecover = { viewModel.onRecoverClick() },
                onAddOrRemoveStarred = { viewModel.onAddOrRemoveStarred() },
                onMoveToTrash = { navigate -> viewModel.moveToTrash(navigate) },
                showSnackbar = showSnackbar
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(height = AppTheme.dimens.spacer))
            Text(
                text = noteUiState.dateUpdated,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                color = Color.DarkGray,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(height = AppTheme.dimens.spacer))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
            ) {
                val modifiers =
                    if (viewModel.isEmptyNote()) Modifier.focusRequester(focusRequester)
                    else Modifier
                item {
                    TextField(
                        value = noteUiState.title,
                        onValueChange = { viewModel.onTitleChange(newValue = it) },
                        modifier = modifiers.fillMaxWidth(),
                        textStyle = MaterialTheme.typography.displayLarge,
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            cursorColor = MaterialTheme.colorScheme.primary,
                            focusedIndicatorColor = MaterialTheme.colorScheme.background,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.background
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Sentences
                        )
                    )
                }

                item {
                    if (noteUiState.noteType != NoteType.TODO.value) {
                        TextField(
                            value = noteUiState.description,
                            onValueChange = { viewModel.onNoteChange(newValue = it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = AppTheme.dimens.horizontal_margin),
                            textStyle = MaterialTheme.typography.bodyLarge,
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                cursorColor = MaterialTheme.colorScheme.primary,
                                focusedIndicatorColor = MaterialTheme.colorScheme.background,
                                unfocusedIndicatorColor = MaterialTheme.colorScheme.background
                            ),
                            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                        )
                    } else {
                        TodoItems(
                            tasks = viewModel.tasksFlow.collectAsState(),
                            onCheckedChange = { taskId, isDone ->
                                viewModel.onTaskStatusChange(
                                    taskId,
                                    isDone
                                )
                            },
                            onChange = { taskId, name ->
                                viewModel.onTaskNameChange(
                                    taskId,
                                    name
                                )
                            },
                            onFocused = { taskId -> viewModel.onFocused(taskId) },
                            focusRequester = focusRequester
                        )
                    }
                }
            }
        }

        if (noteUiState.showDialog) {
            AlertDialog(
                title = stringResource(id = R.string.delete_note_dialog_title),
                text = stringResource(id = R.string.delete_note_dialog_text),
                confirmButtonText = stringResource(id = R.string.delete_note_label),
                dismissButtonText = stringResource(id = R.string.cancel_label),
                navigateToNotes = navigateToNotes,
                onDismiss = { viewModel.closeDialog() },
                confirmButtonClick = { navigate -> viewModel.onDeleteConfirmClick(navigate) },
                showSnackbar = showSnackbar,
                snackbarMessage = stringResource(id = R.string.note_deleted_snackbar_message)
            )
        }
    }
}