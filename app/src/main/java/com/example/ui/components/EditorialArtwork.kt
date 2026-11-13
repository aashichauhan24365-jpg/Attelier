package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.model.MaterialType
import com.example.model.SilhouetteType
import com.example.ui.theme.TerraneThemeExtras

@Composable
fun ArchitecturalPhoto(
    imageUrl: String,
    contentDescription: String,
    silhouetteType: SilhouetteType,
    accentColor: Color,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
) {
    val colors = TerraneThemeExtras.colors
    val context = LocalContext.current

    Box(modifier = modifier.background(colors.elevatedSurface)) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageUrl)
                .crossfade(350)
                .build(),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = Modifier.fillMaxSize(),
            loading = {
                ArchitecturalEditorialBackdrop(
                    silhouetteType = silhouetteType,
                    materialAccent = accentColor,
                    modifier = Modifier.fillMaxSize(),
                )
            },
            error = {
                ArchitecturalEditorialBackdrop(
                    silhouetteType = silhouetteType,
                    materialAccent = accentColor,
                    modifier = Modifier.fillMaxSize(),
                )
            },
        )
    }
}

@Composable
fun ArchitecturalEditorialBackdrop(
    silhouetteType: SilhouetteType,
    materialAccent: Color,
    modifier: Modifier = Modifier,
) {
    val colors = TerraneThemeExtras.colors
    val bgTop = colors.elevatedSurface
    val bgBottom = colors.hairline.copy(alpha = 0.65f)
    val ink = colors.inkPrimary
    val terracotta = colors.primaryAccent
    val warmGlaze = colors.alabasterFill

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        drawRect(
            brush = Brush.verticalGradient(listOf(bgTop, bgBottom)),
            size = size,
        )

        // Warm sunlit arch backdrop
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    warmGlaze.copy(alpha = 0.9f),
                    Color.Transparent,
                ),
                center = Offset(w * 0.52f, h * 0.36f),
                radius = w * 0.45f,
            ),
            radius = w * 0.45f,
            center = Offset(w * 0.52f, h * 0.36f),
        )

        val archPath = Path().apply {
            val archW = w * 0.56f
            val archLeft = (w - archW) / 2f
            val archTop = h * 0.14f
            val archBottom = h * 0.82f
            moveTo(archLeft, archBottom)
            lineTo(archLeft, archTop + archW / 2f)
            cubicTo(
                archLeft,
                archTop,
                archLeft + archW,
                archTop,
                archLeft + archW,
                archTop + archW / 2f,
            )
            lineTo(archLeft + archW, archBottom)
        }
        drawPath(
            path = archPath,
            color = colors.hairline,
            style = Stroke(width = 1.5.dp.toPx()),
        )

        // Floor datum line
        drawLine(
            color = ink.copy(alpha = 0.22f),
            start = Offset(w * 0.14f, h * 0.82f),
            end = Offset(w * 0.86f, h * 0.82f),
            strokeWidth = 1.dp.toPx(),
        )

        drawObjectVector(
            silhouetteType = silhouetteType,
            fill = materialAccent.copy(alpha = 0.72f),
            stroke = ink.copy(alpha = 0.78f),
            accent = terracotta,
            left = w * 0.24f,
            top = h * 0.28f,
            width = w * 0.52f,
            height = h * 0.52f,
            strokePx = 1.6.dp.toPx(),
        )
    }
}

@Composable
fun TechnicalElevationCanvas(
    silhouetteType: SilhouetteType,
    widthLabel: String,
    heightLabel: String,
    modifier: Modifier = Modifier,
) {
    val colors = TerraneThemeExtras.colors
    val strokeColor = colors.inkPrimary
    val accentColor = colors.primaryAccent
    val guideColor = colors.inkMuted.copy(alpha = 0.55f)

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val padX = w * 0.16f
        val padTop = h * 0.14f
        val padBottom = h * 0.22f
        val drawW = w - padX * 2f
        val drawH = h - padTop - padBottom

        val dashEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)

        // Datum floor & side projection lines
        drawLine(
            color = guideColor,
            start = Offset(padX * 0.5f, h - padBottom),
            end = Offset(w - padX * 0.5f, h - padBottom),
            strokeWidth = 1.dp.toPx(),
            pathEffect = dashEffect,
        )

        // Object crisp technical line-art
        drawObjectVector(
            silhouetteType = silhouetteType,
            fill = colors.alabasterFill.copy(alpha = 0.45f),
            stroke = strokeColor,
            accent = accentColor,
            left = padX,
            top = padTop,
            width = drawW,
            height = drawH,
            strokePx = 1.5.dp.toPx(),
        )

        // Bottom width dimension line
        val dimY = h - padBottom * 0.42f
        drawLine(
            color = accentColor,
            start = Offset(padX, dimY),
            end = Offset(padX + drawW, dimY),
            strokeWidth = 1.25.dp.toPx(),
        )
        drawLine(
            color = accentColor,
            start = Offset(padX, dimY - 5.dp.toPx()),
            end = Offset(padX, dimY + 5.dp.toPx()),
            strokeWidth = 1.25.dp.toPx(),
        )
        drawLine(
            color = accentColor,
            start = Offset(padX + drawW, dimY - 5.dp.toPx()),
            end = Offset(padX + drawW, dimY + 5.dp.toPx()),
            strokeWidth = 1.25.dp.toPx(),
        )

        // Right height dimension line
        val dimX = w - padX * 0.48f
        drawLine(
            color = accentColor,
            start = Offset(dimX, padTop),
            end = Offset(dimX, padTop + drawH),
            strokeWidth = 1.25.dp.toPx(),
        )
        drawLine(
            color = accentColor,
            start = Offset(dimX - 5.dp.toPx(), padTop),
            end = Offset(dimX + 5.dp.toPx(), padTop),
            strokeWidth = 1.25.dp.toPx(),
        )
        drawLine(
            color = accentColor,
            start = Offset(dimX - 5.dp.toPx(), padTop + drawH),
            end = Offset(dimX + 5.dp.toPx(), padTop + drawH),
            strokeWidth = 1.25.dp.toPx(),
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawObjectVector(
    silhouetteType: SilhouetteType,
    fill: Color,
    stroke: Color,
    accent: Color,
    left: Float,
    top: Float,
    width: Float,
    height: Float,
    strokePx: Float,
) {
    val lineStroke = Stroke(width = strokePx, cap = StrokeCap.Round, join = StrokeJoin.Round)
    val bottom = top + height
    val right = left + width
    val cx = left + width / 2f

    when (silhouetteType) {
        SilhouetteType.LOUNGE_CHAIR -> {
            // Sculptural curved backrest & cushioned seat
            val backPath = Path().apply {
                moveTo(left + width * 0.16f, top + height * 0.56f)
                cubicTo(
                    left + width * 0.10f,
                    top + height * 0.12f,
                    right - width * 0.10f,
                    top + height * 0.12f,
                    right - width * 0.16f,
                    top + height * 0.56f,
                )
                close()
            }
            drawPath(backPath, color = fill)
            drawPath(backPath, color = stroke, style = lineStroke)

            drawRoundRect(
                color = fill,
                topLeft = Offset(left + width * 0.12f, top + height * 0.54f),
                size = Size(width * 0.76f, height * 0.24f),
                cornerRadius = CornerRadius(14f, 14f),
            )
            drawRoundRect(
                color = stroke,
                topLeft = Offset(left + width * 0.12f, top + height * 0.54f),
                size = Size(width * 0.76f, height * 0.24f),
                cornerRadius = CornerRadius(14f, 14f),
                style = lineStroke,
            )
            // Canted walnut feet
            drawLine(stroke, Offset(left + width * 0.24f, top + height * 0.78f), Offset(left + width * 0.18f, bottom), strokePx)
            drawLine(stroke, Offset(right - width * 0.24f, top + height * 0.78f), Offset(right - width * 0.18f, bottom), strokePx)
        }

        SilhouetteType.SOFA -> {
            // Low wide modular silhouette
            drawRoundRect(
                color = fill,
                topLeft = Offset(left + width * 0.06f, top + height * 0.34f),
                size = Size(width * 0.88f, height * 0.34f),
                cornerRadius = CornerRadius(16f, 16f),
            )
            drawRoundRect(
                color = stroke,
                topLeft = Offset(left + width * 0.06f, top + height * 0.34f),
                size = Size(width * 0.88f, height * 0.34f),
                cornerRadius = CornerRadius(16f, 16f),
                style = lineStroke,
            )
            // Seat base plinth
            drawRoundRect(
                color = fill,
                topLeft = Offset(left + width * 0.03f, top + height * 0.56f),
                size = Size(width * 0.94f, height * 0.30f),
                cornerRadius = CornerRadius(12f, 12f),
            )
            drawRoundRect(
                color = stroke,
                topLeft = Offset(left + width * 0.03f, top + height * 0.56f),
                size = Size(width * 0.94f, height * 0.30f),
                cornerRadius = CornerRadius(12f, 12f),
                style = lineStroke,
            )
            // Cushion seam
            drawLine(stroke.copy(alpha = 0.45f), Offset(cx, top + height * 0.36f), Offset(cx, top + height * 0.84f), strokePx * 0.8f)
            drawLine(stroke, Offset(left + width * 0.14f, top + height * 0.86f), Offset(left + width * 0.14f, bottom), strokePx)
            drawLine(stroke, Offset(right - width * 0.14f, top + height * 0.86f), Offset(right - width * 0.14f, bottom), strokePx)
        }

        SilhouetteType.PLINTH_TABLE -> {
            // Low monolithic travertine slab + twin plinth legs
            drawRoundRect(
                color = fill,
                topLeft = Offset(left + width * 0.05f, top + height * 0.44f),
                size = Size(width * 0.90f, height * 0.16f),
                cornerRadius = CornerRadius(6f, 6f),
            )
            drawRoundRect(
                color = stroke,
                topLeft = Offset(left + width * 0.05f, top + height * 0.44f),
                size = Size(width * 0.90f, height * 0.16f),
                cornerRadius = CornerRadius(6f, 6f),
                style = lineStroke,
            )
            drawRect(
                color = fill,
                topLeft = Offset(left + width * 0.18f, top + height * 0.60f),
                size = Size(width * 0.18f, height * 0.40f),
            )
            drawRect(
                color = stroke,
                topLeft = Offset(left + width * 0.18f, top + height * 0.60f),
                size = Size(width * 0.18f, height * 0.40f),
                style = lineStroke,
            )
            drawRect(
                color = fill,
                topLeft = Offset(right - width * 0.36f, top + height * 0.60f),
                size = Size(width * 0.18f, height * 0.40f),
            )
            drawRect(
                color = stroke,
                topLeft = Offset(right - width * 0.36f, top + height * 0.60f),
                size = Size(width * 0.18f, height * 0.40f),
                style = lineStroke,
            )
        }

        SilhouetteType.DINING_TABLE -> {
            drawRoundRect(
                color = fill,
                topLeft = Offset(left + width * 0.04f, top + height * 0.26f),
                size = Size(width * 0.92f, height * 0.12f),
                cornerRadius = CornerRadius(6f, 6f),
            )
            drawRoundRect(
                color = stroke,
                topLeft = Offset(left + width * 0.04f, top + height * 0.26f),
                size = Size(width * 0.92f, height * 0.12f),
                cornerRadius = CornerRadius(6f, 6f),
                style = lineStroke,
            )
            // Twin fluted columns
            listOf(left + width * 0.24f, right - width * 0.34f).forEach { colLeft ->
                drawRect(
                    color = fill,
                    topLeft = Offset(colLeft, top + height * 0.38f),
                    size = Size(width * 0.10f, height * 0.62f),
                )
                drawRect(
                    color = stroke,
                    topLeft = Offset(colLeft, top + height * 0.38f),
                    size = Size(width * 0.10f, height * 0.62f),
                    style = lineStroke,
                )
            }
        }

        SilhouetteType.PENDANT_LAMP -> {
            // Stem + sculptural dome shade + warm glow disc
            drawLine(stroke, Offset(cx, top), Offset(cx, top + height * 0.34f), strokePx)
            val dome = Path().apply {
                moveTo(left + width * 0.22f, top + height * 0.64f)
                cubicTo(
                    left + width * 0.24f,
                    top + height * 0.30f,
                    right - width * 0.24f,
                    top + height * 0.30f,
                    right - width * 0.22f,
                    top + height * 0.64f,
                )
                close()
            }
            drawPath(dome, color = fill)
            drawPath(dome, color = stroke, style = lineStroke)
            drawCircle(
                color = accent.copy(alpha = 0.25f),
                radius = width * 0.14f,
                center = Offset(cx, top + height * 0.72f),
            )
            drawLine(stroke, Offset(cx, top + height * 0.64f), Offset(cx, bottom), strokePx * 0.9f)
            drawLine(stroke, Offset(left + width * 0.34f, bottom), Offset(right - width * 0.34f, bottom), strokePx)
        }

        SilhouetteType.VESSEL -> {
            val vessel = Path().apply {
                moveTo(cx - width * 0.14f, top + height * 0.14f)
                lineTo(cx + width * 0.14f, top + height * 0.14f)
                lineTo(cx + width * 0.10f, top + height * 0.30f)
                cubicTo(
                    cx + width * 0.34f,
                    top + height * 0.44f,
                    cx + width * 0.32f,
                    top + height * 0.80f,
                    cx + width * 0.16f,
                    bottom,
                )
                lineTo(cx - width * 0.16f, bottom)
                cubicTo(
                    cx - width * 0.32f,
                    top + height * 0.80f,
                    cx - width * 0.34f,
                    top + height * 0.44f,
                    cx - width * 0.10f,
                    top + height * 0.30f,
                )
                close()
            }
            drawPath(vessel, color = fill)
            drawPath(vessel, color = stroke, style = lineStroke)
            // Subtle clay throwing rib
            drawLine(
                color = accent.copy(alpha = 0.45f),
                start = Offset(cx - width * 0.22f, top + height * 0.56f),
                end = Offset(cx + width * 0.22f, top + height * 0.56f),
                strokeWidth = strokePx * 0.7f,
            )
        }

        SilhouetteType.TEXTILE_THROW -> {
            // Layered folded linen planes
            drawRoundRect(
                color = fill,
                topLeft = Offset(left + width * 0.16f, top + height * 0.24f),
                size = Size(width * 0.68f, height * 0.56f),
                cornerRadius = CornerRadius(14f, 14f),
            )
            drawRoundRect(
                color = stroke,
                topLeft = Offset(left + width * 0.16f, top + height * 0.24f),
                size = Size(width * 0.68f, height * 0.56f),
                cornerRadius = CornerRadius(14f, 14f),
                style = lineStroke,
            )
            // Folded drape overlay
            drawRoundRect(
                color = accent.copy(alpha = 0.22f),
                topLeft = Offset(left + width * 0.24f, top + height * 0.42f),
                size = Size(width * 0.52f, height * 0.46f),
                cornerRadius = CornerRadius(10f, 10f),
            )
            drawRoundRect(
                color = stroke,
                topLeft = Offset(left + width * 0.24f, top + height * 0.42f),
                size = Size(width * 0.52f, height * 0.46f),
                cornerRadius = CornerRadius(10f, 10f),
                style = lineStroke,
            )
        }
    }
}

fun materialAccentColor(materialType: MaterialType?): Color =
    materialType?.swatchColor ?: Color(0xFFE7DDD0)
