package com.cleverlycode.notesly.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class Dimensions(
    val margin: Dp = 16.dp,
    val small_margin: Dp = 8.dp,
    val extra_small_margin: Dp = 2.dp,
    val small_vertical_margin: Dp = 4.dp,
    val horizontal_margin: Dp = 16.dp,
    val vertical_margin: Dp = 8.dp,
    val vertical_margin_large: Dp = 16.dp,
    val vertical_margin_extra_large: Dp = 32.dp,
    val spacer: Dp = 12.dp,
    val vertical_padding_chip: Dp = 14.dp,
    val horizontal_padding_chip: Dp = 14.dp,
    val width_alert: Dp = 280.dp,
    val rounded_card_note: Dp = 20.dp,
)

val phoneDimensions = Dimensions()

val tabletDimensions = Dimensions(
    extra_small_margin = 4.dp,
    horizontal_padding_chip = 44.dp,
    width_alert = 500.dp,
    horizontal_margin = 32.dp,
    vertical_margin_extra_large = 32.dp,
    vertical_margin_large = 28.dp,
    spacer = 16.dp
)