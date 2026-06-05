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
                    onOpenSearch = {},
                    onOpenSettings = { openedSettings = true },
                )
            }
        }

        composeRule.onNodeWithContentDescription("Settings").performClick()
        assertTrue(openedSettings)
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
                    onOpenSearch = {},
                    onOpenSettings = {},
                )
            }
        }

        composeRule.onNodeWithText("Something went wrong").assertIsDisplayed()
        composeRule.onNodeWithText("Try again").performClick()
        assertTrue(retried)
    }

    @Test
    fun permission_showsEnableLocation() {
        composeRule.setContent {
            SkyTheme {
                ForecastScreen(
                    state = ForecastUiState.PermissionRequired(),
                    onAction = {},
                    onOpenSearch = {},
                    onOpenSettings = {},
                )
            }
        }

        composeRule.onNodeWithText("Enable location").assertIsDisplayed()
        composeRule.onNodeWithText("Use my location").assertIsDisplayed()
    }
}