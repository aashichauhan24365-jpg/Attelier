package com.example.model

import androidx.compose.ui.graphics.Color

enum class ProductCategory(val displayName: String) {
    ALL("All Pieces"),
    SEATING("Seating"),
    TABLES("Tables"),
    LIGHTING("Lighting"),
    CERAMICS("Ceramics"),
    TEXTILES("Textiles"),
}

enum class RoomType(val displayName: String) {
    ALL("All Rooms"),
    LIVING("Living Salon"),
    DINING("Dining Room"),
    STUDY("Reading Study"),
    SANCTUARY("Bedroom Sanctuary"),
}

enum class MaterialTexturePattern {
    BOUCLE_LOOPS,
    WALNUT_GRAIN,
    TRAVERTINE_PORES,
    LINEN_WEAVE,
    TERRACOTTA_SPECKLE,
}

enum class MaterialType(
    val displayName: String,
    val swatchColor: Color,
    val innerRingColor: Color,
    val originNote: String,
    val swatchCode: String,
    val durabilityRating: String,
    val surfaceFinish: String,
    val wearGrade: String,
    val texturePattern: MaterialTexturePattern,
) {
    BOUCLE(
        displayName = "Bouclé",
        swatchColor = Color(0xFFF3ECE2),
        innerRingColor = Color(0xFFD6CBB8),
        originNote = "Belgian wool-alpaca looped weave",
        swatchCode = "MAT-BC01",
        durabilityRating = "45,000 Martindale",
        surfaceFinish = "Looped Virgin Pile",
        wearGrade = "Contract Grade A",
        texturePattern = MaterialTexturePattern.BOUCLE_LOOPS,
    ),
    WALNUT(
        displayName = "Walnut",
        swatchColor = Color(0xFF5D4334),
        innerRingColor = Color(0xFF3E2A1F),
        originNote = "Kiln-dried European smoked walnut",
        swatchCode = "MAT-WN04",
        durabilityRating = "1,010 lbf Janka",
        surfaceFinish = "Cold-Pressed Linseed",
        wearGrade = "Heirloom Hardwood",
        texturePattern = MaterialTexturePattern.WALNUT_GRAIN,
    ),
    TRAVERTINE(
        displayName = "Travertine",
        swatchColor = Color(0xFFE7DDD0),
        innerRingColor = Color(0xFFC5B6A2),
        originNote = "Honed unfilled Roman travertine",
        swatchCode = "MAT-TV02",
        durabilityRating = "Mohs 3.5 · Sealed",
        surfaceFinish = "Honed Unfilled Strata",
        wearGrade = "Architectural Stone",
        texturePattern = MaterialTexturePattern.TRAVERTINE_PORES,
    ),
    LINEN(
        displayName = "Linen",
        swatchColor = Color(0xFFDCD3C4),
        innerRingColor = Color(0xFFB5AA98),
        originNote = "Stonewashed Normandy raw flax",
        swatchCode = "MAT-LN03",
        durabilityRating = "32,000 Martindale",
        surfaceFinish = "540gsm Raw Flax",
        wearGrade = "Residential Heavy",
        texturePattern = MaterialTexturePattern.LINEN_WEAVE,
    ),
    TERRACOTTA(
        displayName = "Terracotta",
        swatchColor = Color(0xFFC8643B),
        innerRingColor = Color(0xFF9E4420),
        originNote = "Hand-thrown Impruneta red clay",
        swatchCode = "MAT-TC05",
        durabilityRating = "1,060°C Kiln-Fired",
        surfaceFinish = "Unglazed Mineral Clay",
        wearGrade = "Artisanal Fired Clay",
        texturePattern = MaterialTexturePattern.TERRACOTTA_SPECKLE,
    ),
}

enum class SilhouetteType {
    LOUNGE_CHAIR,
    SOFA,
    PLINTH_TABLE,
    DINING_TABLE,
    PENDANT_LAMP,
    VESSEL,
    TEXTILE_THROW,
}

enum class DimensionUnit(val shortLabel: String) {
    CM("cm"),
    INCHES("in"),
}

data class ColorSwatch(
    val name: String,
    val hexCode: String,
    val color: Color,
    val surfaceRole: String,
    val undertoneNote: String,
)

data class MaterialVariant(
    val materialType: MaterialType,
    val finishName: String,
    val finishNote: String,
)

data class ProductDimensions(
    val widthCm: Float,
    val depthCm: Float,
    val heightCm: Float,
    val detailLabel: String,
    val detailValueCm: Float,
    val weightKg: Float,
    val silhouetteType: SilhouetteType,
)

data class Product(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: ProductCategory,
    val roomType: RoomType,
    val stylingTip: String,
    val pairingSummary: String,
    val imageUrl: String,
    val secondaryImageUrl: String,
    val description: String,
    val provenance: String,
    val careGuide: String,
    val variants: List<MaterialVariant>,
    val dimensions: ProductDimensions,
    val suggestedColors: List<ColorSwatch>,
    val vignetteId: String? = null,
    val isFeaturedMood: Boolean = false,
)

data class RoomVignette(
    val id: String,
    val title: String,
    val seasonTag: String,
    val architecturalSubtitle: String,
    val description: String,
    val decorAdvice: String,
    val imageUrl: String,
    val roomArea: String,
    val lightingNote: String,
    val palette: List<ColorSwatch>,
    val featuredProductIds: List<String>,
)

data class RoomColorPalette(
    val id: String,
    val title: String,
    val roomType: RoomType,
    val moodTag: String,
    val architecturalSummary: String,
    val wallGuidance: String,
    val lightingGuidance: String,
    val colors: List<ColorSwatch>,
    val pairedMaterials: List<MaterialType>,
    val recommendedPieceIds: List<String>,
)
