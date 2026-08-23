package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.data.model.AppColorTheme
import com.example.data.model.NightModeOption

@Immutable
data class ExtendedThemeColors(
    val theme: AppColorTheme,
    val isNightMode: Boolean,
    val isOledBlack: Boolean,
    val primaryHeaderGradient: Brush,
    val ayahHighlightBackground: Color,
    val ayahBorderColor: Color,
    val nightTextContrastHigh: Boolean = true,
    val goldAccent: Color = QuranGold
)

val LocalExtendedTheme = staticCompositionLocalOf {
    ExtendedThemeColors(
        theme = AppColorTheme.EMERALD,
        isNightMode = false,
        isOledBlack = false,
        primaryHeaderGradient = Brush.linearGradient(listOf(EmeraldPrimaryDark, EmeraldPrimaryLight)),
        ayahHighlightBackground = EmeraldPrimaryContainerLight.copy(alpha = 0.45f),
        ayahBorderColor = EmeraldPrimaryLight.copy(alpha = 0.25f)
    )
}

fun getThemeColorScheme(
    theme: AppColorTheme,
    nightModeOption: NightModeOption,
    isSystemDark: Boolean
): ColorScheme {
    val isDark = when (nightModeOption) {
        NightModeOption.LIGHT -> false
        NightModeOption.NIGHT -> true
        NightModeOption.OLED_BLACK -> true
        NightModeOption.SYSTEM -> isSystemDark
    }

    val isOled = nightModeOption == NightModeOption.OLED_BLACK

    return when (theme) {
        AppColorTheme.EMERALD -> {
            if (isDark) {
                val bg = if (isOled) OledPureBlack else EmeraldBackgroundNight
                val surf = if (isOled) OledBlackSurface else EmeraldSurfaceNight
                val surfVar = if (isOled) OledBlackSurfaceVariant else EmeraldSurfaceVariantNight
                darkColorScheme(
                    primary = EmeraldPrimaryNight,
                    onPrimary = Color.White,
                    primaryContainer = EmeraldPrimaryContainerNight,
                    onPrimaryContainer = EmeraldPrimaryContainerLight,
                    secondary = QuranGoldLight,
                    onSecondary = bg,
                    background = bg,
                    onBackground = EmeraldTextPrimaryNight,
                    surface = surf,
                    onSurface = EmeraldTextPrimaryNight,
                    surfaceVariant = surfVar,
                    onSurfaceVariant = EmeraldTextSecondaryNight,
                    outline = EmeraldSurfaceVariantNight
                )
            } else {
                lightColorScheme(
                    primary = EmeraldPrimaryLight,
                    onPrimary = Color.White,
                    primaryContainer = EmeraldPrimaryContainerLight,
                    onPrimaryContainer = EmeraldOnPrimaryContainerLight,
                    secondary = QuranGold,
                    onSecondary = Color.White,
                    background = EmeraldBackgroundLight,
                    onBackground = EmeraldTextPrimaryLight,
                    surface = EmeraldSurfaceLight,
                    onSurface = EmeraldTextPrimaryLight,
                    surfaceVariant = EmeraldSurfaceVariantLight,
                    onSurfaceVariant = EmeraldTextSecondaryLight,
                    outline = Color(0xFFE0ECE6)
                )
            }
        }

        AppColorTheme.SAPPHIRE -> {
            if (isDark) {
                val bg = if (isOled) OledPureBlack else SapphireBackgroundNight
                val surf = if (isOled) OledBlackSurface else SapphireSurfaceNight
                val surfVar = if (isOled) OledBlackSurfaceVariant else SapphireSurfaceVariantNight
                darkColorScheme(
                    primary = SapphirePrimaryNight,
                    onPrimary = Color.White,
                    primaryContainer = SapphirePrimaryContainerNight,
                    onPrimaryContainer = SapphirePrimaryContainerLight,
                    secondary = Color(0xFF60A5FA),
                    onSecondary = bg,
                    background = bg,
                    onBackground = SapphireTextPrimaryNight,
                    surface = surf,
                    onSurface = SapphireTextPrimaryNight,
                    surfaceVariant = surfVar,
                    onSurfaceVariant = SapphireTextSecondaryNight,
                    outline = SapphireSurfaceVariantNight
                )
            } else {
                lightColorScheme(
                    primary = SapphirePrimaryLight,
                    onPrimary = Color.White,
                    primaryContainer = SapphirePrimaryContainerLight,
                    onPrimaryContainer = SapphireOnPrimaryContainerLight,
                    secondary = Color(0xFF2563EB),
                    onSecondary = Color.White,
                    background = SapphireBackgroundLight,
                    onBackground = SapphireTextPrimaryLight,
                    surface = SapphireSurfaceLight,
                    onSurface = SapphireTextPrimaryLight,
                    surfaceVariant = SapphireSurfaceVariantLight,
                    onSurfaceVariant = SapphireTextSecondaryLight,
                    outline = Color(0xFFD6E4F0)
                )
            }
        }

        AppColorTheme.AMBER_DESERT -> {
            if (isDark) {
                val bg = if (isOled) OledPureBlack else AmberBackgroundNight
                val surf = if (isOled) OledBlackSurface else AmberSurfaceNight
                val surfVar = if (isOled) OledBlackSurfaceVariant else AmberSurfaceVariantNight
                darkColorScheme(
                    primary = AmberPrimaryNight,
                    onPrimary = Color.White,
                    primaryContainer = AmberPrimaryContainerNight,
                    onPrimaryContainer = AmberPrimaryContainerLight,
                    secondary = Color(0xFFFBBF24),
                    onSecondary = bg,
                    background = bg,
                    onBackground = AmberTextPrimaryNight,
                    surface = surf,
                    onSurface = AmberTextPrimaryNight,
                    surfaceVariant = surfVar,
                    onSurfaceVariant = AmberTextSecondaryNight,
                    outline = AmberSurfaceVariantNight
                )
            } else {
                lightColorScheme(
                    primary = AmberPrimaryLight,
                    onPrimary = Color.White,
                    primaryContainer = AmberPrimaryContainerLight,
                    onPrimaryContainer = AmberOnPrimaryContainerLight,
                    secondary = Color(0xFFD97706),
                    onSecondary = Color.White,
                    background = AmberBackgroundLight,
                    onBackground = AmberTextPrimaryLight,
                    surface = AmberSurfaceLight,
                    onSurface = AmberTextPrimaryLight,
                    surfaceVariant = AmberSurfaceVariantLight,
                    onSurfaceVariant = AmberTextSecondaryLight,
                    outline = Color(0xFFEDE0D4)
                )
            }
        }

        AppColorTheme.AMETHYST -> {
            if (isDark) {
                val bg = if (isOled) OledPureBlack else AmethystBackgroundNight
                val surf = if (isOled) OledBlackSurface else AmethystSurfaceNight
                val surfVar = if (isOled) OledBlackSurfaceVariant else AmethystSurfaceVariantNight
                darkColorScheme(
                    primary = AmethystPrimaryNight,
                    onPrimary = Color.White,
                    primaryContainer = AmethystPrimaryContainerNight,
                    onPrimaryContainer = AmethystPrimaryContainerLight,
                    secondary = Color(0xFFD8B4FE),
                    onSecondary = bg,
                    background = bg,
                    onBackground = AmethystTextPrimaryNight,
                    surface = surf,
                    onSurface = AmethystTextPrimaryNight,
                    surfaceVariant = surfVar,
                    onSurfaceVariant = AmethystTextSecondaryNight,
                    outline = AmethystSurfaceVariantNight
                )
            } else {
                lightColorScheme(
                    primary = AmethystPrimaryLight,
                    onPrimary = Color.White,
                    primaryContainer = AmethystPrimaryContainerLight,
                    onPrimaryContainer = AmethystOnPrimaryContainerLight,
                    secondary = Color(0xFF9333EA),
                    onSecondary = Color.White,
                    background = AmethystBackgroundLight,
                    onBackground = AmethystTextPrimaryLight,
                    surface = AmethystSurfaceLight,
                    onSurface = AmethystTextPrimaryLight,
                    surfaceVariant = AmethystSurfaceVariantLight,
                    onSurfaceVariant = AmethystTextSecondaryLight,
                    outline = Color(0xFFEADBEE)
                )
            }
        }

        AppColorTheme.SEPIA_MUSHAF -> {
            if (isDark) {
                val bg = if (isOled) OledPureBlack else SepiaBackgroundNight
                val surf = if (isOled) OledBlackSurface else SepiaSurfaceNight
                val surfVar = if (isOled) OledBlackSurfaceVariant else SepiaSurfaceVariantNight
                darkColorScheme(
                    primary = SepiaPrimaryNight,
                    onPrimary = Color.White,
                    primaryContainer = SepiaPrimaryContainerNight,
                    onPrimaryContainer = SepiaPrimaryContainerLight,
                    secondary = QuranGoldLight,
                    onSecondary = bg,
                    background = bg,
                    onBackground = SepiaTextPrimaryNight,
                    surface = surf,
                    onSurface = SepiaTextPrimaryNight,
                    surfaceVariant = surfVar,
                    onSurfaceVariant = SepiaTextSecondaryNight,
                    outline = SepiaSurfaceVariantNight
                )
            } else {
                lightColorScheme(
                    primary = SepiaPrimaryLight,
                    onPrimary = Color.White,
                    primaryContainer = SepiaPrimaryContainerLight,
                    onPrimaryContainer = SepiaOnPrimaryContainerLight,
                    secondary = QuranGoldDark,
                    onSecondary = Color.White,
                    background = SepiaBackgroundLight,
                    onBackground = SepiaTextPrimaryLight,
                    surface = SepiaSurfaceLight,
                    onSurface = SepiaTextPrimaryLight,
                    surfaceVariant = SepiaSurfaceVariantLight,
                    onSurfaceVariant = SepiaTextSecondaryLight,
                    outline = Color(0xFFE2D6C5)
                )
            }
        }
    }
}

@Composable
fun AlQuranEditorTheme(
    theme: AppColorTheme = AppColorTheme.EMERALD,
    nightModeOption: NightModeOption = NightModeOption.LIGHT,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val isSystemDark = isSystemInDarkTheme()
    val isDark = when (nightModeOption) {
        NightModeOption.LIGHT -> false
        NightModeOption.NIGHT -> true
        NightModeOption.OLED_BLACK -> true
        NightModeOption.SYSTEM -> isSystemDark
    }
    val isOled = nightModeOption == NightModeOption.OLED_BLACK

    val colorScheme = if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val context = LocalContext.current
        if (isDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        getThemeColorScheme(theme, nightModeOption, isSystemDark)
    }

    val headerGradient = when (theme) {
        AppColorTheme.EMERALD -> {
            if (isDark) Brush.linearGradient(listOf(Color(0xFF0F261C), EmeraldPrimaryNight.copy(alpha = 0.8f)))
            else Brush.linearGradient(listOf(EmeraldPrimaryDark, EmeraldPrimaryLight))
        }
        AppColorTheme.SAPPHIRE -> {
            if (isDark) Brush.linearGradient(listOf(Color(0xFF0B2034), SapphirePrimaryNight.copy(alpha = 0.8f)))
            else Brush.linearGradient(listOf(SapphirePrimaryDark, SapphirePrimaryLight))
        }
        AppColorTheme.AMBER_DESERT -> {
            if (isDark) Brush.linearGradient(listOf(Color(0xFF2E170A), AmberPrimaryNight.copy(alpha = 0.8f)))
            else Brush.linearGradient(listOf(AmberPrimaryDark, AmberPrimaryLight))
        }
        AppColorTheme.AMETHYST -> {
            if (isDark) Brush.linearGradient(listOf(Color(0xFF281130), AmethystPrimaryNight.copy(alpha = 0.8f)))
            else Brush.linearGradient(listOf(AmethystPrimaryDark, AmethystPrimaryLight))
        }
        AppColorTheme.SEPIA_MUSHAF -> {
            if (isDark) Brush.linearGradient(listOf(Color(0xFF231B14), SepiaPrimaryNight.copy(alpha = 0.8f)))
            else Brush.linearGradient(listOf(SepiaPrimaryDark, SepiaPrimaryLight))
        }
    }

    val highlightBg = colorScheme.primaryContainer.copy(alpha = if (isDark) 0.35f else 0.45f)
    val borderCol = colorScheme.primary.copy(alpha = if (isDark) 0.35f else 0.25f)

    val extendedTheme = ExtendedThemeColors(
        theme = theme,
        isNightMode = isDark,
        isOledBlack = isOled,
        primaryHeaderGradient = headerGradient,
        ayahHighlightBackground = highlightBg,
        ayahBorderColor = borderCol
    )

    CompositionLocalProvider(LocalExtendedTheme provides extendedTheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

@Composable
fun MyApplicationTheme(
    theme: AppColorTheme = AppColorTheme.EMERALD,
    nightModeOption: NightModeOption = NightModeOption.LIGHT,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    AlQuranEditorTheme(theme, nightModeOption, dynamicColor, content)
}
