package com.example.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.DimensionUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.terraneDataStore by preferencesDataStore(name = "terrane_studio_prefs")

class PreferencesRepository(private val context: Context) {

    private object Keys {
        val SAVED_IDS = stringSetPreferencesKey("saved_product_ids")
        val SAVED_PALETTE_IDS = stringSetPreferencesKey("saved_palette_ids")
        val DIMENSION_UNIT = stringPreferencesKey("dimension_unit")
    }

    val savedProductIds: Flow<Set<String>> = context.terraneDataStore.data.map { prefs ->
        prefs[Keys.SAVED_IDS] ?: setOf("soraia_lounge", "toscana_plinth", "alba_amphora")
    }

    val savedPaletteIds: Flow<Set<String>> = context.terraneDataStore.data.map { prefs ->
        prefs[Keys.SAVED_PALETTE_IDS] ?: setOf("palette_travertine_salon")
    }

    val dimensionUnit: Flow<DimensionUnit> = context.terraneDataStore.data.map { prefs ->
        runCatching {
            DimensionUnit.valueOf(prefs[Keys.DIMENSION_UNIT] ?: DimensionUnit.CM.name)
        }.getOrDefault(DimensionUnit.CM)
    }

    suspend fun toggleSaved(productId: String) {
        context.terraneDataStore.edit { prefs ->
            val current = (prefs[Keys.SAVED_IDS] ?: setOf("soraia_lounge", "toscana_plinth", "alba_amphora")).toMutableSet()
            if (!current.add(productId)) {
                current.remove(productId)
            }
            prefs[Keys.SAVED_IDS] = current
        }
    }

    suspend fun toggleSavedPalette(paletteId: String) {
        context.terraneDataStore.edit { prefs ->
            val current = (prefs[Keys.SAVED_PALETTE_IDS] ?: setOf("palette_travertine_salon")).toMutableSet()
            if (!current.add(paletteId)) {
                current.remove(paletteId)
            }
            prefs[Keys.SAVED_PALETTE_IDS] = current
        }
    }

    suspend fun setDimensionUnit(unit: DimensionUnit) {
        context.terraneDataStore.edit { prefs ->
            prefs[Keys.DIMENSION_UNIT] = unit.name
        }
    }
}
