package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = AmberPrimary,
    onPrimary = Color(0xFF1E1000),
    primaryContainer = AmberContainer,
    onPrimaryContainer = OnAmberContainer,
    secondary = CyberCyan,
    onSecondary = Color(0xFF001F24),
    secondaryContainer = CyberCyanContainer,
    onSecondaryContainer = Color(0xFFB8F5FF),
    tertiary = VioletAccent,
    onTertiary = Color(0xFF23005A),
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = DarkBorder,
    outlineVariant = Color(0xFF1E2638),
    error = CrimsonAlert,
    onError = Color.White
)

@Composable
fun MohoragaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
