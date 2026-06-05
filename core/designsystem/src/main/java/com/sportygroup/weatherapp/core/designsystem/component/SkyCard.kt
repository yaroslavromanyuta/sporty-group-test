package com.sportygroup.weatherapp.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme

/** Rounded, bordered, soft-shadow surface — the base for every SkyCast card. */
@Composable
fun SkyCard(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = SkyTheme.shapes.card,
    contentPadding: PaddingValues = PaddingValues(20.dp),
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = SkyTheme.colors.card,
        border = BorderStroke(1.dp, SkyTheme.colors.cardBorder),
        shadowElevation = if (SkyTheme.colors.isDark) 0.dp else 6.dp,
        tonalElevation = 0.dp,
    ) {
        Box(Modifier.padding(contentPadding)) { content() }
    }
}

@Preview
@Composable
private fun SkyCardPreview() {
    SkyTheme {
        SkyCard {
            androidx.compose.material3.Text(
                text = "SkyCast card",
                color = SkyTheme.colors.textHigh,
            )
        }
    }
}