package com.sportygroup.weatherapp.core.designsystem.preview

import android.content.res.Configuration
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme

/**
 * Multi-preview annotation that renders a composable in both light and dark SkyCast themes.
 * Use together with [SkyPreview] (which honors the preview's night [Configuration] mode).
 */
@Preview(name = "Light", group = "themes", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", group = "themes", uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class ThemePreviews

/**
 * Wraps preview content in [SkyTheme] on a themed surface. `SkyTheme` derives dark/light from
 * the system (preview) night mode, so a single body renders correctly for [ThemePreviews].
 */
@Composable
fun SkyPreview(content: @Composable () -> Unit) {
    SkyTheme {
        Surface(color = SkyTheme.colors.gradientBottom, content = content)
    }
}