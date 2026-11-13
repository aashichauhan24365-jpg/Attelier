package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CatalogRepository
import com.example.data.PreferencesRepository
import com.example.model.DimensionUnit
import com.example.model.MaterialType
import com.example.model.Product
import com.example.model.ProductCategory
import com.example.model.RoomColorPalette
import com.example.model.RoomType
import com.example.model.RoomVignette
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class TerraneUiState(
    val allProducts: List<Product> = CatalogRepository.products,
    val vignettes: List<RoomVignette> = CatalogRepository.vignettes,
    val palettes: List<RoomColorPalette> = CatalogRepository.palettes,
    val searchQuery: String = "",
    val selectedCategory: ProductCategory = ProductCategory.ALL,
    val selectedRoomFilter: RoomType = RoomType.ALL,
    val selectedMaterialFilter: MaterialType? = null,
    val savedProductIds: Set<String> = emptySet(),
    val savedPaletteIds: Set<String> = emptySet(),
    val dimensionUnit: DimensionUnit = DimensionUnit.CM,
    val quickViewProduct: Product? = null,
    val activeVignetteSheet: RoomVignette? = null,
    val atelierNotesVisible: Boolean = false,
    val snackbarMessage: String? = null,
) {
    val filteredProducts: List<Product>
        get() {
            val q = searchQuery.trim().lowercase()
            return allProducts.filter { product ->
                val matchesCategory =
                    selectedCategory == ProductCategory.ALL || product.category == selectedCategory
                val matchesRoom =
                    selectedRoomFilter == RoomType.ALL || product.roomType == selectedRoomFilter
                val matchesMaterial =
                    selectedMaterialFilter == null ||
                        product.variants.any { it.materialType == selectedMaterialFilter }
                val matchesQuery = q.isEmpty() ||
                    product.title.lowercase().contains(q) ||
                    product.subtitle.lowercase().contains(q) ||
                    product.roomType.displayName.lowercase().contains(q) ||
                    product.stylingTip.lowercase().contains(q) ||
                    product.description.lowercase().contains(q) ||
                    product.suggestedColors.any { it.name.lowercase().contains(q) } ||
                    product.variants.any {
                        it.finishName.lowercase().contains(q) ||
                            it.materialType.displayName.lowercase().contains(q)
                    }
                matchesCategory && matchesRoom && matchesMaterial && matchesQuery
            }
        }

    val filteredPalettes: List<RoomColorPalette>
        get() = palettes.filter { palette ->
            selectedRoomFilter == RoomType.ALL || palette.roomType == selectedRoomFilter
        }

    val savedProducts: List<Product>
        get() = allProducts.filter { it.id in savedProductIds }

    val savedPalettes: List<RoomColorPalette>
        get() = palettes.filter { it.id in savedPaletteIds }

    val moodboardCount: Int
        get() = savedProductIds.size + savedPaletteIds.size
}

private data class FilterState(
    val query: String = "",
    val category: ProductCategory = ProductCategory.ALL,
    val room: RoomType = RoomType.ALL,
    val material: MaterialType? = null,
)

private data class ModalState(
    val quickViewProduct: Product? = null,
    val activeVignetteSheet: RoomVignette? = null,
    val atelierNotesVisible: Boolean = false,
    val snackbarMessage: String? = null,
)

class TerraneViewModel(application: Application) : AndroidViewModel(application) {
    private val preferences = PreferencesRepository(application.applicationContext)

    private val filterState = MutableStateFlow(FilterState())
    private val modalState = MutableStateFlow(ModalState())

    val uiState: StateFlow<TerraneUiState> = combine(
        filterState,
        modalState,
        preferences.savedProductIds,
        preferences.savedPaletteIds,
        preferences.dimensionUnit,
    ) { filters, modals, savedIds, savedPaletteIds, dimUnit ->
        TerraneUiState(
            allProducts = CatalogRepository.products,
            vignettes = CatalogRepository.vignettes,
            palettes = CatalogRepository.palettes,
            searchQuery = filters.query,
            selectedCategory = filters.category,
            selectedRoomFilter = filters.room,
            selectedMaterialFilter = filters.material,
            savedProductIds = savedIds,
            savedPaletteIds = savedPaletteIds,
            dimensionUnit = dimUnit,
            quickViewProduct = modals.quickViewProduct,
            activeVignetteSheet = modals.activeVignetteSheet,
            atelierNotesVisible = modals.atelierNotesVisible,
            snackbarMessage = modals.snackbarMessage,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TerraneUiState(),
    )

    fun updateSearchQuery(query: String) {
        filterState.value = filterState.value.copy(query = query)
    }

    fun selectCategory(category: ProductCategory) {
        filterState.value = filterState.value.copy(category = category)
    }

    fun selectRoomFilter(room: RoomType) {
        filterState.value = filterState.value.copy(room = room)
    }

    fun toggleMaterialFilter(material: MaterialType) {
        val current = filterState.value.material
        filterState.value = filterState.value.copy(
            material = if (current == material) null else material,
        )
    }

    fun resetFilters() {
        filterState.value = FilterState()
    }

    fun toggleSaved(productId: String) {
        val wasSaved = productId in uiState.value.savedProductIds
        val productTitle = CatalogRepository.findProduct(productId)?.title ?: "Piece"
        viewModelScope.launch {
            preferences.toggleSaved(productId)
            modalState.value = modalState.value.copy(
                snackbarMessage = if (wasSaved) {
                    "Removed $productTitle from Moodboard"
                } else {
                    "Saved $productTitle to Moodboard"
                },
            )
        }
    }

    fun toggleSavedPalette(paletteId: String) {
        val wasSaved = paletteId in uiState.value.savedPaletteIds
        val paletteTitle = CatalogRepository.palettes.find { it.id == paletteId }?.title ?: "Palette"
        viewModelScope.launch {
            preferences.toggleSavedPalette(paletteId)
            modalState.value = modalState.value.copy(
                snackbarMessage = if (wasSaved) {
                    "Removed $paletteTitle from Moodboard"
                } else {
                    "Saved $paletteTitle to Moodboard"
                },
            )
        }
    }

    fun toggleDimensionUnit() {
        viewModelScope.launch {
            val next = if (uiState.value.dimensionUnit == DimensionUnit.CM) {
                DimensionUnit.INCHES
            } else {
                DimensionUnit.CM
            }
            preferences.setDimensionUnit(next)
        }
    }

    fun openQuickView(product: Product) {
        modalState.value = modalState.value.copy(quickViewProduct = product)
    }

    fun dismissQuickView() {
        modalState.value = modalState.value.copy(quickViewProduct = null)
    }

    fun openVignetteSheet(vignette: RoomVignette) {
        modalState.value = modalState.value.copy(activeVignetteSheet = vignette)
    }

    fun dismissVignetteSheet() {
        modalState.value = modalState.value.copy(activeVignetteSheet = null)
    }

    fun showAtelierNotes(visible: Boolean) {
        modalState.value = modalState.value.copy(atelierNotesVisible = visible)
    }

    fun clearSnackbar() {
        modalState.value = modalState.value.copy(snackbarMessage = null)
    }
}
