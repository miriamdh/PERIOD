package com.g12.periodee.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(

    // 🌸 TITRE (Bienvenue)
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Cursive,
        fontSize = 32.sp
    ),

    headlineMedium = TextStyle(
        fontFamily = FontFamily.Cursive,
        fontSize = 26.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 16.sp
    ),

    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium
    )
)
