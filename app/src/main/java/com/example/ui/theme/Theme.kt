package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = IslamicEmeraldLight,
    onPrimary = Color.White,
    primaryContainer = IslamicEmeraldDark,
    onPrimaryContainer = IslamicEmeraldContainer,
    secondary = QuranGoldLight,
    onSecondary = DarkBackground,
    secondaryContainer = QuranGoldDark,
    onSecondaryContainer = QuranGoldLight,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkDivider
)

private val LightColorScheme = lightColorScheme(
    primary = IslamicEmeraldPrimary,
    onPrimary = Color.White,
    primaryContainer = IslamicEmeraldContainer,
    onPrimaryContainer = IslamicEmeraldOnContainer,
    secondary = QuranGold,
    onSecondary = Color.White,
    secondaryContainer = QuranGoldLight,
    onSecondaryContainer = QuranGoldDark,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightTextSecondary,
    outline = LightDivider
)

@Composable
fun AlQuranEditorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent Quran green identity
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    AlQuranEditorTheme(darkTheme, dynamicColor, content)
}
