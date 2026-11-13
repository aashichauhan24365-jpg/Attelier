package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chair
import androidx.compose.material.icons.outlined.MeetingRoom
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.model.MaterialType
import com.example.model.Product
import com.example.model.ProductCategory
import com.example.model.RoomType
import com.example.ui.components.FilterChipPill
import com.example.ui.components.GhostPillButton
import com.example.ui.components.MaterialSwatchCircle
import com.example.ui.components.PillSearchField
import com.example.ui.components.TerraneProductCard
import com.example.ui.theme.TerraneThemeExtras
import com.example.ui.theme.tabular
import com.example.viewmodel.TerraneUiState

@Composable
fun CatalogScreen(
    uiState: TerraneUiState,
    columns: Int,
    onSearchQueryChange: (String) -> Unit,
    onSelectCategory: (ProductCategory) -> Unit,
    onSelectRoomFilter: (RoomType) -> Unit,
    onToggleMaterialFilter: (MaterialType) -> Unit,
    onResetFilters: () -> Unit,
    onOpenProduct: (String) -> Unit,
    onQuickView: (Product) -> Unit,
    onToggleSave: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = TerraneThemeExtras.colors
    val products = uiState.filteredProducts
    val rows = products.chunked(columns)
    var roomMenuExpanded by remember { mutableStateOf(false) }

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
            // Header
            item {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 18.dp),
                ) {
                    Column {
                        Text(
                            text = "DECOR & STYLING ARCHIVE",
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.secondaryLabel,
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Decor Index",
                            style = MaterialTheme.typography.displayMedium,
                            color = colors.inkPrimary,
                        )
                    }

                    Text(
                        text = "${products.size} studies",
                        style = MaterialTheme.typography.labelMedium.tabular(),
                        color = colors.inkSecondary,
                        modifier = Modifier.padding(bottom = 6.dp),
                    )
                }
            }

            // Pill Search Input
            item {
                PillSearchField(
                    query = uiState.searchQuery,
                    onQueryChange = onSearchQueryChange,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Category Filter Chips
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(ProductCategory.entries) { category ->
                        FilterChipPill(
                            label = category.displayName,
                            selected = uiState.selectedCategory == category,
                            onClick = { onSelectCategory(category) },
                            modifier = Modifier.testTag("category_chip_${category.name}"),
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Material Swatch Tones + Room Filter Row
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        MaterialType.entries.forEach { material ->
                            MaterialSwatchCircle(
                                materialType = material,
                                selected = uiState.selectedMaterialFilter == material,
                                onClick = { onToggleMaterialFilter(material) },
                                size = 22.dp,
                                modifier = Modifier.testTag("material_filter_${material.name}"),
                            )
                        }
                        if (uiState.selectedMaterialFilter != null) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = uiState.selectedMaterialFilter.displayName,
                                style = MaterialTheme.typography.labelMedium,
                                color = colors.primaryAccent,
                            )
                        }
                    }

                    Box {
                        Surface(
                            onClick = { roomMenuExpanded = true },
                            shape = CircleShape,
                            color = colors.elevatedSurface,
                            border = BorderStroke(1.dp, colors.hairline),
                            modifier = Modifier.testTag("room_filter_dropdown_button"),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.MeetingRoom,
                                    contentDescription = "Filter by room",
                                    tint = colors.inkPrimary,
                                    modifier = Modifier.size(16.dp),
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = uiState.selectedRoomFilter.displayName,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = colors.inkPrimary,
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = roomMenuExpanded,
                            onDismissRequest = { roomMenuExpanded = false },
                            containerColor = colors.cardSurface,
                        ) {
                            RoomType.entries.forEach { room ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = room.displayName,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = if (room == uiState.selectedRoomFilter) colors.primaryAccent else colors.inkPrimary,
                                        )
                                    },
                                    onClick = {
                                        onSelectRoomFilter(room)
                                        roomMenuExpanded = false
                                    },
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(
                    color = colors.hairline,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (products.isEmpty()) {
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp, vertical = 64.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Chair,
                            contentDescription = null,
                            tint = colors.inkMuted,
                            modifier = Modifier.size(44.dp),
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "No matching decor studies",
                            style = MaterialTheme.typography.headlineSmall,
                            color = colors.inkPrimary,
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Try clearing a material tone or switching rooms to browse all interior suggestions.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.inkSecondary,
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        GhostPillButton(
                            text = "Reset Filters",
                            onClick = onResetFilters,
                            modifier = Modifier.testTag("reset_filters_button"),
                        )
                    }
                }
            } else {
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
            }
        }
    }
}
