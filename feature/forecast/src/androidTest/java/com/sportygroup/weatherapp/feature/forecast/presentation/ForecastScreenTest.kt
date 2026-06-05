package com.sportygroup.weatherapp.feature.forecast.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme
import com.sportygroup.weatherapp.feature.forecast.presentation.mapper.ErrorMessage
import com.sportygroup.weatherapp.feature.forecast.presentation.preview.ForecastPreviewData
import com.sportygroup.weatherapp.feature.forecast.presentation.state.ForecastUiAction
import com.sportygroup.weatherapp.feature.forecast.presentation.state.ForecastUiState
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ForecastScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun content_showsCityAndSections() {
        composeRule.setContent {
            SkyTheme {
                ForecastScreen(
                    state = ForecastUiState.Content(ForecastPreviewData.forecast),
                    onAction = {},
                    onUseCurrentLocation = {},
                    onOpenAppSettings = {},
                    onOpenSearch = {},
                    onOpenSettings = {},
                )
            }
        }

        composeRule.onNodeWithText("Malaga").assertIsDisplayed()
        composeRule.onNodeWithText("Partly cloudy").assertIsDisplayed()
        composeRule.onNodeWithText("HOURLY").assertIsDisplayed()
    }

    @Test
    fun content_settingsButtonInvokesCallback() {
        var openedSettings = false
        composeRule.setContent {
            SkyTheme {
                ForecastScreen(
                    state = ForecastUiState.Content(ForecastPreviewData.forecast),
                    onAction = {},
                    onUseCurrentLocation = {},
                    onOpenAppSettings = {},
                    onOpenSearch = {},
                    onOpenSettings = { openedSettings = true },
                )
            }
        }

        composeRule.onNodeWithContentDescription("Settings").performClick()
        assertTrue(openedSettings)
    }

    @Test
    fun initialChoice_showsBothChoices() {
        composeRule.setContent {
            SkyTheme {
                ForecastScreen(
                    state = ForecastUiState.InitialChoice(),
                    onAction = {},
                    onUseCurrentLocation = {},
                    onOpenAppSettings = {},
                    onOpenSearch = {},
                    onOpenSettings = {},
                )
            }
        }

        composeRule.onNodeWithText("Use current location").assertIsDisplayed()
        composeRule.onNodeWithText("Search city manually").assertIsDisplayed()
    }

    @Test
    fun initialChoice_useCurrentLocationInvokesCallback() {
        var requested = false
        composeRule.setContent {
            SkyTheme {
                ForecastScreen(
                    state = ForecastUiState.InitialChoice(),
                    onAction = {},
                    onUseCurrentLocation = { requested = true },
                    onOpenAppSettings = {},
                    onOpenSearch = {},
                    onOpenSettings = {},
                )
            }
        }

        composeRule.onNodeWithText("Use current location").performClick()
        assertTrue(requested)
    }

    @Test
    fun initialChoice_manualSearchAvailableAndDeniedMessageShown() {
        var searched = false
        composeRule.setContent {
            SkyTheme {
                ForecastScreen(
                    state = ForecastUiState.InitialChoice(permissionDenied = true),
                    onAction = {},
                    onUseCurrentLocation = {},
                    onOpenAppSettings = {},
                    onOpenSearch = { searched = true },
                    onOpenSettings = {},
                )
            }
        }

        composeRule.onNodeWithText(
            "Location permission denied. You can still search for a city manually.",
        ).assertIsDisplayed()
        composeRule.onNodeWithText("Search city manually").performClick()
        assertTrue(searched)
    }

    @Test
    fun initialChoice_permanentlyDeniedShowsOpenSettings() {
        var openedAppSettings = false
        composeRule.setContent {
            SkyTheme {
                ForecastScreen(
                    state = ForecastUiState.InitialChoice(
                        permissionDenied = true,
                        permissionPermanentlyDenied = true,
                    ),
                    onAction = {},
                    onUseCurrentLocation = {},
                    onOpenAppSettings = { openedAppSettings = true },
                    onOpenSearch = {},
                    onOpenSettings = {},
                )
            }
        }

        // Manual search stays available; the primary action becomes "Open settings".
        composeRule.onNodeWithText("Search city manually").assertIsDisplayed()
        composeRule.onNodeWithText("Open settings").performClick()
        assertTrue(openedAppSettings)
    }

    @Test
    fun content_showsWeeklyAndMetricDetails() {
        composeRule.setContent {
            SkyTheme {
                ForecastScreen(
                    state = ForecastUiState.Content(ForecastPreviewData.forecast),
                    onAction = {},
                    onUseCurrentLocation = {},
                    onOpenAppSettings = {},
                    onOpenSearch = {},
                    onOpenSettings = {},
                )
            }
        }

        composeRule.onNodeWithText("Humidity").assertIsDisplayed()
        composeRule.onNodeWithText("Wind").assertIsDisplayed()
        composeRule.onNodeWithText("Today").assertIsDisplayed()
    }

    @Test
    fun loading_showsProgressText() {
        composeRule.setContent {
            SkyTheme {
                ForecastScreen(
                    state = ForecastUiState.Loading,
                    onAction = {},
                    onUseCurrentLocation = {},
                    onOpenAppSettings = {},
                    onOpenSearch = {},
                    onOpenSettings = {},
                )
            }
        }

        composeRule.onNodeWithText("Fetching latest forecast…").assertIsDisplayed()
    }

    @Test
    fun requestingPermission_showsProgressText() {
        composeRule.setContent {
            SkyTheme {
                ForecastScreen(
                    state = ForecastUiState.RequestingPermission,
                    onAction = {},
                    onUseCurrentLocation = {},
                    onOpenAppSettings = {},
                    onOpenSearch = {},
                    onOpenSettings = {},
                )
            }
        }

        composeRule.onNodeWithText("Fetching latest forecast…").assertIsDisplayed()
    }

    @Test
    fun content_cityName_opensSearch() {
        var openedSearch = false
        composeRule.setContent {
            SkyTheme {
                ForecastScreen(
                    state = ForecastUiState.Content(ForecastPreviewData.forecast),
                    onAction = {},
                    onUseCurrentLocation = {},
                    onOpenAppSettings = {},
                    onOpenSearch = { openedSearch = true },
                    onOpenSettings = {},
                )
            }
        }

        composeRule.onNodeWithText("Malaga").performClick()
        assertTrue(openedSearch)
    }

    @Test
    fun initialChoice_searchManually_opensSearch() {
        var openedSearch = false
        composeRule.setContent {
            SkyTheme {
                ForecastScreen(
                    state = ForecastUiState.InitialChoice(),
                    onAction = {},
                    onUseCurrentLocation = {},
                    onOpenAppSettings = {},
                    onOpenSearch = { openedSearch = true },
                    onOpenSettings = {},
                )
            }
        }

        composeRule.onNodeWithText("Search city manually").performClick()
        assertTrue(openedSearch)
    }

    @Test
    fun error_searchAnotherCity_opensSearch() {
        var openedSearch = false
        composeRule.setContent {
            SkyTheme {
                ForecastScreen(
                    state = ForecastUiState.Error(
                        ErrorMessage("Something went wrong", "Check your connection.", "NETWORK_TIMEOUT"),
                    ),
                    onAction = {},
                    onUseCurrentLocation = {},
                    onOpenAppSettings = {},
                    onOpenSearch = { openedSearch = true },
                    onOpenSettings = {},
                )
            }
        }

        composeRule.onNodeWithText("Search another city").performClick()
        assertTrue(openedSearch)
    }

    @Test
    fun error_retryButtonInvokesAction() {
        var retried = false
        composeRule.setContent {
            SkyTheme {
                ForecastScreen(
                    state = ForecastUiState.Error(
                        ErrorMessage("Something went wrong", "Check your connection.", "NETWORK_TIMEOUT"),
                    ),
                    onAction = { if (it is ForecastUiAction.OnRetryClick) retried = true },
                    onUseCurrentLocation = {},
                    onOpenAppSettings = {},
                    onOpenSearch = {},
                    onOpenSettings = {},
                )
            }
        }

        composeRule.onNodeWithText("Something went wrong").assertIsDisplayed()
        composeRule.onNodeWithText("Try again").performClick()
        assertTrue(retried)
    }
}