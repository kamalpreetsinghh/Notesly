package com.cleverlycode.notesly.ui.screens.notes

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cleverlycode.notesly.R
import com.cleverlycode.notesly.ui.composables.*
import com.cleverlycode.notesly.ui.theme.AppTheme

@Composable
fun NotesScreen(
    snackbarHostState: SnackbarHostState,
    navigateToNoteDetail: (String, Long) -> Unit,
    showSnackbar: (String, SnackbarDuration) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val notesUiState by viewModel.notesUiState
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = AppTheme.dimens.horizontal_margin,
                vertical = AppTheme.dimens.vertical_margin
            )
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }
            .windowInsetsPadding(WindowInsets.navigationBars),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = AppTheme.dimens.vertical_margin_extra_large,
                        bottom = AppTheme.dimens.vertical_margin
                    ),
                horizontalArrangement = Arrangement.Center
            ) {
                stringResource(id = R.string.my_label).forEach { char ->
                    TextWithShape(
                        char = char,
                        shape = CircleShape,
                        textStyle = MaterialTheme.typography.displayMedium,
                        shapeSize = 44.dp,
                        color = MaterialTheme.colorScheme.primary,
                        textColor = Color.White,
                        modifier = Modifier.padding(horizontal = AppTheme.dimens.extra_small_margin)
                    )
                }
                stringResource(id = R.string.notes_label).forEach { char ->
                    TextWithShape(
                        char = char,
                        shape = CircleShape,
                        textStyle = MaterialTheme.typography.displayMedium,
                        shapeSize = 44.dp,
                        color = MaterialTheme.colorScheme.surface,
                        textColor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = AppTheme.dimens.extra_small_margin)
                    )
                }
            }
        },
        bottomBar = {
            val selectedChip = notesUiState.selectedChip
            AnimatedVisibility(
                visible = selectedChip != NoteType.TRASH.value,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppTheme.dimens.small_margin),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { viewModel.onCreateNoteButtonClick(navigateToNoteDetail = navigateToNoteDetail) },
                        modifier = Modifier.size(52.dp),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            if (selectedChip == NoteType.TODO.value) Icons.Filled.Check
                            else Icons.Filled.Add,
                            modifier = Modifier.size(32.dp),
                            contentDescription = stringResource(id = R.string.create_note_button_description),
                            tint = Color.White
                        )
                    }
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { contentPadding ->
        Column(
            modifier = Modifier.padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val selectedChip = notesUiState.selectedChip
            val notes = notesUiState.notes
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AppTheme.dimens.vertical_margin),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Chips(
                    items = getNoteTypes(),
                    selectedItem = selectedChip,
                    onSelectionChanged = { noteType -> viewModel.onClickNoteTypeChip(noteType) },
                    modifier = Modifier.fillMaxWidth(0.93f)
                )

                Row {
                    IconButton(
                        onClick = { viewModel.openMenu() },
                        enabled = !notesUiState.isMenuExpanded
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = stringResource(id = R.string.menu_note_description)
                        )
                    }
                    NotesMenu(
                        expanded = notesUiState.isMenuExpanded,
                        noteType = selectedChip,
                        isGridLayout = notesUiState.isGridLayout,
                        isNotesEmpty = notes.isEmpty(),
                        onDismissRequest = { viewModel.closeMenu() },
                        moveToTrash = { viewModel.deleteNotes() },
                        openDialog = { viewModel.openDialog() },
                        changeNotesLayout = { viewModel.changeNotesLayout() },
                        showSnackbar = showSnackbar
                    )
                }
            }

            if (notesUiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                AnimatedVisibility(
                    visible = notes.isNotEmpty() || notesUiState.search.isNotEmpty(),
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    SearchBar(
                        search = notesUiState.search,
                        onChange = { newValue -> viewModel.onSearchTextChange(newValue) }
                    )
                }

                AnimatedVisibility(
                    visible = selectedChip == NoteType.TRASH.value &&
                            (notes.isNotEmpty() || notesUiState.search.isNotEmpty())
                ) {
                    Text(
                        text = stringResource(id = R.string.trash_notes_message),
                        modifier = Modifier.padding(vertical = AppTheme.dimens.vertical_margin),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                AnimatedVisibility(
                    visible = notes.isNotEmpty(),
                    enter = slideInVertically(
                        initialOffsetY = { it / 4 },
                        animationSpec = tween(durationMillis = 100)
                    ) + fadeIn(),
                    exit = slideOutVertically(animationSpec = tween()) + fadeOut()
                ) {
                    NoteCards(
                        notes = notes,
                        listState = notesUiState.listState,
                        isGridLayout = notesUiState.isGridLayout,
                        navigateToNoteDetail = navigateToNoteDetail
                    ) { noteId, navigateToNoteDetail ->
                        viewModel.onClickNote(
                            noteId, navigateToNoteDetail
                        )
                    }
                }

                AnimatedVisibility(
                    visible = notes.isEmpty(),
                    enter = fadeIn(animationSpec = tween(durationMillis = 100)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 100))
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val emptyNoteMsg =
                            when (selectedChip) {
                                NoteType.ALL.value -> stringResource(id = R.string.empty_notes_all)
                                else -> stringResource(
                                    id = R.string.empty_notes,
                                    if (selectedChip == NoteType.TRASH.value) "Recently Deleted"
                                    else selectedChip
                                )
                            }
                        Text(text = emptyNoteMsg)
                    }
                }
            }
        }

        if (notesUiState.showDialog) {
            AlertDialog(
                title = stringResource(id = R.string.delete_note_dialog_title),
                text = stringResource(id = R.string.delete_note_dialog_text),
                confirmButtonText = stringResource(id = R.string.delete_note_label),
                dismissButtonText = stringResource(id = R.string.cancel_label),
                onDismiss = { viewModel.closeDialog() },
                confirmButtonClick = { viewModel.emptyTrash() },
                showSnackbar = showSnackbar,
                snackbarMessage = stringResource(id = R.string.all_moved_to_trash_snackbar_message)
            )
        }
    }
}

@Composable
fun SearchBar(
    search: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = search,
        onValueChange = { onChange(it) },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = AppTheme.dimens.vertical_margin),
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
            containerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

