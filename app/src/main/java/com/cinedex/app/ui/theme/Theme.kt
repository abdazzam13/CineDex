package com.cinedex.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val CinematicDarkColorScheme = darkColorScheme(
    primary = ColorPrimary,
    onPrimary = ColorOnPrimary,
    primaryContainer = ColorPrimaryContainer,
    onPrimaryContainer = ColorOnPrimaryContainer,
    inversePrimary = ColorInversePrimary,
    secondary = ColorSecondary,
    onSecondary = ColorOnSecondary,
    secondaryContainer = ColorSecondaryContainer,
    onSecondaryContainer = ColorOnSecondaryContainer,
    tertiary = ColorTertiary,
    onTertiary = ColorOnTertiary,
    tertiaryContainer = ColorTertiaryContainer,
    onTertiaryContainer = ColorOnTertiaryContainer,
    error = ColorError,
    onError = ColorOnError,
    errorContainer = ColorErrorContainer,
    onErrorContainer = ColorOnErrorContainer,
    background = ColorBackground,
    onBackground = ColorOnBackground,
    surface = ColorSurface,
    onSurface = ColorOnSurface,
    surfaceVariant = ColorSurfaceVariant,
    onSurfaceVariant = ColorOnSurfaceVariant,
    surfaceTint = ColorSurfaceTint,
    inverseSurface = ColorInverseSurface,
    inverseOnSurface = ColorInverseOnSurface,
    outline = ColorOutline,
    outlineVariant = ColorOutlineVariant,
    surfaceDim = ColorSurfaceDim,
    surfaceBright = ColorSurfaceBright,
    surfaceContainerLowest = ColorSurfaceContainerLowest,
    surfaceContainerLow = ColorSurfaceContainerLow,
    surfaceContainer = ColorSurfaceContainer,
    surfaceContainerHigh = ColorSurfaceContainerHigh,
    surfaceContainerHighest = ColorSurfaceContainerHighest,
)

@Composable
fun CineDexTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CinematicDarkColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
