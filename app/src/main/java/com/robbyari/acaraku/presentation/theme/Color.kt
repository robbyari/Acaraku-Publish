package com.robbyari.acaraku.presentation.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val ButtonBackground = Color(0xFF5E3D35)
val ButtonColor = Color(0xFFEB565B)
val Orange = Color(0xFFEF8833)
val Green = Color(0xFF2FA409)

val GradientTop = Color(0xFF4F1911)
val GradientBot = Color(0xFF120A07)


val gradient45 = Brush.verticalGradient(
    colors = listOf(GradientTop, GradientBot),
)

val gradient50 = Brush.verticalGradient(
    colors = listOf(GradientBot, Color.Transparent),
    startY = 0f,
    endY = 1100f
)