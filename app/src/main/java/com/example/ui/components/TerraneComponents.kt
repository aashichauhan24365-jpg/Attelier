package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.ColorSwatch
import com.example.model.DimensionUnit
import com.example.model.MaterialTexturePattern
import com.example.model.MaterialType
import com.example.model.Product
import com.example.model.ProductDimensions
import com.example.ui.theme.TerraneThemeExtras
import com.example.ui.theme.tabular
import java.util.Locale

fun formatMetric(cmValue: Float, unit: DimensionUnit): String {
    return when (unit) {
        DimensionUnit.CM -> {
            if (cmValue % 1f == 0f) "${cmValue.toInt()} cm" else String.format(Locale.US, "%.1f cm", cmValue)
        }
        DimensionUnit.INCHES -> {
            val inches = cmValue / 2.54f
            String.format(Locale.US, "%.1f in", inches)
        }
    }
}

@Composable
fun PrimaryPillButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val colors = TerraneThemeExtras.colors
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.primaryAccent,
            contentColor = Color.White,
            disabledContainerColor = colors.hairline,
            disabledContentColor = colors.inkMuted,
        ),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp),
        modifier = modifier.height(48.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Composable
fun GhostPillButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = TerraneThemeExtras.colors
    OutlinedButton(
        onClick = onClick,
        shape = CircleShape,
        border = BorderStroke(1.dp, colors.hairline),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = colors.inkPrimary,
        ),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
        modifier = modifier.height(48.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Composable
fun EditorialTextLinkButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = TerraneThemeExtras.colors
    val lineColor = colors.inkPrimary
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .minimumInteractiveComponentSize()
            .clickable(role = Role.Button, onClick = onClick)
            .padding(vertical = 8.dp)
            .drawBehind {
                val y = size.height + 2.dp.toPx()
                drawLine(
                    color = lineColor,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1.dp.toPx(),
                )
            },
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            color = colors.inkPrimary,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
            contentDescription = null,
            tint = colors.inkPrimary,
            modifier = Modifier.size(16.dp),
        )
    }
}

@Composable
fun FilterChipPill(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = TerraneThemeExtras.colors
    val bgColor by animateColorAsState(
        targetValue = if (selected) colors.alabasterFill else colors.elevatedSurface,
        label = "chipBg",
    )
    val borderColor by animateColorAsState(
        targetValue = if (selected) colors.secondaryAccent else Color.Transparent,
        label = "chipBorder",
    )
    val textColor by animateColorAsState(
        targetValue = if (selected) colors.primaryAccent else colors.inkPrimary,
        label = "chipText",
    )

    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = bgColor,
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier
            .minimumInteractiveComponentSize()
            .height(36.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                    letterSpacing = MaterialTheme.typography.bodySmall.letterSpacing,
                ),
                color = textColor,
                maxLines = 1,
            )
        }
    }
}

@Composable
fun MaterialTextureSample(
    materialType: MaterialType,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier
            .clip(CircleShape)
            .background(materialType.swatchColor),
    ) {
        val w = size.width
        val h = size.height
        val grainColor = materialType.innerRingColor.copy(alpha = 0.48f)

        when (materialType.texturePattern) {
            MaterialTexturePattern.BOUCLE_LOOPS -> {
                val r = w * 0.14f
                listOf(
                    Offset(w * 0.30f, h * 0.34f),
                    Offset(w * 0.62f, h * 0.30f),
                    Offset(w * 0.44f, h * 0.56f),
                    Offset(w * 0.70f, h * 0.64f),
                    Offset(w * 0.28f, h * 0.70f),
                ).forEach { center ->
                    drawCircle(
                        color = grainColor,
                        radius = r,
                        center = center,
                        style = Stroke(width = 1.dp.toPx()),
                    )
                }
            }

            MaterialTexturePattern.WALNUT_GRAIN -> {
                val grainStroke = Stroke(width = 1.1.dp.toPx(), cap = StrokeCap.Round)
                listOf(0.26f, 0.48f, 0.70f).forEach { yFrac ->
                    val path = Path().apply {
                        moveTo(0f, h * yFrac)
                        cubicTo(
                            w * 0.35f,
                            h * (yFrac - 0.08f),
                            w * 0.65f,
                            h * (yFrac + 0.08f),
                            w,
                            h * yFrac,
                        )
                    }
                    drawPath(path, color = grainColor, style = grainStroke)
                }
            }

            MaterialTexturePattern.TRAVERTINE_PORES -> {
                val poreStroke = Stroke(width = 1.2.dp.toPx(), cap = StrokeCap.Round)
                drawLine(grainColor, Offset(w * 0.16f, h * 0.32f), Offset(w * 0.54f, h * 0.32f), 1.2.dp.toPx(), StrokeCap.Round)
                drawLine(grainColor, Offset(w * 0.42f, h * 0.52f), Offset(w * 0.84f, h * 0.52f), 1.2.dp.toPx(), StrokeCap.Round)
                drawLine(grainColor, Offset(w * 0.22f, h * 0.72f), Offset(w * 0.64f, h * 0.72f), 1.2.dp.toPx(), StrokeCap.Round)
                drawCircle(grainColor, radius = 1.dp.toPx(), center = Offset(w * 0.70f, h * 0.32f), style = poreStroke)
            }

            MaterialTexturePattern.LINEN_WEAVE -> {
                val threadColor = grainColor.copy(alpha = 0.42f)
                listOf(0.28f, 0.50f, 0.72f).forEach { frac ->
                    drawLine(threadColor, Offset(0f, h * frac), Offset(w, h * frac), 0.9.dp.toPx())
                    drawLine(threadColor, Offset(w * frac, 0f), Offset(w * frac, h), 0.9.dp.toPx())
                }
            }

            MaterialTexturePattern.TERRACOTTA_SPECKLE -> {
                val fleckColor = Color(0xFFFAF8F5).copy(alpha = 0.42f)
                listOf(
                    Offset(w * 0.28f, h * 0.32f),
                    Offset(w * 0.66f, h * 0.28f),
                    Offset(w * 0.52f, h * 0.52f),
                    Offset(w * 0.32f, h * 0.68f),
                    Offset(w * 0.72f, h * 0.70f),
                ).forEach { pos ->
                    drawCircle(color = fleckColor, radius = 1.1.dp.toPx(), center = pos)
                }
            }
        }
    }
}

@Composable
fun MaterialSwatchCircle(
    materialType: MaterialType,
    selected: Boolean,
    onClick: (() -> Unit)? = null,
    size: Dp = 24.dp,
    modifier: Modifier = Modifier,
) {
    val colors = TerraneThemeExtras.colors
    val outerBorder = if (selected) colors.primaryAccent else colors.hairline
    val outerWidth = if (selected) 2.dp else 1.dp

    val baseModifier = modifier
        .then(if (onClick != null) Modifier.minimumInteractiveComponentSize() else Modifier)
        .size(size + 8.dp)
        .clip(CircleShape)
        .then(
            if (onClick != null) {
                Modifier.clickable(
                    role = Role.RadioButton,
                    onClickLabel = materialType.displayName,
                    onClick = onClick,
                )
            } else {
                Modifier
            },
        )
        .border(outerWidth, outerBorder, CircleShape)
        .padding(3.dp)

    Box(
        contentAlignment = Alignment.Center,
        modifier = baseModifier,
    ) {
        MaterialTextureSample(
            materialType = materialType,
            modifier = Modifier
                .fillMaxSize()
                .border(1.dp, materialType.innerRingColor.copy(alpha = 0.55f), CircleShape),
        )
    }
}

/**
 * Interactive material swatch component that displays a tactile color/texture sample
 * and reveals technical metadata (material name, swatch code, durability rating,
 * surface finish, wear grade, and provenance) when tapped.
 */
@Composable
fun InteractiveMaterialSwatch(
    materialType: MaterialType,
    modifier: Modifier = Modifier,
    finishName: String = materialType.displayName,
    selected: Boolean? = null,
    initiallyExpanded: Boolean = false,
    showInlineLabel: Boolean = true,
    onSelect: (() -> Unit)? = null,
) {
    val colors = TerraneThemeExtras.colors
    var internalExpanded by remember(materialType) { mutableStateOf(initiallyExpanded) }
    val isExpanded = selected ?: internalExpanded
    val borderColor by animateColorAsState(
        targetValue = if (isExpanded) colors.primaryAccent else colors.hairline,
        label = "interactiveSwatchBorder",
    )

    Column(
        modifier = modifier
            .testTag("interactive_material_swatch_${materialType.name}"),
    ) {
        Surface(
            onClick = {
                if (selected == null) {
                    internalExpanded = !internalExpanded
                }
                onSelect?.invoke()
            },
            shape = CircleShape,
            color = if (isExpanded) colors.alabasterFill else colors.elevatedSurface,
            border = BorderStroke(if (isExpanded) 1.5.dp else 1.dp, borderColor),
            modifier = Modifier
                .minimumInteractiveComponentSize()
                .testTag("swatch_sample_button_${materialType.name}"),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(
                    start = 6.dp,
                    end = if (showInlineLabel) 14.dp else 6.dp,
                    top = 6.dp,
                    bottom = 6.dp,
                ),
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .border(
                            width = if (isExpanded) 1.5.dp else 1.dp,
                            color = if (isExpanded) colors.primaryAccent else materialType.innerRingColor,
                            shape = CircleShape,
                        )
                        .padding(2.dp),
                ) {
                    MaterialTextureSample(
                        materialType = materialType,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                if (showInlineLabel) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = materialType.displayName,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = if (isExpanded) FontWeight.SemiBold else FontWeight.Medium,
                            letterSpacing = MaterialTheme.typography.bodySmall.letterSpacing,
                        ),
                        color = if (isExpanded) colors.primaryAccent else colors.inkPrimary,
                        maxLines = 1,
                    )
                }
            }
        }

        // Reveals technical metadata smoothly when tapped (standalone mode)
        if (selected == null) {
            AnimatedVisibility(
                visible = internalExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
            ) {
                MaterialTechnicalMetadataCard(
                    materialType = materialType,
                    finishName = finishName,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }
    }
}

@Composable
fun MaterialTechnicalMetadataCard(
    materialType: MaterialType,
    finishName: String = materialType.displayName,
    modifier: Modifier = Modifier,
) {
    val colors = TerraneThemeExtras.colors
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = colors.elevatedSurface,
        border = BorderStroke(1.dp, colors.hairline),
        modifier = modifier
            .fillMaxWidth()
            .testTag("swatch_metadata_card_${materialType.name}"),
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f),
                ) {
                    MaterialTextureSample(
                        materialType = materialType,
                        modifier = Modifier
                            .size(20.dp)
                            .border(1.dp, materialType.innerRingColor, CircleShape),
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = materialType.displayName.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.secondaryLabel,
                        )
                        Text(
                            text = finishName,
                            style = MaterialTheme.typography.titleMedium,
                            color = colors.inkPrimary,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = colors.alabasterFill,
                    border = BorderStroke(1.dp, colors.secondaryAccent.copy(alpha = 0.45f)),
                ) {
                    Text(
                        text = materialType.swatchCode,
                        style = MaterialTheme.typography.labelSmall.tabular(),
                        color = colors.primaryAccent,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = colors.hairline)
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                DimensionMetricCell(
                    label = "DURABILITY",
                    value = materialType.durabilityRating,
                    modifier = Modifier.weight(1f),
                )
                DimensionMetricCell(
                    label = "SURFACE FINISH",
                    value = materialType.surfaceFinish,
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                DimensionMetricCell(
                    label = "WEAR GRADE",
                    value = materialType.wearGrade,
                    modifier = Modifier.weight(1f),
                )
                DimensionMetricCell(
                    label = "PROVENANCE",
                    value = materialType.originNote,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
fun PillSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String = stringResource(R.string.search_placeholder),
    modifier: Modifier = Modifier,
) {
    val colors = TerraneThemeExtras.colors
    var isFocused by remember { mutableStateOf(false) }

    val bgColor by animateColorAsState(
        targetValue = if (isFocused) colors.cardSurface else colors.elevatedSurface,
        label = "searchBg",
    )
    val borderColor by animateColorAsState(
        targetValue = if (isFocused) colors.primaryAccent else colors.hairline,
        label = "searchBorder",
    )
    val borderWidth = if (isFocused) 1.5.dp else 1.dp

    Surface(
        shape = CircleShape,
        color = bgColor,
        border = BorderStroke(borderWidth, borderColor),
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "Search",
                tint = if (isFocused) colors.primaryAccent else colors.inkMuted,
                modifier = Modifier.size(20.dp),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier.weight(1f),
            ) {
                if (query.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.inkMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = colors.inkPrimary),
                    cursorBrush = SolidColor(colors.primaryAccent),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("catalog_search_input")
                        .onFocusChanged { isFocused = it.isFocused },
                )
            }
            if (query.isNotEmpty()) {
                IconButton(
                    onClick = { onQueryChange("") },
                    modifier = Modifier.size(36.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Clear search",
                        tint = colors.inkSecondary,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun TerraneProductCard(
    product: Product,
    isSaved: Boolean,
    onToggleSave: () -> Unit,
    onQuickView: () -> Unit,
    onOpenDetail: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = TerraneThemeExtras.colors
    val primarySwatch = product.variants.firstOrNull()?.materialType

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onOpenDetail)
            .testTag("product_card_${product.id}"),
    ) {
        // Frameless 4:5 portrait container on #F4EFEA with subtle hairline
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.8f)
                .clip(RoundedCornerShape(16.dp))
                .background(colors.elevatedSurface)
                .border(1.dp, colors.hairline, RoundedCornerShape(16.dp)),
        ) {
            ArchitecturalPhoto(
                imageUrl = product.imageUrl,
                contentDescription = product.title,
                silhouetteType = product.dimensions.silhouetteType,
                accentColor = materialAccentColor(primarySwatch),
                modifier = Modifier.fillMaxSize(),
            )

            // Top-left FEATURED mood badge if applicable
            if (product.isFeaturedMood) {
                Surface(
                    shape = CircleShape,
                    color = colors.alabasterFill.copy(alpha = 0.94f),
                    border = BorderStroke(1.dp, colors.secondaryAccent.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp),
                ) {
                    Text(
                        text = product.roomType.displayName.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.primaryAccent,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    )
                }
            }

            // Top-right Moodboard Heart with 48dp accessible touch target
            IconButton(
                onClick = onToggleSave,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .testTag("save_toggle_${product.id}"),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(colors.canvas.copy(alpha = 0.88f))
                        .border(1.dp, colors.hairline, CircleShape),
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (isSaved) "Remove ${product.title} from moodboard" else "Save ${product.title} to moodboard",
                        tint = if (isSaved) colors.primaryAccent else colors.inkPrimary,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }

            // Subtle "Inspect" pill centered along the base per Warm Architectural Living spec
            Surface(
                onClick = onQuickView,
                shape = CircleShape,
                color = colors.canvas.copy(alpha = 0.92f),
                border = BorderStroke(1.dp, colors.hairline),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 10.dp)
                    .testTag("quick_view_${product.id}"),
            ) {
                Text(
                    text = stringResource(R.string.quick_view),
                    style = MaterialTheme.typography.labelMedium,
                    color = colors.inkPrimary,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Typography Stack: Category microcopy -> EB Garamond name -> Room pairing & color dots
        Text(
            text = product.subtitle,
            style = MaterialTheme.typography.labelSmall,
            color = colors.secondaryLabel,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = product.title,
            style = MaterialTheme.typography.headlineSmall,
            color = colors.inkPrimary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = product.roomType.displayName,
                style = MaterialTheme.typography.bodySmall,
                color = colors.inkSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )

            Spacer(modifier = Modifier.width(6.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                product.suggestedColors.forEach { swatch ->
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(swatch.color)
                            .border(1.dp, colors.hairline, CircleShape),
                    )
                }
            }
        }
    }
}

@Composable
fun InteractiveColorPaletteStrip(
    swatches: List<ColorSwatch>,
    modifier: Modifier = Modifier,
) {
    if (swatches.isEmpty()) return
    val colors = TerraneThemeExtras.colors
    var selectedIndex by remember(swatches) { mutableStateOf(0) }
    val activeSwatch = swatches.getOrElse(selectedIndex) { swatches.first() }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            swatches.forEachIndexed { index, swatch ->
                val isSelected = selectedIndex == index
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { selectedIndex = index }
                        .border(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) colors.primaryAccent else colors.hairline,
                            shape = RoundedCornerShape(12.dp),
                        )
                        .background(if (isSelected) colors.alabasterFill.copy(alpha = 0.55f) else colors.canvas)
                        .padding(8.dp)
                        .testTag("color_swatch_${swatch.hexCode}"),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(swatch.color)
                            .border(1.dp, colors.hairline, RoundedCornerShape(8.dp)),
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = swatch.surfaceRole,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSelected) colors.primaryAccent else colors.inkSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = swatch.hexCode,
                        style = MaterialTheme.typography.labelSmall.tabular(),
                        color = colors.inkMuted,
                        maxLines = 1,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = colors.elevatedSurface,
            border = BorderStroke(1.dp, colors.hairline),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(12.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(activeSwatch.color)
                        .border(1.dp, colors.hairline, CircleShape),
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${activeSwatch.name} · ${activeSwatch.hexCode}",
                        style = MaterialTheme.typography.labelLarge.tabular(),
                        color = colors.inkPrimary,
                    )
                    Text(
                        text = "${activeSwatch.surfaceRole} — ${activeSwatch.undertoneNote}",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.inkSecondary,
                    )
                }
            }
        }
    }
}

@Composable
fun EditorialDimensionSpecifier(
    dimensions: ProductDimensions,
    unit: DimensionUnit,
    onToggleUnit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = TerraneThemeExtras.colors
    val widthText = formatMetric(dimensions.widthCm, unit)
    val depthText = formatMetric(dimensions.depthCm, unit)
    val heightText = formatMetric(dimensions.heightCm, unit)
    val detailText = formatMetric(dimensions.detailValueCm, unit)

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = colors.elevatedSurface,
        border = BorderStroke(1.dp, colors.hairline),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column {
                    Text(
                        text = "ARCHITECTURAL PROPORTIONS",
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.secondaryLabel,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Technical Elevation",
                        style = MaterialTheme.typography.headlineSmall,
                        color = colors.inkPrimary,
                    )
                }

                // Unit switcher pill (cm / in)
                Surface(
                    onClick = onToggleUnit,
                    shape = CircleShape,
                    color = colors.cardSurface,
                    border = BorderStroke(1.dp, colors.hairline),
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("dimension_unit_toggle"),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    ) {
                        Text(
                            text = "CM",
                            style = MaterialTheme.typography.labelMedium,
                            color = if (unit == DimensionUnit.CM) colors.primaryAccent else colors.inkMuted,
                        )
                        Text(
                            text = " / ",
                            style = MaterialTheme.typography.labelMedium,
                            color = colors.inkMuted,
                        )
                        Text(
                            text = "IN",
                            style = MaterialTheme.typography.labelMedium,
                            color = if (unit == DimensionUnit.INCHES) colors.primaryAccent else colors.inkMuted,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Line-art technical elevation diagram
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(172.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(colors.canvas)
                    .border(1.dp, colors.hairline, RoundedCornerShape(12.dp)),
            ) {
                TechnicalElevationCanvas(
                    silhouetteType = dimensions.silhouetteType,
                    widthLabel = widthText,
                    heightLabel = heightText,
                    modifier = Modifier.fillMaxSize(),
                )
                Text(
                    text = "W $widthText",
                    style = MaterialTheme.typography.labelSmall.tabular(),
                    color = colors.primaryAccent,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 4.dp),
                )
                Text(
                    text = "H $heightText",
                    style = MaterialTheme.typography.labelSmall.tabular(),
                    color = colors.primaryAccent,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 8.dp),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = colors.hairline)
            Spacer(modifier = Modifier.height(12.dp))

            // Tabular specification metrics
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                DimensionMetricCell(
                    label = "WIDTH",
                    value = widthText,
                    modifier = Modifier.weight(1f),
                )
                DimensionMetricCell(
                    label = "DEPTH",
                    value = depthText,
                    modifier = Modifier.weight(1f),
                )
                DimensionMetricCell(
                    label = "HEIGHT",
                    value = heightText,
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                DimensionMetricCell(
                    label = dimensions.detailLabel.uppercase(),
                    value = detailText,
                    modifier = Modifier.weight(1f),
                )
                DimensionMetricCell(
                    label = "NET WEIGHT",
                    value = String.format(Locale.US, "%.1f kg", dimensions.weightKg),
                    modifier = Modifier.weight(1f),
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun DimensionMetricCell(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    val colors = TerraneThemeExtras.colors
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = colors.inkMuted,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.tabular(),
            color = colors.inkPrimary,
            fontWeight = FontWeight.Medium,
        )
    }
}
