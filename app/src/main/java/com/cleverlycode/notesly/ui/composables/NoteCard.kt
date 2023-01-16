package com.cleverlycode.notesly.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cleverlycode.notesly.R
import com.cleverlycode.notesly.domain.model.Note
import com.cleverlycode.notesly.ui.screens.notes.NoteType
import com.cleverlycode.notesly.ui.theme.GreenCard
import com.cleverlycode.notesly.ui.theme.RedCard
import com.cleverlycode.notesly.ui.theme.YellowCard
import java.text.DateFormat
import java.text.SimpleDateFormat

@Composable
fun NoteCard(
    note: Note,
    isGridView: Boolean,
    navigateToNoteDetail: (String, Long) -> Unit,
    onClick: (Long, (String, Long) -> Unit) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = mapOf(
        NoteType.ALL.value to RedCard,
        NoteType.STARRED.value to YellowCard,
        NoteType.TODO.value to GreenCard
    )

    Card(
        onClick = { onClick(note.id, navigateToNoteDetail) },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.card_rounded_corner_note)),
        colors = CardDefaults.cardColors(containerColor = colors[note.noteType] ?: RedCard)
    ) {
        Column(
            modifier = Modifier.padding
                (
                vertical = dimensionResource(
                    id = if (isGridView) R.dimen.vertical_margin_notes
                    else R.dimen.vertical_margin
                ),
                horizontal = dimensionResource(id = R.dimen.horizontal_margin)
            )
        ) {
            if (note.title.isNotBlank()) {
                Row(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = note.title,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        maxLines = if (isGridView) 2 else 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (isGridView) {
                Row(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = SimpleDateFormat
                            .getDateInstance(DateFormat.SHORT)
                            .format(note.dateUpdated),
                        color = Color.DarkGray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Light
                    )
                }

                if (note.noteType != NoteType.TODO.value && note.description.isNotBlank()) {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = note.description,
                            color = Color.DarkGray,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Light,
                            maxLines = 4,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                } else if (note.noteType == NoteType.TODO.value && note.tasks.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = if (note.tasks.isNotEmpty()) note.tasks[0].name else "",
                            color = Color.DarkGray,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Light,
                            maxLines = 4,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            AnimatedVisibility(visible = !isGridView) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = SimpleDateFormat.getDateInstance(DateFormat.SHORT)
                            .format(note.dateUpdated),
                        color = Color.DarkGray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Light
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    if (note.description.isNotBlank() && note.noteType != NoteType.TODO.value) {
                        Text(
                            text = note.description,
                            color = Color.DarkGray,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Light,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}