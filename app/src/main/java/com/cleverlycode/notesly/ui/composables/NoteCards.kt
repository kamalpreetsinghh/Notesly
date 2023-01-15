package com.cleverlycode.notesly.ui.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cleverlycode.notesly.domain.model.Note

@Composable
fun NoteCards(
    notes: List<Note>,
    listState: LazyListState,
    navigateToNoteDetail: (String, Long) -> Unit,
    onClick: (Long, (String, Long) -> Unit) -> Unit
) {
    LazyColumn(modifier = Modifier.animateContentSize(), state = listState) {
        items(
            items = notes,
            key = { notes: Note -> notes.id }) { note ->
            NoteCard(
                note = note,
                navigateToNoteDetail = navigateToNoteDetail,
                onClick = onClick,
                modifier = Modifier.animateItemPlacement(
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing,
                    )
                )
            )
        }
    }
}