package com.cleverlycode.notesly.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.cleverlycode.notesly.R
import com.cleverlycode.notesly.domain.model.Note
import com.cleverlycode.notesly.ui.screens.notes.NoteType
import com.cleverlycode.notesly.ui.theme.AppTheme
import com.cleverlycode.notesly.ui.theme.GreenCard
import com.cleverlycode.notesly.ui.theme.RedCard
import com.cleverlycode.notesly.ui.theme.YellowCard
import java.text.DateFormat
import java.text.SimpleDateFormat

@Composable
fun NoteCards(
    notes: List<Note>,
    listState: LazyStaggeredGridState,
    isGridLayout: Boolean,
    onScroll: (Float, Float) -> Unit,
    navigateToNoteDetail: (String, Long) -> Unit,
    onClick: (Long, (String, Long) -> Unit) -> Unit
) {
    val columns by animateIntAsState(
        targetValue = if (isGridLayout) 2 else 1,
        animationSpec = tween()
    )

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                onScroll(consumed.y, available.y)
                return Offset.Zero
            }
        }
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(columns),
        modifier = Modifier
            .animateContentSize()
            .nestedScroll(connection = nestedScrollConnection),
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(space = AppTheme.dimens.margin),
    ) {
        items(notes) { note ->
            NoteCard(
                note = note,
                isGridLayout = isGridLayout,
                navigateToNoteDetail = navigateToNoteDetail,
                onClick = onClick,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun NoteCard(
    note: Note,
    isGridLayout: Boolean,
    navigateToNoteDetail: (String, Long) -> Unit,
    onClick: (Long, (String, Long) -> Unit) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = mapOf(
        NoteType.ALL.value to RedCard,
        NoteType.STARRED.value to YellowCard,
        NoteType.TODO.value to GreenCard
    )
    val noteType = note.noteType

    Card(
        onClick = { onClick(note.id, navigateToNoteDetail) },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = AppTheme.dimens.vertical_margin),
        shape = RoundedCornerShape(size = AppTheme.dimens.rounded_card_note),
        colors = CardDefaults.cardColors(containerColor = colors[noteType] ?: RedCard)
    ) {
        Column(
            modifier = Modifier.padding
                (
                vertical = if (isGridLayout) AppTheme.dimens.vertical_margin_large
                else AppTheme.dimens.vertical_margin,
                horizontal = AppTheme.dimens.horizontal_margin
            )
        ) {
            if (note.title.isNotBlank()) {
                Row(
                    modifier = Modifier
                        .padding(vertical = AppTheme.dimens.vertical_margin)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = note.title,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        maxLines = if (isGridLayout) 2 else 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (isGridLayout) {
                Row(
                    modifier = Modifier
                        .padding(vertical = AppTheme.dimens.vertical_margin)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = SimpleDateFormat
                            .getDateInstance(DateFormat.SHORT)
                            .format(note.dateUpdated),
                        color = Color.DarkGray,
                        fontSize = 16.sp
                    )
                }

                if (noteType != NoteType.TODO.value && note.description.isNotBlank()) {
                    Row(
                        modifier = Modifier
                            .padding(vertical = AppTheme.dimens.vertical_margin)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = note.description,
                            color = Color.DarkGray,
                            fontSize = 16.sp,
                            maxLines = 4,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                } else if (noteType == NoteType.TODO.value && note.tasks.isNotEmpty()) {
                    note.tasks.forEachIndexed { index, task ->
                        if (index < 2) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = AppTheme.dimens.small_vertical_margin),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(
                                        id = if (task.isDone) R.drawable.ic_checkbox
                                        else R.drawable.ic_check_box_outline
                                    ),
                                    tint = Color.Black,
                                    contentDescription = null
                                )
                                Text(
                                    text = task.name,
                                    modifier = Modifier.padding(start = AppTheme.dimens.small_margin),
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(visible = !isGridLayout) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = AppTheme.dimens.vertical_margin),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = SimpleDateFormat.getDateInstance(DateFormat.SHORT)
                            .format(note.dateUpdated),
                        color = Color.DarkGray,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.width(width = AppTheme.dimens.spacer))
                    if (noteType != NoteType.TODO.value && note.description.isNotBlank()) {
                        Text(
                            text = note.description,
                            color = Color.DarkGray,
                            fontSize = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    } else if (noteType == NoteType.TODO.value && note.tasks.isNotEmpty()) {
                        Icon(
                            painter = painterResource(
                                id = if (note.tasks[0].isDone) R.drawable.ic_checkbox
                                else R.drawable.ic_check_box_outline
                            ),
                            tint = Color.Black,
                            contentDescription = null
                        )

                        Text(
                            text = note.tasks[0].name,
                            modifier = Modifier.padding(start = AppTheme.dimens.small_margin),
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}