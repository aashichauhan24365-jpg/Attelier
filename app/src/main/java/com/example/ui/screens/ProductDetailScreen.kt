package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.MeetingRoom
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.DimensionUnit
import com.example.model.Product
import com.example.ui.components.ArchitecturalPhoto
import com.example.ui.components.EditorialDimensionSpecifier
import com.example.ui.components.GhostPillButton
import com.example.ui.components.InteractiveColorPaletteStrip
import com.example.ui.components.InteractiveMaterialSwatch
import com.example.ui.components.MaterialTechnicalMetadataCard
import com.example.ui.components.PrimaryPillButton
import com.example.ui.components.TechnicalElevationCanvas
import com.example.ui.components.formatMetric
import com.example.ui.components.materialAccentColor
import com.example.ui.theme.TerraneThemeExtras
import com.example.ui.theme.tabular

@Composable
fun ProductDetailScreen(
    product: Product,
    isSaved: Boolean,
    dimensionUnit: DimensionUnit,
    onToggleUnit: () -> Unit,
    onToggleSave: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = TerraneThemeExtras.colors
    var selectedVariantIndex by remember(product.id) { mutableStateOf(0) }
    val selectedVariant = product.variants[selectedVariantIndex]
    var careExpanded by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(pageCount = { 3 })

    Scaffold(
        containerColor = colors.canvas,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            Surface(
                color = colors.canvas,
                border = BorderStroke(1.dp, colors.hairline),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = product.roomType.displayName.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.secondaryLabel,
                        )
                        Text(
                            text = selectedVariant.finishName,
                            style = MaterialTheme.typography.titleMedium,
                            color = colors.inkPrimary,
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    if (isSaved) {
                        GhostPillButton(
                            text = stringResource(R.string.saved_in_moodboard),
                            onClick = onToggleSave,
                            modifier = Modifier.testTag("detail_moodboard_button"),
                        )
                    } else {
                        PrimaryPillButton(
                            text = stringResource(R.string.save_to_moodboard),
                            onClick = onToggleSave,
                            modifier = Modifier.testTag("detail_moodboard_button"),
                        )
                    }
                }
            }
        },
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 32.dp),
                modifier = Modifier
                    .widthIn(max = 840.dp)
                    .fillMaxSize(),
            ) {
                // Portrait Swipeable Gallery + Architectural Drawing Slide
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(0.95f)
                            .background(colors.elevatedSurface),
                    ) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize(),
                        ) { page ->
                            when (page) {
                                0 -> ArchitecturalPhoto(
                                    imageUrl = product.imageUrl,
                                    contentDescription = product.title,
                                    silhouetteType = product.dimensions.silhouetteType,
                                    accentColor = materialAccentColor(selectedVariant.materialType),
                                    modifier = Modifier.fillMaxSize(),
                                )
                                1 -> ArchitecturalPhoto(
                                    imageUrl = product.secondaryImageUrl,
                                    contentDescription = "${product.title} interior study",
                                    silhouetteType = product.dimensions.silhouetteType,
                                    accentColor = materialAccentColor(selectedVariant.materialType),
                                    modifier = Modifier.fillMaxSize(),
                                )
                                else -> Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(colors.elevatedSurface)
                                        .padding(28.dp),
                                ) {
                                    TechnicalElevationCanvas(
                                        silhouetteType = product.dimensions.silhouetteType,
                                        widthLabel = formatMetric(product.dimensions.widthCm, dimensionUnit),
                                        heightLabel = formatMetric(product.dimensions.heightCm, dimensionUnit),
                                        modifier = Modifier.fillMaxSize(),
                                    )
                                }
                            }
                        }

                        // Floating Top Navigation Pills
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .statusBarsPadding()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                        ) {
                            IconButton(
                                onClick = onBack,
                                modifier = Modifier.testTag("detail_back_button"),
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(colors.canvas.copy(alpha = 0.9f))
                                        .border(1.dp, colors.hairline, CircleShape),
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                                        contentDescription = "Back",
                                        tint = colors.inkPrimary,
                                        modifier = Modifier.size(20.dp),
                                    )
                                }
                            }

                            IconButton(
                                onClick = onToggleSave,
                                modifier = Modifier.testTag("detail_save_button"),
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(colors.canvas.copy(alpha = 0.9f))
                                        .border(1.dp, colors.hairline, CircleShape),
                                ) {
                                    Icon(
                                        imageVector = if (isSaved) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                        contentDescription = if (isSaved) "Remove from moodboard" else "Save to moodboard",
                                        tint = if (isSaved) colors.primaryAccent else colors.inkPrimary,
                                        modifier = Modifier.size(20.dp),
                                    )
                                }
                            }
                        }

                        // Gallery Pager Dots
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 14.dp)
                                .clip(CircleShape)
                                .background(colors.canvas.copy(alpha = 0.88f))
                                .border(1.dp, colors.hairline, CircleShape)
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                        ) {
                            repeat(3) { index ->
                                Box(
                                    modifier = Modifier
                                        .size(if (pagerState.currentPage == index) 8.dp else 6.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (pagerState.currentPage == index) {
                                                colors.primaryAccent
                                            } else {
                                                colors.hairline
                                            },
                                        ),
                                )
                            }
                        }
                    }
                }

                // Editorial Overview, Styling Advice, Room Color Harmony, Material Swatches & Dimensions
                item {
                    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp)) {
                        Text(
                            text = product.subtitle,
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.secondaryLabel,
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = product.title,
                            style = MaterialTheme.typography.displayMedium,
                            color = colors.inkPrimary,
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Surface(
                            shape = CircleShape,
                            color = colors.alabasterFill,
                            border = BorderStroke(1.dp, colors.secondaryAccent.copy(alpha = 0.45f)),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.MeetingRoom,
                                    contentDescription = null,
                                    tint = colors.primaryAccent,
                                    modifier = Modifier.size(14.dp),
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = product.pairingSummary,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = colors.primaryAccent,
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))
                        Text(
                            text = product.description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = colors.inkSecondary,
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // Interior Decorating & Placement Suggestion
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = colors.elevatedSurface,
                            border = BorderStroke(1.dp, colors.hairline),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Row(
                                verticalAlignment = Alignment.Top,
                                modifier = Modifier.padding(16.dp),
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Lightbulb,
                                    contentDescription = null,
                                    tint = colors.primaryAccent,
                                    modifier = Modifier.size(20.dp),
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "ROOM STYLING & PLACEMENT",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = colors.secondaryLabel,
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = product.stylingTip,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = colors.inkPrimary,
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        HorizontalDivider(color = colors.hairline)
                        Spacer(modifier = Modifier.height(20.dp))

                        // Recommended Wall & Surface Color Pairing
                        Text(
                            text = "SUGGESTED ROOM COLOR PAIRING",
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.secondaryLabel,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tap a tone to view paint & surface guidance",
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.inkSecondary,
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        InteractiveColorPaletteStrip(swatches = product.suggestedColors)

                        Spacer(modifier = Modifier.height(24.dp))
                        HorizontalDivider(color = colors.hairline)
                        Spacer(modifier = Modifier.height(20.dp))

                        // Interactive Material Swatch Selector
                        Text(
                            text = "MATERIAL FINISH · TAP SWATCH FOR SPECS",
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.secondaryLabel,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = selectedVariant.finishName,
                            style = MaterialTheme.typography.titleMedium,
                            color = colors.inkPrimary,
                            fontWeight = FontWeight.Medium,
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        @OptIn(ExperimentalLayoutApi::class)
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            product.variants.forEachIndexed { index, variant ->
                                InteractiveMaterialSwatch(
                                    materialType = variant.materialType,
                                    finishName = variant.finishName,
                                    selected = selectedVariantIndex == index,
                                    onSelect = { selectedVariantIndex = index },
                                    modifier = Modifier.testTag("detail_swatch_${variant.materialType.name}"),
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        MaterialTechnicalMetadataCard(
                            materialType = selectedVariant.materialType,
                            finishName = selectedVariant.finishName,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = selectedVariant.finishNote,
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.inkSecondary,
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Editorial Dimension Specifier
                        EditorialDimensionSpecifier(
                            dimensions = product.dimensions,
                            unit = dimensionUnit,
                            onToggleUnit = onToggleUnit,
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Provenance & Care Accordion
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = colors.canvas,
                            border = BorderStroke(1.dp, colors.hairline),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Text(
                                    text = "CRAFT ORIGIN",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = colors.secondaryLabel,
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = product.provenance,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = colors.inkPrimary,
                                )

                                Spacer(modifier = Modifier.height(14.dp))
                                HorizontalDivider(color = colors.hairline)

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { careExpanded = !careExpanded }
                                        .padding(top = 14.dp)
                                        .testTag("care_accordion_toggle"),
                                ) {
                                    Text(
                                        text = "CARE & LONGEVITY GUIDE",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = colors.inkPrimary,
                                    )
                                    Icon(
                                        imageVector = if (careExpanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                                        contentDescription = "Toggle care guide",
                                        tint = colors.inkPrimary,
                                    )
                                }

                                AnimatedVisibility(visible = careExpanded) {
                                    Text(
                                        text = product.careGuide,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = colors.inkSecondary,
                                        modifier = Modifier.padding(top = 10.dp),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
