package com.sportygroup.weatherapp.feature.settings.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme
import com.sportygroup.weatherapp.feature.settings.presentation.model.SettingsUiModel
import com.sportygroup.weatherapp.feature.settings.presentation.state.SettingsUiAction
import com.sportygroup.weatherapp.feature.settings.presentation.state.SettingsUiState
import com.sportygroup.weatherapp.lib.settings.model.MeasurementSystem
import com.sportygroup.weatherapp.lib.settings.model.TemperatureUnit
import com.sportygroup.weatherapp.lib.settings.model.ThemeMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SettingsScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val defaultState = SettingsUiState(
        SettingsUiModel(
            measurementSystem = MeasurementSystem.METRIC,
            temperatureUnit = TemperatureUnit.CELSIUS,
            themeMode = ThemeMode.SYSTEM,
        ),
    )

    @Test
    fun showsSectionsAndOptions() {
        composeRule.setContent {
            SkyTheme { SettingsScreen(state = defaultState, onAction = {}, onBack = {}) }
        }

        composeRule.onNodeWithText("Settings").assertIsDisplayed()
        composeRule.onNodeWithText("Measurement system").assertIsDisplayed()
        composeRule.onNodeWithText("Imperial").assertIsDisplayed()
        composeRule.onNodeWithText("Fahrenheit").assertIsDisplayed()
    }

    @Test
    fun selectingImperialEmitsAction() {
        var action: SettingsUiAction? = null
        composeRule.setContent {
            SkyTheme {
                SettingsScreen(state = defaultState, onAction = { action = it }, onBack = {})
            }
        }

        composeRule.onNodeWithText("Imperial").performClick()

        assertTrue(action is SettingsUiAction.SelectMeasurementSystem)
        assertEquals(
            MeasurementSystem.IMPERIAL,
            (action as SettingsUiAction.SelectMeasurementSystem).value,
        )
    }

    @Test
    fun selectingFahrenheitEmitsAction() {
        var action: SettingsUiAction? = null
        composeRule.setContent {
            SkyTheme {
                SettingsScreen(state = defaultState, onAction = { action = it }, onBack = {})
            }
        }

        composeRule.onNodeWithText("Fahrenheit").performClick()

        assertTrue(action is SettingsUiAction.SelectTemperatureUnit)
        assertEquals(
            TemperatureUnit.FAHRENHEIT,
            (action as SettingsUiAction.SelectTemperatureUnit).value,
        )
    }

    @Test
    fun selectingDarkThemeEmitsAction() {
        var action: SettingsUiAction? = null
        composeRule.setContent {
            SkyTheme {
                SettingsScreen(state = defaultState, onAction = { action = it }, onBack = {})
            }
        }

        composeRule.onNodeWithText("Dark").performClick()

        assertTrue(action is SettingsUiAction.SelectThemeMode)
        assertEquals(ThemeMode.DARK, (action as SettingsUiAction.SelectThemeMode).value)
    }

    @Test
    fun backButtonInvokesCallback() {
        var backPressed = false
        composeRule.setContent {
            SkyTheme {
                SettingsScreen(state = defaultState, onAction = {}, onBack = { backPressed = true })
            }
        }

        composeRule.onNodeWithContentDescription("Back").performClick()
        assertTrue(backPressed)
    }
}