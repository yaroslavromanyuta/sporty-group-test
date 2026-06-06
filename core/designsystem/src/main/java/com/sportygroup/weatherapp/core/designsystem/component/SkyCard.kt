package com.sportygroup.weatherapp.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sportygroup.weatherapp.core.designsystem.preview.SkyPreview
import com.sportygroup.weatherapp.core.designsystem.preview.ThemePreviews
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme

/** Rounded, bordered, soft-shadow surface — the base for every SkyCast card. */
@Composable
fun SkyCard(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = SkyTheme.shapes.card,
    contentPadding: PaddingValues = PaddingValues(SkyTheme.spacing.xl),
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = SkyTheme.colors.card,
        border = BorderStroke(SkyTheme.size.borderThin, SkyTheme.colors.cardBorder),
        shadowElevation = if (SkyTheme.colors.isDark) SkyTheme.size.none else SkyTheme.size.cardElevation,
        tonalElevation = SkyTheme.size.none,
    ) {
        Box(Modifier.padding(contentPadding)) { content() }
    }
}

@ThemePreviews
@Composable
private fun SkyCardPreview() {
    SkyPreview {
        SkyCard {
            androidx.compose.material3.Text(
                text = "SkyCast card",
                color = SkyTheme.colors.textHigh,
            )
        }
    }
}