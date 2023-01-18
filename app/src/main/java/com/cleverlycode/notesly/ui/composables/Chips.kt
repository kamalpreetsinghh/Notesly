package com.cleverlycode.notesly.ui.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.cleverlycode.notesly.ui.theme.AppTheme

@Composable
fun Chips(
    items: List<String>,
    selectedItem: String,
    onSelectionChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items(items) { item ->
            Chip(
                text = item,
                modifier = Modifier,
                isSelected = item == selectedItem,
                onSelectionChanged = onSelectionChanged
            )
        }
    }
}

@Composable
fun Chip(
    text: String,
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
        shape = CircleShape,
        color = color,
        contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.background
    ) {
        Row(
            modifier = Modifier.toggleable(
                value = isSelected,
                onValueChange = { onSelectionChanged(text) }
            )
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(
                    vertical = AppTheme.dimens.vertical_padding_chip,
                    horizontal = AppTheme.dimens.horizontal_padding_chip
                ),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}