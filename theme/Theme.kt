package com.personalfitnessos.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/** Matches the user's persisted theme preference (spec section 33). */
enum class ThemeMode { SYSTEM, LIGHT, DARK }

private val AppDarkColors = darkColorScheme(
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    onBackground = DarkOnBackground,
    onSurface = DarkOnBackground,
    onSurfaceVariant = DarkOnSurfaceVariant,
    primary = AccentCyan,
    onPrimary = DarkBackground,
    secondary = AccentAmber,
    onSecondary = DarkBackground,
    error = AccentRed,
    tertiary = AccentGreen,
)

private val AppLightColors = lightColorScheme(
    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = LightSurfaceVariant,
    onBackground = LightOnBackground,
    onSurface = LightOnBackground,
    onSurfaceVariant = LightOnSurfaceVariant,
    primary = AccentCyanDim,
    onPrimary = LightSurface,
    secondary = AccentAmber,
    onSecondary = LightOnBackground,
    error = AccentRed,
    tertiary = AccentGreen,
)

@Composable
fun PersonalFitnessTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    content: @Composable () -> Unit,
) {
    val useDark = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
    MaterialTheme(
        colorScheme = if (useDark) AppDarkColors else AppLightColors,
        typography = AppTypography,
        content = content,
    )
}
