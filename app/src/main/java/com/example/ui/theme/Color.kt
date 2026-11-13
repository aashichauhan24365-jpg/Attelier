package com.example.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Core Warm Architectural Living Editorial Tokens
val TerracottaPrimary = Color(0xFFEA580C)
val TerracottaHover = Color(0xFFC2410C)
val TerracottaDeep = Color(0xFFA33900)
val TerracottaContainer = Color(0xFFCC4900)

val ApricotSecondary = Color(0xFFFB923C)
val ApricotLabel = Color(0xFF944A00)
val ApricotContainer = Color(0xFFFD933D)

val AlabasterGlaze = Color(0xFFFFEDD5)
val WarmHoverTint = Color(0xFFFFF7ED)

val SmokedWalnut = Color(0xFF292524)
val CharcoalInk = Color(0xFF1E1B1A)
val EarthVariant = Color(0xFF5A4138)
val MutedStone = Color(0xFFA8A29E)

val EcruCanvas = Color(0xFFFAF8F5)
val LinenElevation = Color(0xFFF4EFEA)
val SurfaceContainer = Color(0xFFF5ECEA)
val SurfaceContainerHigh = Color(0xFFEFE6E4)
val BoneHairline = Color(0xFFE6DFD5)
val OutlineRoseClay = Color(0xFFE2BFB2)
val OutlineClay = Color(0xFF8E7166)

val TertiaryBronze = Color(0xFF665A48)
val TertiaryAlabaster = Color(0xFFF2E0C8)

val ErrorClay = Color(0xFFBA1A1A)
val ErrorContainerLight = Color(0xFFFFDAD6)
val OnErrorContainerLight = Color(0xFF93000A)

// Dark mode warm architectural palette (charred walnut & kiln ember)
val DarkWalnutCanvas = Color(0xFF1A1614)
val DarkWalnutSurface = Color(0xFF231E1B)
val DarkWalnutElevated = Color(0xFF2D2724)
val DarkBoneText = Color(0xFFF5ECE6)
val DarkMutedSand = Color(0xFFB8ACA4)
val DarkHairline = Color(0xFF3E3530)
val DarkTerracottaGlow = Color(0xFFFFB599)

@Immutable
data class TerraneColors(
    val canvas: Color,
    val elevatedSurface: Color,
    val cardSurface: Color,
    val hairline: Color,
    val primaryAccent: Color,
    val primaryHover: Color,
    val secondaryAccent: Color,
    val secondaryLabel: Color,
    val alabasterFill: Color,
    val warmHoverFill: Color,
    val inkPrimary: Color,
    val inkSecondary: Color,
    val inkMuted: Color,
    val isDark: Boolean,
)

val LightTerraneColors = TerraneColors(
    canvas = EcruCanvas,
    elevatedSurface = LinenElevation,
    cardSurface = Color.White,
    hairline = BoneHairline,
    primaryAccent = TerracottaPrimary,
    primaryHover = TerracottaHover,
    secondaryAccent = ApricotSecondary,
    secondaryLabel = ApricotLabel,
    alabasterFill = AlabasterGlaze,
    warmHoverFill = WarmHoverTint,
    inkPrimary = SmokedWalnut,
    inkSecondary = EarthVariant,
    inkMuted = MutedStone,
    isDark = false,
)

val DarkTerraneColors = TerraneColors(
    canvas = DarkWalnutCanvas,
    elevatedSurface = DarkWalnutSurface,
    cardSurface = DarkWalnutElevated,
    hairline = DarkHairline,
    primaryAccent = ApricotSecondary,
    primaryHover = TerracottaPrimary,
    secondaryAccent = DarkTerracottaGlow,
    secondaryLabel = DarkTerracottaGlow,
    alabasterFill = Color(0xFF3D271C),
    warmHoverFill = Color(0xFF312219),
    inkPrimary = DarkBoneText,
    inkSecondary = DarkMutedSand,
    inkMuted = Color(0xFF8C8078),
    isDark = true,
)

val LocalTerraneColors = staticCompositionLocalOf { LightTerraneColors }
