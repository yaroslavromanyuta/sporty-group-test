package com.sportygroup.weatherapp.feature.settings.screenshot

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.Density
import com.github.takahirom.roborazzi.captureRoboImage
import com.sportygroup.weatherapp.core.designsystem.preview.SkyPreview
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/** A rendering variant: a color theme optionally combined with an accessibility font scale. */
data class ThemeVariant(val label: String, val night: Boolean, val fontScale: Float) {
    override fun toString(): String = label
}

/**
 * Base class for JVM (Robolectric + Roborazzi) screenshot tests.
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
        composeRule.setContent {
            val base = LocalDensity.current
            CompositionLocalProvider(
                LocalDensity provides Density(density = base.density, fontScale = variant.fontScale),
            ) {
                SkyPreview {
                    Box(modifier = Modifier.fillMaxSize()) {
                        content()
                    }
                }
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
            arrayOf(ThemeVariant(label = "light", night = false, fontScale = 1f)),
            arrayOf(ThemeVariant(label = "dark", night = true, fontScale = 1f)),
            arrayOf(ThemeVariant(label = "a11yLargeFontLight", night = false, fontScale = 1.5f)),
            arrayOf(ThemeVariant(label = "a11yLargeFontDark", night = true, fontScale = 1.5f)),
        )
    }
}
