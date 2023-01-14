package com.cleverlycode.notesly.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.cleverlycode.notesly.R
import com.cleverlycode.notesly.ui.screens.notes.NoteType

@Composable
fun NoteTypesChips(
    noteTypes: List<NoteType>,
    selectedNoteType: String,
    onSelectionChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        noteTypes.forEach { noteType ->
            Chip(
                label = noteType.value,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
                isSelected = noteType.value == selectedNoteType,
                onSelectionChanged = onSelectionChanged
            )
        }
    }
}