package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = TerracottaPrimary,
    onPrimary = Color.White,
    primaryContainer = AlabasterGlaze,
    onPrimaryContainer = TerracottaDeep,
    secondary = ApricotLabel,
    onSecondary = Color.White,
    secondaryContainer = ApricotContainer,
    onSecondaryContainer = Color(0xFF693300),
    tertiary = TertiaryBronze,
    onTertiary = Color.White,
    tertiaryContainer = TertiaryAlabaster,
    onTertiaryContainer = Color(0xFF231A0C),
    background = EcruCanvas,
    onBackground = SmokedWalnut,
    surface = EcruCanvas,
    onSurface = SmokedWalnut,
    surfaceVariant = LinenElevation,
    onSurfaceVariant = EarthVariant,
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFFBF2F0),
    surfaceContainer = LinenElevation,
    surfaceContainerHigh = SurfaceContainerHigh,
    surfaceContainerHighest = Color(0xFFE9E1DF),
    outline = OutlineClay,
    outlineVariant = BoneHairline,
    error = ErrorClay,
    onError = Color.White,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight,
)

private val DarkColorScheme = darkColorScheme(
    primary = ApricotSecondary,
    onPrimary = Color(0xFF370E00),
    primaryContainer = Color(0xFF7F2B00),
    onPrimaryContainer = AlabasterGlaze,
    secondary = DarkTerracottaGlow,
    onSecondary = Color(0xFF301400),
    secondaryContainer = Color(0xFF5D2D00),
    onSecondaryContainer = Color(0xFFFFDCC5),
    tertiary = Color(0xFFD5C4AD),
    onTertiary = Color(0xFF231A0C),
    background = DarkWalnutCanvas,
    onBackground = DarkBoneText,
    surface = DarkWalnutCanvas,
    onSurface = DarkBoneText,
    surfaceVariant = DarkWalnutSurface,
    onSurfaceVariant = DarkMutedSand,
    surfaceContainerLowest = Color(0xFF14110F),
    surfaceContainerLow = DarkWalnutSurface,
    surfaceContainer = DarkWalnutElevated,
    surfaceContainerHigh = Color(0xFF362F2B),
    surfaceContainerHighest = Color(0xFF413934),
    outline = DarkMutedSand,
    outlineVariant = DarkHairline,
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
)

@Composable
fun TerraneTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val terraneColors = if (darkTheme) DarkTerraneColors else LightTerraneColors

    CompositionLocalProvider(LocalTerraneColors provides terraneColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = TerraneTypography,
            shapes = TerraneShapes,
            content = content,
        )
    }
}

object TerraneThemeExtras {
    val colors: TerraneColors
        @Composable get() = LocalTerraneColors.current
}
