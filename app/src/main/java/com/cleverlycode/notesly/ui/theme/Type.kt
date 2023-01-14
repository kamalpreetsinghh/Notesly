package com.cleverlycode.notesly.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.cleverlycode.notesly.R

val Montserrat = FontFamily(
    Font(R.font.montserrat_light, FontWeight.Thin),
    Font(R.font.montserrat_normal, FontWeight.Normal),
    Font(R.font.montserrat_bold, FontWeight.Bold)
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    displayLarge = TextStyle(
        fontFamily = Montserrat,
        fontSize = 52.sp
    ),
    titleMedium = TextStyle(
        fontFamily = Montserrat
    ),
    headlineLarge = TextStyle(
        fontFamily = Montserrat,
        fontSize = 20.sp
    )
)