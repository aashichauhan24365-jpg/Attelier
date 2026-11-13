package com.example

import com.example.data.CatalogRepository
import com.example.model.DimensionUnit
import com.example.model.MaterialType
import com.example.model.ProductCategory
import com.example.model.RoomType
import com.example.ui.components.formatMetric
import com.example.viewmodel.TerraneUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CatalogLogicTest {

    @Test
    fun `decor index filters by category, room type, and material swatch`() {
        val state = TerraneUiState(
            allProducts = CatalogRepository.products,
            selectedCategory = ProductCategory.SEATING,
            selectedRoomFilter = RoomType.LIVING,
            selectedMaterialFilter = MaterialType.BOUCLE,
        )
        val results = state.filteredProducts
        assertTrue(results.isNotEmpty())
        assertTrue(results.all { it.category == ProductCategory.SEATING })
        assertTrue(results.all { it.roomType == RoomType.LIVING })
        assertTrue(results.all { p -> p.variants.any { it.materialType == MaterialType.BOUCLE } })
    }

    @Test
    fun `decor index search matches color palette names and styling tips`() {
        val state = TerraneUiState(
            allProducts = CatalogRepository.products,
            searchQuery = "apricot",
        )
        val results = state.filteredProducts
        assertTrue(results.isNotEmpty())
        assertTrue(
            results.all { product ->
                product.suggestedColors.any { it.name.contains("apricot", ignoreCase = true) } ||
                    product.title.contains("apricot", ignoreCase = true) ||
                    product.description.contains("apricot", ignoreCase = true) ||
                    product.stylingTip.contains("apricot", ignoreCase = true)
            },
        )
    }

    @Test
    fun `room color palettes filter cleanly by room type`() {
        val studyPalettes = TerraneUiState(
            palettes = CatalogRepository.palettes,
            selectedRoomFilter = RoomType.STUDY,
        ).filteredPalettes

        assertTrue(studyPalettes.isNotEmpty())
        assertTrue(studyPalettes.all { it.roomType == RoomType.STUDY })
        assertTrue(studyPalettes.all { it.colors.size >= 4 })
    }

    @Test
    fun `all lookbook decor pieces and vignettes include color guidance`() {
        assertTrue(CatalogRepository.products.all { it.suggestedColors.isNotEmpty() && it.stylingTip.isNotBlank() })
        assertTrue(CatalogRepository.vignettes.all { it.palette.isNotEmpty() && it.decorAdvice.isNotBlank() })
    }

    @Test
    fun `metric formatter produces clean tabular strings`() {
        assertEquals("84 cm", formatMetric(84f, DimensionUnit.CM))
        assertEquals("33.1 in", formatMetric(84f, DimensionUnit.INCHES))
    }
}
