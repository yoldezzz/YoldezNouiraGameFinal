package com.example.yoldeznouiraproject.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = MediterraneanSeaBlue,
    secondary = MediterraneanTerracotta,
    tertiary = MediterraneanOliveGreen,
    background = MediterraneanWarmCream,
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFF5E6D3),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = MediterraneanDarkSeaBlue,
    onSurface = MediterraneanDarkSeaBlue,
    onSurfaceVariant = MediterraneanSeaBlue,
    error = IncorrectAnswerRed,
    errorContainer = IncorrectAnswerRed.copy(alpha = 0.1f)
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = AppTypography,
        content = content
    )
}
