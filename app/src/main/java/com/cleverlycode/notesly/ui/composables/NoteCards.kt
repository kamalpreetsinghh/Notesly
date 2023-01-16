package com.cleverlycode.notesly.ui.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.cleverlycode.notesly.R
import com.cleverlycode.notesly.domain.model.Note

@Composable
fun NoteCards(
    notes: List<Note>,
    listState: LazyGridState,
    isGridView: Boolean,
    navigateToNoteDetail: (String, Long) -> Unit,
    onClick: (Long, (String, Long) -> Unit) -> Unit
) {
    val columns by animateIntAsState(
        targetValue = if (isGridView) 2 else 1,
        animationSpec = tween()
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = Modifier.animateContentSize(),
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.horizontal_margin)),
    ) {
        items(notes) { note ->
            NoteCard(
                note = note,
                isGridView = isGridView,
                navigateToNoteDetail = navigateToNoteDetail,
                onClick = onClick,
                modifier = Modifier
            )
        }
    }
}