package com.cleverlycode.notesly.ui.screens.notes

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cleverlycode.notesly.R
import com.cleverlycode.notesly.ui.composables.AlertDialog
import com.cleverlycode.notesly.ui.composables.NoteCards
import com.cleverlycode.notesly.ui.composables.NoteTypesChips
import com.cleverlycode.notesly.ui.composables.NotesMenu
import com.cleverlycode.notesly.ui.theme.SearchBarColorDark
import com.cleverlycode.notesly.ui.theme.SearchBarColorLight

@Composable
fun NotesScreen(
    navigateToNoteDetail: (String, Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val notesUiState by viewModel.notesUiState
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = dimensionResource(id = R.dimen.horizontal_margin),
                vertical = dimensionResource(id = R.dimen.vertical_margin)
            )
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Column(
            modifier = if (notesUiState.noteType == NoteType.TRASH.value) modifier
            else modifier.fillMaxHeight(0.92f)
        ) {
            Text(
                text = stringResource(id = R.string.my_notes_label),
                style = MaterialTheme.typography.displayLarge
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacer)))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NoteTypesChips(
                    noteTypes = getNoteTypes(),
                    selectedNoteType = notesUiState.noteType,
                    onSelectionChanged = { noteType -> viewModel.onClickNoteTypeChip(noteType) },
                    modifier = Modifier.fillMaxWidth(0.93f)
                )

                Row {
                    IconButton(onClick = { viewModel.openMenu() }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = stringResource(id = R.string.menu_note_description)
                        )
                    }
                    NotesMenu(expanded = notesUiState.isMenuExpanded,
                        noteType = notesUiState.noteType,
                        onDismissRequest = { viewModel.closeMenu() },
                        moveToTrash = { viewModel.deleteNotes() },
                        openDialog = { viewModel.openDialog() })
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacer)))

            if (notesUiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                SearchBar(
                    search = notesUiState.search,
                    isVisible = notesUiState.notes.isNotEmpty() || notesUiState.search.isNotEmpty(),
                    onChange = { newValue -> viewModel.onSearchTextChange(newValue) })

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacer)))

                AnimatedVisibility(
                    visible = notesUiState.noteType == NoteType.TRASH.value &&
                            (notesUiState.notes.isNotEmpty() || notesUiState.search.isNotEmpty())
                ) {
                    Text(
                        text = stringResource(id = R.string.trash_notes_message),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light
                    )

                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacer)))
                }

                if (notesUiState.notes.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val emptyNoteMsg =
                            when (notesUiState.noteType) {
                                NoteType.ALL.value -> stringResource(id = R.string.empty_notes_all)
                                else -> stringResource(
                                    id = R.string.empty_notes,
                                    if (notesUiState.noteType == NoteType.TRASH.value) "Recently Deleted"
                                    else notesUiState.noteType
                                )
                            }
                        Text(text = emptyNoteMsg)
                    }
                }

                AnimatedVisibility(
                    visible = notesUiState.notes.isNotEmpty(),
                    enter = slideInVertically(
                        initialOffsetY = { it / 4 },
                        animationSpec = tween()
                    ) + fadeIn(),
                    exit = slideOutVertically(animationSpec = tween()) + fadeOut()
                ) {
                    NoteCards(
                        notes = notesUiState.notes,
                        listState = notesUiState.listState,
                        navigateToNoteDetail = navigateToNoteDetail
                    ) { noteId, navigateToNoteDetail ->
                        viewModel.onClickNote(
                            noteId, navigateToNoteDetail
                        )
                    }
                }
            }
        }

        if (notesUiState.noteType != NoteType.TRASH.value) {
            Button(
                onClick = { viewModel.onCreateNoteButtonClick(navigateToNoteDetail = navigateToNoteDetail) },
                modifier = Modifier
                    .size(52.dp)
                    .align(Alignment.BottomCenter),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    if (notesUiState.noteType == NoteType.TODO.value) Icons.Filled.Check
                    else Icons.Filled.Add,
                    modifier = Modifier.size(32.dp),
                    contentDescription = stringResource(id = R.string.create_note_button_description),
                    tint = Color.White
                )
            }
        }

        if (notesUiState.showDialog) {
            AlertDialog(title = stringResource(id = R.string.delete_note_dialog_title),
                text = stringResource(id = R.string.delete_note_dialog_text),
                confirmButtonText = stringResource(id = R.string.delete_note_label),
                dismissButtonText = stringResource(id = R.string.cancel_label),
                onDismiss = { viewModel.closeDialog() },
                confirmButtonClick = { viewModel.emptyTrash() })
        }
    }
}

@Composable
fun SearchBar(
    search: String,
    isVisible: Boolean,
    onChange: (String) -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        OutlinedTextField(
            value = search,
            onValueChange = { onChange(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = stringResource(id = R.string.searchbar_placeholder)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(id = R.string.search_icon)
                )
            },
            shape = CircleShape,
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = if (isSystemInDarkTheme()) SearchBarColorDark else SearchBarColorLight,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

