package com.sportygroup.weatherapp.core.designsystem.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.sportygroup.weatherapp.core.designsystem.icon.UiIconType
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SkyComponentsTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun primaryButton_click_invokesCallback() {
        var clicked = false
        composeRule.setContent {
            SkyTheme { SkyPrimaryButton(text = "Use my location", onClick = { clicked = true }) }
        }

        composeRule.onNodeWithText("Use my location").performClick()
        assertTrue(clicked)
    }

    @Test
    fun ghostButton_click_invokesCallback() {
        var clicked = false
        composeRule.setContent {
            SkyTheme { SkyGhostButton(text = "Search a city", onClick = { clicked = true }) }
        }

        composeRule.onNodeWithText("Search a city").performClick()
        assertTrue(clicked)
    }

    @Test
    fun iconButton_click_invokesCallback() {
        var clicked = false
        composeRule.setContent {
            SkyTheme {
                SkyIconButton(
                    icon = UiIconType.SLIDERS,
                    contentDescription = "Settings",
                    onClick = { clicked = true },
                )
            }
        }

        composeRule.onNodeWithContentDescription("Settings").performClick()
        assertTrue(clicked)
    }

    @Test
    fun searchBar_showsPlaceholder_acceptsInput() {
        var value = ""
        composeRule.setContent {
            SkyTheme {
                SkySearchBar(
                    value = value,
                    onValueChange = { value = it },
                    onClear = {},
                )
            }
        }

        composeRule.onNodeWithText("Search city or airport").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Search city or airport").performTextInput("Malaga")
        assertEquals("Malaga", value)
    }

    @Test
    fun searchBar_clearButton_invokesOnClear() {
        var cleared = false
        composeRule.setContent {
            SkyTheme {
                SkySearchBar(
                    value = "Malaga",
                    onValueChange = {},
                    onClear = { cleared = true },
                )
            }
        }

        composeRule.onNodeWithContentDescription("Clear search").performClick()
        assertTrue(cleared)
    }

    @Test
    fun sectionHeader_actionClick_invokesCallback() {
        var clicked = false
        composeRule.setContent {
            SkyTheme {
                SkySectionHeader(
                    title = "This week",
                    actionText = "7 days",
                    onActionClick = { clicked = true },
                )
            }
        }

        composeRule.onNodeWithText("7 days").performClick()
        assertTrue(clicked)
    }
}