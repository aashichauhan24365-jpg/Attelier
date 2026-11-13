package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.DimensionUnit
import com.example.model.MaterialType
import com.example.model.Product
import com.example.model.RoomVignette
import com.example.model.SilhouetteType
import com.example.ui.components.ArchitecturalPhoto
import com.example.ui.components.EditorialTextLinkButton
import com.example.ui.components.GhostPillButton
import com.example.ui.components.InteractiveColorPaletteStrip
import com.example.ui.components.InteractiveMaterialSwatch
import com.example.ui.components.MaterialTechnicalMetadataCard
import com.example.ui.components.PrimaryPillButton
import com.example.ui.components.formatMetric
import com.example.ui.components.materialAccentColor
import com.example.ui.theme.TerraneThemeExtras
import com.example.ui.theme.tabular

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickViewSheet(
    product: Product,
    isSaved: Boolean,
    dimensionUnit: DimensionUnit,
    onDismiss: () -> Unit,
    onToggleSave: () -> Unit,
    onViewFullSpecifications: () -> Unit,
) {
    val colors = TerraneThemeExtras.colors
    var selectedIdx by remember(product.id) { mutableStateOf(0) }
    val selectedVariant = product.variants[selectedIdx]
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colors.canvas,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(bottom = 28.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 96.dp, height = 120.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, colors.hairline, RoundedCornerShape(14.dp)),
                ) {
                    ArchitecturalPhoto(
                        imageUrl = product.imageUrl,
                        contentDescription = product.title,
                        silhouetteType = product.dimensions.silhouetteType,
                        accentColor = materialAccentColor(selectedVariant.materialType),
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = product.subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.secondaryLabel,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = product.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = colors.inkPrimary,
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = product.pairingSummary,
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.primaryAccent,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${formatMetric(product.dimensions.widthCm, dimensionUnit)} × ${formatMetric(product.dimensions.depthCm, dimensionUnit)} × ${formatMetric(product.dimensions.heightCm, dimensionUnit)}",
                        style = MaterialTheme.typography.bodySmall.tabular(),
                        color = colors.inkMuted,
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
            HorizontalDivider(color = colors.hairline)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "INSPECT MATERIAL · ${selectedVariant.finishName.uppercase()}",
                style = MaterialTheme.typography.labelSmall,
                color = colors.secondaryLabel,
            )
            Spacer(modifier = Modifier.height(10.dp))
            @OptIn(ExperimentalLayoutApi::class)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                product.variants.forEachIndexed { index, variant ->
                    InteractiveMaterialSwatch(
                        materialType = variant.materialType,
                        finishName = variant.finishName,
                        selected = selectedIdx == index,
                        onSelect = { selectedIdx = index },
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            MaterialTechnicalMetadataCard(
                materialType = selectedVariant.materialType,
                finishName = selectedVariant.finishName,
            )

            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = product.stylingTip,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.inkSecondary,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(20.dp))
            if (isSaved) {
                GhostPillButton(
                    text = stringResource(R.string.saved_in_moodboard),
                    onClick = onToggleSave,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("quick_view_moodboard_button"),
                )
            } else {
                PrimaryPillButton(
                    text = stringResource(R.string.save_to_moodboard),
                    onClick = onToggleSave,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("quick_view_moodboard_button"),
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth(),
            ) {
                EditorialTextLinkButton(
                    text = "Styling, Colors & Dimensions",
                    onClick = onViewFullSpecifications,
                    modifier = Modifier.testTag("quick_view_full_specs"),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VignetteLookbookSheet(
    vignette: RoomVignette,
    allProducts: List<Product>,
    onDismiss: () -> Unit,
    onOpenProduct: (String) -> Unit,
) {
    val colors = TerraneThemeExtras.colors
    val vignetteProducts = vignette.featuredProductIds.mapNotNull { id ->
        allProducts.find { it.id == id }
    }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colors.canvas,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(bottom = 28.dp),
        ) {
            Text(
                text = vignette.seasonTag,
                style = MaterialTheme.typography.labelSmall,
                color = colors.secondaryLabel,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = vignette.title,
                style = MaterialTheme.typography.displayMedium,
                color = colors.inkPrimary,
            )
            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.6f)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, colors.hairline, RoundedCornerShape(16.dp)),
            ) {
                ArchitecturalPhoto(
                    imageUrl = vignette.imageUrl,
                    contentDescription = vignette.title,
                    silhouetteType = SilhouetteType.SOFA,
                    accentColor = colors.alabasterFill,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = vignette.description,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.inkSecondary,
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "ROOM COLOR PALETTE",
                style = MaterialTheme.typography.labelSmall,
                color = colors.secondaryLabel,
            )
            Spacer(modifier = Modifier.height(10.dp))
            InteractiveColorPaletteStrip(swatches = vignette.palette)

            Spacer(modifier = Modifier.height(14.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = colors.elevatedSurface,
                border = BorderStroke(1.dp, colors.hairline),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(14.dp),
                ) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("SPATIAL PROPORTION", style = MaterialTheme.typography.labelSmall, color = colors.inkMuted)
                            Text(vignette.roomArea, style = MaterialTheme.typography.bodySmall, color = colors.inkPrimary)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("LIGHTING STUDY", style = MaterialTheme.typography.labelSmall, color = colors.inkMuted)
                            Text(vignette.lightingNote, style = MaterialTheme.typography.bodySmall, color = colors.inkPrimary)
                        }
                    }
                    HorizontalDivider(color = colors.hairline)
                    Column {
                        Text("DECORATING NOTE", style = MaterialTheme.typography.labelSmall, color = colors.inkMuted)
                        Text(vignette.decorAdvice, style = MaterialTheme.typography.bodySmall, color = colors.inkPrimary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "KEY FORMS IN THIS ROOM",
                style = MaterialTheme.typography.labelSmall,
                color = colors.secondaryLabel,
            )
            Spacer(modifier = Modifier.height(10.dp))

            vignetteProducts.forEach { product ->
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = colors.cardSurface,
                    border = BorderStroke(1.dp, colors.hairline),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .clickable { onOpenProduct(product.id) },
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(10.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, colors.hairline, RoundedCornerShape(10.dp)),
                        ) {
                            ArchitecturalPhoto(
                                imageUrl = product.imageUrl,
                                contentDescription = product.title,
                                silhouetteType = product.dimensions.silhouetteType,
                                accentColor = materialAccentColor(product.variants.first().materialType),
                                modifier = Modifier.fillMaxSize(),
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = product.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = colors.inkPrimary,
                            )
                            Text(
                                text = product.pairingSummary,
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.inkSecondary,
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AtelierNotesSheet(
    onDismiss: () -> Unit,
) {
    val colors = TerraneThemeExtras.colors
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colors.canvas,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(bottom = 28.dp),
        ) {
            Text(
                text = "ATELIER NOTES · MATERIAL & COLOR",
                style = MaterialTheme.typography.labelSmall,
                color = colors.secondaryLabel,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "On Kiln-Fired Clay & Sun-Bleached Flax",
                style = MaterialTheme.typography.displayMedium,
                color = colors.inkPrimary,
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Every surface in our architectural lookbook is chosen for how it receives natural afternoon light. Unfilled Roman travertine retains microscopic stone cavities; Impruneta clay ages with a quiet matte patina; Belgian bouclé softens acoustic reverberation across plaster walls.",
                style = MaterialTheme.typography.bodyLarge,
                color = colors.inkSecondary,
            )
            Spacer(modifier = Modifier.height(18.dp))
            var inspectedMaterial by remember { mutableStateOf(MaterialType.TRAVERTINE) }
            Text(
                text = "TAP A MATERIAL SAMPLE TO INSPECT SPECIFICATIONS",
                style = MaterialTheme.typography.labelSmall,
                color = colors.secondaryLabel,
            )
            Spacer(modifier = Modifier.height(10.dp))
            @OptIn(ExperimentalLayoutApi::class)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                MaterialType.entries.forEach { material ->
                    InteractiveMaterialSwatch(
                        materialType = material,
                        selected = inspectedMaterial == material,
                        onSelect = { inspectedMaterial = material },
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            MaterialTechnicalMetadataCard(materialType = inspectedMaterial)
            Spacer(modifier = Modifier.height(22.dp))
            PrimaryPillButton(
                text = "Return to Lookbook",
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
