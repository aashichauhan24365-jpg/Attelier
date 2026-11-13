package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.Product
import com.example.model.RoomVignette
import com.example.model.SilhouetteType
import com.example.ui.components.ArchitecturalPhoto
import com.example.ui.components.EditorialTextLinkButton
import com.example.ui.components.PrimaryPillButton
import com.example.ui.components.TerraneProductCard
import com.example.ui.theme.TerraneThemeExtras
import com.example.ui.theme.tabular
import com.example.viewmodel.TerraneUiState

@Composable
fun LookbookScreen(
    uiState: TerraneUiState,
    columns: Int,
    onOpenProduct: (String) -> Unit,
    onQuickView: (Product) -> Unit,
    onToggleSave: (String) -> Unit,
    onOpenVignette: (RoomVignette) -> Unit,
    onOpenDecorIndex: () -> Unit,
    onOpenPalettes: () -> Unit,
    onOpenAtelierNotes: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = TerraneThemeExtras.colors
    val heroVignette = uiState.vignettes.first()
    val secondaryVignettes = uiState.vignettes.drop(1)
    val featuredProducts = uiState.allProducts.take(4)
    val chunkedFeatured = featuredProducts.chunked(columns)

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .fillMaxSize()
            .background(colors.canvas),
    ) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 36.dp),
            modifier = Modifier
                .widthIn(max = 960.dp)
                .fillMaxSize(),
        ) {
            // Editorial Masthead
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 18.dp),
                ) {
                    Column {
                        Text(
                            text = "TERRANE",
                            style = MaterialTheme.typography.headlineLarge,
                            color = colors.inkPrimary,
                        )
                        Text(
                            text = "INTERIOR ARCHITECTURE LOOKBOOK",
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.secondaryLabel,
                        )
                    }

                    Surface(
                        onClick = onOpenPalettes,
                        shape = CircleShape,
                        color = colors.elevatedSurface,
                        border = BorderStroke(1.dp, colors.hairline),
                        modifier = Modifier.testTag("masthead_palettes_button"),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Palette,
                                contentDescription = "Open Color Palettes",
                                tint = colors.primaryAccent,
                                modifier = Modifier.size(18.dp),
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Palettes",
                                style = MaterialTheme.typography.labelLarge,
                                color = colors.inkPrimary,
                            )
                        }
                    }
                }
            }

            // Hero Room Vignette Card (with inline Room Color Palette dots)
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(if (columns >= 3) 1.75f else 1.25f)
                            .clip(RoundedCornerShape(20.dp))
                            .border(1.dp, colors.hairline, RoundedCornerShape(20.dp))
                            .clickable { onOpenVignette(heroVignette) }
                            .testTag("hero_vignette_card"),
                    ) {
                        ArchitecturalPhoto(
                            imageUrl = heroVignette.imageUrl,
                            contentDescription = heroVignette.title,
                            silhouetteType = SilhouetteType.SOFA,
                            accentColor = colors.alabasterFill,
                            modifier = Modifier.fillMaxSize(),
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color(0x881E1B1A),
                                            Color(0xD41E1B1A),
                                        ),
                                    ),
                                ),
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.TopCenter)
                                .padding(16.dp),
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = colors.alabasterFill,
                            ) {
                                Text(
                                    text = heroVignette.seasonTag,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = colors.primaryAccent,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                )
                            }

                            Surface(
                                shape = CircleShape,
                                color = colors.canvas.copy(alpha = 0.92f),
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                ) {
                                    heroVignette.palette.forEach { swatch ->
                                        Box(
                                            modifier = Modifier
                                                .size(14.dp)
                                                .clip(CircleShape)
                                                .background(swatch.color)
                                                .border(1.dp, colors.hairline, CircleShape),
                                        )
                                    }
                                }
                            }
                        }

                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(20.dp),
                        ) {
                            Text(
                                text = heroVignette.title,
                                style = MaterialTheme.typography.displayMedium,
                                color = Color(0xFFFAF8F5),
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = heroVignette.architecturalSubtitle,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFFE6DFD5),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            PrimaryPillButton(
                                text = "Explore Room & Colors",
                                onClick = { onOpenVignette(heroVignette) },
                                modifier = Modifier.testTag("explore_vignette_button"),
                            )
                        }
                    }
                }
            }

            // Room Vignettes Story Strip
            item {
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                ) {
                    Column {
                        Text(
                            text = "CURATED ROOMS",
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.secondaryLabel,
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Spatial Inspiration",
                            style = MaterialTheme.typography.headlineLarge,
                            color = colors.inkPrimary,
                        )
                    }

                    EditorialTextLinkButton(
                        text = "All Palettes",
                        onClick = onOpenPalettes,
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(secondaryVignettes, key = { it.id }) { vignette ->
                        VignetteMiniCard(
                            vignette = vignette,
                            onClick = { onOpenVignette(vignette) },
                        )
                    }
                }
            }

            // Featured Decor Elements Section Header
            item {
                Spacer(modifier = Modifier.height(36.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "DECOR SUGGESTIONS",
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.secondaryLabel,
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Tactile Forms",
                            style = MaterialTheme.typography.headlineLarge,
                            color = colors.inkPrimary,
                        )
                    }

                    EditorialTextLinkButton(
                        text = "Decor Index",
                        onClick = onOpenDecorIndex,
                        modifier = Modifier.testTag("lookbook_full_catalog_link"),
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Featured Decor Grid
            items(chunkedFeatured) { rowProducts ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                ) {
                    rowProducts.forEach { product ->
                        TerraneProductCard(
                            product = product,
                            isSaved = product.id in uiState.savedProductIds,
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

            // Editorial Pull Quote & Color Philosophy
            item {
                Spacer(modifier = Modifier.height(28.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(colors.elevatedSurface)
                        .border(1.dp, colors.hairline, RoundedCornerShape(16.dp))
                        .padding(horizontal = 24.dp, vertical = 28.dp),
                ) {
                    Text(
                        text = "COLOR & MATERIAL PHILOSOPHY",
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.secondaryLabel,
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(
                        color = colors.hairline,
                        modifier = Modifier.width(64.dp),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.editorial_quote),
                        style = MaterialTheme.typography.headlineMedium.copy(fontStyle = FontStyle.Italic),
                        color = colors.inkPrimary,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    EditorialTextLinkButton(
                        text = "Read Material Notes",
                        onClick = onOpenAtelierNotes,
                        modifier = Modifier.testTag("read_atelier_notes_button"),
                    )
                }
            }
        }
    }
}

@Composable
private fun VignetteMiniCard(
    vignette: RoomVignette,
    onClick: () -> Unit,
) {
    val colors = TerraneThemeExtras.colors
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = colors.elevatedSurface,
        border = BorderStroke(1.dp, colors.hairline),
        modifier = Modifier
            .width(276.dp)
            .testTag("vignette_card_${vignette.id}"),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.5f),
            ) {
                ArchitecturalPhoto(
                    imageUrl = vignette.imageUrl,
                    contentDescription = vignette.title,
                    silhouetteType = SilhouetteType.DINING_TABLE,
                    accentColor = colors.alabasterFill,
                    modifier = Modifier.fillMaxSize(),
                )
                Surface(
                    shape = CircleShape,
                    color = colors.canvas.copy(alpha = 0.92f),
                    border = BorderStroke(1.dp, colors.hairline),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(10.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    ) {
                        vignette.palette.forEach { swatch ->
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(swatch.color)
                                    .border(0.5.dp, colors.hairline, CircleShape),
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${vignette.featuredProductIds.size} cues",
                            style = MaterialTheme.typography.labelSmall.tabular(),
                            color = colors.inkPrimary,
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = vignette.seasonTag,
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.secondaryLabel,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = vignette.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = colors.inkPrimary,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = vignette.roomArea,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.inkSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
