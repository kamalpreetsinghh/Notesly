package com.cleverlycode.notesly.ui.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import com.cleverlycode.notesly.R

@Composable
fun Chip(
    label: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onSelectionChanged: (String) -> Unit = {}
) {
    val color by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onBackground,
        animationSpec = tween(durationMillis = 150)
    )

    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = color,
        contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.background
    ) {
        Row(modifier = Modifier
            .toggleable(
                value = isSelected,
                onValueChange = { onSelectionChanged(label) }
            )
        ) {
            Text(
                text = label,
                modifier = Modifier.padding(
                    vertical = dimensionResource(id = R.dimen.chip_padding_vertical),
                    horizontal = dimensionResource(id = R.dimen.chip_padding_horizontal)
                ),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}