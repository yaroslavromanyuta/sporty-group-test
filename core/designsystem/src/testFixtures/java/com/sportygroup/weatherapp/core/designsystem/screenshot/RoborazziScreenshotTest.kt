package com.sportygroup.weatherapp.core.designsystem.screenshot

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.github.takahirom.roborazzi.captureRoboImage
import com.sportygroup.weatherapp.core.designsystem.preview.SkyPreview
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/** A rendering variant: a color theme optionally combined with an accessibility font scale. */
data class ThemeVariant(
    val label: String,
    val night: Boolean,
    val fontScale: Float,
    val layoutDirection: LayoutDirection = LayoutDirection.Ltr
) {
    override fun toString(): String = label
}

/**
 * Shared base class for JVM (Robolectric + Roborazzi) screenshot tests across all feature and core
 * modules. Lives in `:core:designsystem` test fixtures; consume it from a module with
 * `testImplementation(testFixtures(project(":core:designsystem")))`.
 *
 * Every concrete test is run once per [ThemeVariant], so a single `snapshot(...)` call produces
 * four golden images covering both required dimensions: the two color themes (light/dark) and an
 * accessibility large-font (1.5x) variant of each. Record with `recordRoborazziDebug`, verify with
 * `verifyRoborazziDebug`.
 */
@RunWith(ParameterizedRobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "w411dp-h891dp-420dpi")
abstract class RoborazziScreenshotTest(private val variant: ThemeVariant) {

    @get:Rule
    val composeRule = createComposeRule()

    /** Renders [content] wrapped in `SkyTheme` for the active variant and records a golden image. */
    protected fun snapshot(name: String, content: @Composable () -> Unit) {
        RuntimeEnvironment.setQualifiers(if (variant.night) "+night" else "+notnight")
        if (variant.layoutDirection == LayoutDirection.Rtl) {
            RuntimeEnvironment.setQualifiers("+ldrtl")
        }
        composeRule.setContent {
            val base = LocalDensity.current
            CompositionLocalProvider(
                LocalDensity provides Density(density = base.density, fontScale = variant.fontScale),
                LocalLayoutDirection provides variant.layoutDirection,
            ) {
                SkyPreview(content)
            }
        }
        composeRule.onRoot().captureRoboImage(
            "src/test/screenshots/${this::class.java.simpleName}/${name}_${variant.label}.png",
        )
    }

    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun variants(): List<Array<Any>> = listOf(
            arrayOf(ThemeVariant(label = "light_ltr", night = false, fontScale = 1f, layoutDirection = LayoutDirection.Ltr)),
            arrayOf(ThemeVariant(label = "light_rtl", night = false, fontScale = 1f, layoutDirection = LayoutDirection.Rtl)),
            arrayOf(ThemeVariant(label = "dark_ltr", night = true, fontScale = 1f, layoutDirection = LayoutDirection.Ltr)),
            arrayOf(ThemeVariant(label = "dark_rtl", night = true, fontScale = 1f, layoutDirection = LayoutDirection.Rtl)),
            arrayOf(ThemeVariant(label = "a11y_light_ltr", night = false, fontScale = 1.5f, layoutDirection = LayoutDirection.Ltr)),
            arrayOf(ThemeVariant(label = "a11y_light_rtl", night = false, fontScale = 1.5f, layoutDirection = LayoutDirection.Rtl)),
            arrayOf(ThemeVariant(label = "a11y_dark_ltr", night = true, fontScale = 1.5f, layoutDirection = LayoutDirection.Ltr)),
            arrayOf(ThemeVariant(label = "a11y_dark_rtl", night = true, fontScale = 1.5f, layoutDirection = LayoutDirection.Rtl)),
        )
    }
}
