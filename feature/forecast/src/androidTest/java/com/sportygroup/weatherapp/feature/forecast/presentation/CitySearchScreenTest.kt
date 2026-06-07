package com.sportygroup.weatherapp.feature.forecast.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme
import com.sportygroup.weatherapp.feature.forecast.presentation.preview.ForecastPreviewData
import com.sportygroup.weatherapp.feature.forecast.presentation.state.CitySearchUiAction
import com.sportygroup.weatherapp.feature.forecast.presentation.state.CitySearchUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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

        composeRule.onNodeWithContentDescription("Search city or airport").performTextInput("Lis")
        assertEquals("Lis", lastQuery)
    }

    @Test
    fun selectingResult_emitsCitySelectedAction() {
        var selected: CitySearchUiAction.OnCitySelected? = null
        composeRule.setContent {
            SkyTheme {
                CitySearchScreen(
                    state = CitySearchUiState(query = "Ma", results = ForecastPreviewData.searchResults),
                    onAction = { if (it is CitySearchUiAction.OnCitySelected) selected = it },
                    onBack = {},
                )
            }
        }

        composeRule.onNodeWithText("Manchester").performClick()

        assertEquals("Manchester", selected?.city?.name)
    }

    @Test
    fun suggestions_showRecentAndUseCurrentLocation() {
        var useCurrentLocation = false
        composeRule.setContent {
            SkyTheme {
                CitySearchScreen(
                    state = CitySearchUiState(recent = ForecastPreviewData.recent),
                    onAction = {
                        if (it is CitySearchUiAction.OnUseCurrentLocation) useCurrentLocation = true
                    },
                    onBack = {},
                )
            }
        }

        composeRule.onNodeWithText("Recent", ignoreCase = true).assertIsDisplayed()
        composeRule.onNodeWithText("Use current location").assertIsDisplayed()
        composeRule.onNodeWithText("Use current location").performClick()
        assertTrue(useCurrentLocation)
    }

    @Test
    fun selectingRecentCity_emitsCitySelectedAction() {
        var selected: CitySearchUiAction.OnCitySelected? = null
        composeRule.setContent {
            SkyTheme {
                CitySearchScreen(
                    state = CitySearchUiState(recent = ForecastPreviewData.recent),
                    onAction = { if (it is CitySearchUiAction.OnCitySelected) selected = it },
                    onBack = {},
                )
            }
        }

        composeRule.onNodeWithText("Lisbon").performClick()
        assertEquals("Lisbon", selected?.city?.name)
    }

    @Test
    fun clearButton_emitsClearQueryAction() {
        var cleared = false
        composeRule.setContent {
            SkyTheme {
                CitySearchScreen(
                    state = CitySearchUiState(query = "Malaga", results = ForecastPreviewData.searchResults),
                    onAction = { if (it is CitySearchUiAction.OnClearQuery) cleared = true },
                    onBack = {},
                )
            }
        }

        composeRule.onNodeWithContentDescription("Clear search").performClick()
        assertTrue(cleared)
    }

    @Test
    fun backButton_invokesOnBack() {
        var back = false
        composeRule.setContent {
            SkyTheme {
                CitySearchScreen(
                    state = CitySearchUiState(),
                    onAction = {},
                    onBack = { back = true },
                )
            }
        }

        composeRule.onNodeWithContentDescription("Back").performClick()
        assertTrue(back)
    }
}