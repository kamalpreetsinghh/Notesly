package com.cleverlycode.notesly.ui.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.cleverlycode.notesly.domain.model.Note
import com.cleverlycode.notesly.ui.theme.AppTheme

@Composable
fun NoteCards(
    notes: List<Note>,
    listState: LazyStaggeredGridState,
    isGridView: Boolean,
    navigateToNoteDetail: (String, Long) -> Unit,
    onClick: (Long, (String, Long) -> Unit) -> Unit
) {
    val columns by animateIntAsState(
        targetValue = if (isGridView) 2 else 1,
        animationSpec = tween()
    )

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(columns),
        modifier = Modifier.animateContentSize(),
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(space = AppTheme.dimens.margin),
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