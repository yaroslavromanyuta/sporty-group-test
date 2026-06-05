package com.sportygroup.weatherapp.feature.forecast.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme
import com.sportygroup.weatherapp.feature.forecast.presentation.preview.ForecastPreviewData
import com.sportygroup.weatherapp.feature.forecast.presentation.state.CitySearchUiAction
import com.sportygroup.weatherapp.feature.forecast.presentation.state.CitySearchUiState
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class CitySearchScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun results_areDisplayed() {
        composeRule.setContent {
            SkyTheme {
                CitySearchScreen(
                    state = CitySearchUiState(query = "Ma", results = ForecastPreviewData.searchResults),
                    onAction = {},
                    onBack = {},
                )
            }
        }

        composeRule.onNodeWithText("Malaga").assertIsDisplayed()
        composeRule.onNodeWithText("Manchester").assertIsDisplayed()
    }

    @Test
    fun emptyState_isShownForNoMatches() {
        composeRule.setContent {
            SkyTheme {
                CitySearchScreen(
                    state = CitySearchUiState(query = "Zzzzz", results = emptyList()),
                    onAction = {},
                    onBack = {},
                )
            }
        }

        composeRule.onNodeWithText("No matches for “Zzzzz”").assertIsDisplayed()
    }

    @Test
    fun typingQuery_emitsQueryChangedAction() {
        var lastQuery: String? = null
        composeRule.setContent {
            SkyTheme {
                CitySearchScreen(
                    state = CitySearchUiState(),
                    onAction = { if (it is CitySearchUiAction.OnQueryChanged) lastQuery = it.query },
                    onBack = {},
                )
            }
        }

        composeRule.onNodeWithText("Search city or airport").performTextInput("Lis")
        assertEquals("Lis", lastQuery)
    }
}