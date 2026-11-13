package com.example

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.model.MaterialType
import com.example.ui.components.InteractiveMaterialSwatch
import com.example.ui.theme.TerraneTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class InteractiveMaterialSwatchTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `tapping InteractiveMaterialSwatch reveals technical metadata`() {
        composeTestRule.setContent {
            TerraneTheme {
                InteractiveMaterialSwatch(
                    materialType = MaterialType.BOUCLE,
                    finishName = "Oatmeal Loop Bouclé",
                )
            }
        }

        composeTestRule
            .onNodeWithTag("swatch_metadata_card_BOUCLE")
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithTag("swatch_sample_button_BOUCLE")
            .performClick()

        composeTestRule
            .onNodeWithTag("swatch_metadata_card_BOUCLE")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Oatmeal Loop Bouclé")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("45,000 Martindale")
            .assertIsDisplayed()
    }
}
