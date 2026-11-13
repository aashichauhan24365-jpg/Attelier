package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.Product
import com.example.model.RoomColorPalette
import com.example.model.RoomType
import com.example.ui.components.ArchitecturalPhoto
import com.example.ui.components.FilterChipPill
import com.example.ui.components.GhostPillButton
import com.example.ui.components.InteractiveColorPaletteStrip
import com.example.ui.components.InteractiveMaterialSwatch
import com.example.ui.components.MaterialTechnicalMetadataCard
import com.example.ui.components.TerraneProductCard
import com.example.ui.components.materialAccentColor
import com.example.ui.theme.TerraneThemeExtras
import com.example.ui.theme.tabular
import com.example.viewmodel.TerraneUiState

@Composable
fun PaletteStudioScreen(
    uiState: TerraneUiState,
    onSelectRoomFilter: (RoomType) -> Unit,
    onToggleSavePalette: (String) -> Unit,
    onOpenProduct: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = TerraneThemeExtras.colors
    val palettes = uiState.filteredPalettes

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .fillMaxSize()
            .background(colors.canvas),
    ) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 36.dp),
            modifier = Modifier
                .widthIn(max = 900.dp)
                .fillMaxSize(),
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 18.dp),
                ) {
                    Text(
                        text = "COLOR & DECOR GUIDE",
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.secondaryLabel,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Room Palettes",
                        style = MaterialTheme.typography.displayMedium,
                        color = colors.inkPrimary,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Architectural wall tones, lighting cues, and material pairings tailored to each room.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.inkSecondary,
                    )
                }
            }

            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(RoomType.entries) { room ->
                        FilterChipPill(
                            label = room.displayName,
                            selected = uiState.selectedRoomFilter == room,
                            onClick = { onSelectRoomFilter(room) },
                            modifier = Modifier.testTag("palette_room_chip_${room.name}"),
                        )
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
            }

            items(palettes, key = { it.id }) { palette ->
                RoomPaletteSuggestionCard(
                    palette = palette,
                    allProducts = uiState.allProducts,
                    isSaved = palette.id in uiState.savedPaletteIds,
                    onToggleSave = { onToggleSavePalette(palette.id) },
                    onOpenProduct = onOpenProduct,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                )
            }
        }
    }
}

@Composable
private fun RoomPaletteSuggestionCard(
    palette: RoomColorPalette,
    allProducts: List<Product>,
    isSaved: Boolean,
    onToggleSave: () -> Unit,
    onOpenProduct: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = TerraneThemeExtras.colors
    var inspectedMaterial by remember(palette.id) { mutableStateOf(palette.pairedMaterials.first()) }
    val suggestedPieces = palette.recommendedPieceIds.mapNotNull { id ->
        allProducts.find { it.id == id }
    }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = colors.cardSurface,
        border = BorderStroke(1.dp, colors.hairline),
        modifier = modifier
            .fillMaxWidth()
            .testTag("room_palette_card_${palette.id}"),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${palette.roomType.displayName.uppercase()} · ${palette.moodTag}",
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.secondaryLabel,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = palette.title,
                        style = MaterialTheme.typography.headlineLarge,
                        color = colors.inkPrimary,
                    )
                }

                IconButton(
                    onClick = onToggleSave,
                    modifier = Modifier.testTag("save_palette_${palette.id}"),
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (isSaved) "Remove palette from moodboard" else "Save palette to moodboard",
                        tint = if (isSaved) colors.primaryAccent else colors.inkPrimary,
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = palette.architecturalSummary,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.inkSecondary,
            )

            Spacer(modifier = Modifier.height(16.dp))
            InteractiveColorPaletteStrip(swatches = palette.colors)

            Spacer(modifier = Modifier.height(16.dp))

            // Wall & Lighting Advice
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = colors.elevatedSurface,
                border = BorderStroke(1.dp, colors.hairline),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(14.dp),
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Outlined.Palette,
                            contentDescription = null,
                            tint = colors.primaryAccent,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "WALL & FINISH SUGGESTION",
                                style = MaterialTheme.typography.labelSmall,
                                color = colors.inkMuted,
                            )
                            Text(
                                text = palette.wallGuidance,
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.inkPrimary,
                            )
                        }
                    }
                    HorizontalDivider(color = colors.hairline)
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Outlined.Lightbulb,
                            contentDescription = null,
                            tint = colors.primaryAccent,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "LIGHTING & ATMOSPHERE",
                                style = MaterialTheme.typography.labelSmall,
                                color = colors.inkMuted,
                            )
                            Text(
                                text = palette.lightingGuidance,
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.inkPrimary,
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "PAIRED MATERIALS · TAP TO INSPECT",
                style = MaterialTheme.typography.labelSmall,
                color = colors.secondaryLabel,
            )
            Spacer(modifier = Modifier.height(8.dp))
            @OptIn(ExperimentalLayoutApi::class)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                palette.pairedMaterials.forEach { material ->
                    InteractiveMaterialSwatch(
                        materialType = material,
                        selected = inspectedMaterial == material,
                        onSelect = { inspectedMaterial = material },
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            MaterialTechnicalMetadataCard(materialType = inspectedMaterial)

            if (suggestedPieces.isNotEmpty()) {
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = "SUGGESTED DECOR PIECES",
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.secondaryLabel,
                )
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(suggestedPieces, key = { it.id }) { piece ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = colors.elevatedSurface,
                            border = BorderStroke(1.dp, colors.hairline),
                            modifier = Modifier
                                .width(154.dp)
                                .clickable { onOpenProduct(piece.id) },
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(104.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .border(1.dp, colors.hairline, RoundedCornerShape(10.dp)),
                                ) {
                                    ArchitecturalPhoto(
                                        imageUrl = piece.imageUrl,
                                        contentDescription = piece.title,
                                        silhouetteType = piece.dimensions.silhouetteType,
                                        accentColor = materialAccentColor(piece.variants.first().materialType),
                                        modifier = Modifier.fillMaxSize(),
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = piece.title,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = colors.inkPrimary,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SavedScreen(
    uiState: TerraneUiState,
    columns: Int,
    onOpenProduct: (String) -> Unit,
    onQuickView: (Product) -> Unit,
    onToggleSave: (String) -> Unit,
    onToggleSavePalette: (String) -> Unit,
    onExploreDecor: () -> Unit,
    onExplorePalettes: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = TerraneThemeExtras.colors
    val savedPieces = uiState.savedProducts
    val savedPalettes = uiState.savedPalettes
    val rows = savedPieces.chunked(columns)

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .fillMaxSize()
            .background(colors.canvas),
    ) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 32.dp),
            modifier = Modifier
                .widthIn(max = 960.dp)
                .fillMaxSize(),
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 18.dp),
                ) {
                    Text(
                        text = "HOME DECOR BOARD",
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.secondaryLabel,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Moodboard",
                        style = MaterialTheme.typography.displayMedium,
                        color = colors.inkPrimary,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${savedPalettes.size} color palettes · ${savedPieces.size} decor pieces",
                        style = MaterialTheme.typography.bodyMedium.tabular(),
                        color = colors.inkSecondary,
                    )
                }
            }

            if (savedPalettes.isNotEmpty()) {
                item {
                    Text(
                        text = "SAVED COLOR PALETTES",
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.secondaryLabel,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp),
                    )
                }

                items(savedPalettes, key = { it.id }) { palette ->
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = colors.cardSurface,
                        border = BorderStroke(1.dp, colors.hairline),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 6.dp),
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = palette.roomType.displayName.uppercase(),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = colors.secondaryLabel,
                                    )
                                    Text(
                                        text = palette.title,
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = colors.inkPrimary,
                                    )
                                }
                                IconButton(onClick = { onToggleSavePalette(palette.id) }) {
                                    Icon(
                                        imageVector = Icons.Filled.Favorite,
                                        contentDescription = "Remove ${palette.title}",
                                        tint = colors.primaryAccent,
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                palette.colors.forEach { swatch ->
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.weight(1f),
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(34.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(swatch.color)
                                                .border(1.dp, colors.hairline, RoundedCornerShape(8.dp)),
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = swatch.hexCode,
                                            style = MaterialTheme.typography.labelSmall.tabular(),
                                            color = colors.inkMuted,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (savedPieces.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "SAVED DECOR PIECES",
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.secondaryLabel,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp),
                    )
                }

                items(rows) { rowProducts ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                    ) {
                        rowProducts.forEach { product ->
                            TerraneProductCard(
                                product = product,
                                isSaved = true,
                                onToggleSave = { onToggleSave(product.id) },
                                onQuickView = { onQuickView(product) },
                                onOpenDetail = { onOpenProduct(product.id) },
                                modifier = Modifier.weight(1f),
                            )
                        }
                        repeat(columns - rowProducts.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            if (savedPieces.isEmpty() && savedPalettes.isEmpty()) {
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp, vertical = 64.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.FavoriteBorder,
                            contentDescription = null,
                            tint = colors.inkMuted,
                            modifier = Modifier.size(44.dp),
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Your moodboard is quiet",
                            style = MaterialTheme.typography.headlineSmall,
                            color = colors.inkPrimary,
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Save room color palettes and architectural decor studies to plan your home.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.inkSecondary,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            GhostPillButton(
                                text = "Room Palettes",
                                onClick = onExplorePalettes,
                            )
                            GhostPillButton(
                                text = "Decor Index",
                                onClick = onExploreDecor,
                            )
                        }
                    }
                }
            }
        }
    }
}
