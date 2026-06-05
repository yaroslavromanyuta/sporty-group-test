package com.sportygroup.weatherapp.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sportygroup.weatherapp.core.designsystem.icon.UiIcon
import com.sportygroup.weatherapp.core.designsystem.icon.UiIconType
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme

/** Full-width pill primary CTA. */
@Composable
fun SkyPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: UiIconType? = null,
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(56.dp),
        shape = SkyTheme.shapes.pill,
        colors = ButtonDefaults.buttonColors(
            containerColor = SkyTheme.colors.primary,
            contentColor = SkyTheme.colors.onPrimary,
        ),
    ) {
        ButtonContent(text, icon, SkyTheme.colors.onPrimary)
    }
}

/** Outlined secondary action. */
@Composable
fun SkyGhostButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: UiIconType? = null,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(54.dp),
        shape = SkyTheme.shapes.pill,
        border = BorderStroke(1.5.dp, SkyTheme.colors.cardBorder),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = SkyTheme.colors.primary),
    ) {
        ButtonContent(text, icon, SkyTheme.colors.primary)
    }
}

@Composable
private fun ButtonContent(text: String, icon: UiIconType?, contentColor: androidx.compose.ui.graphics.Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(9.dp),
    ) {
        if (icon != null) {
            UiIcon(icon = icon, size = 20.dp, tint = contentColor)
        }
        Text(text = text, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

@Preview
@Composable
private fun SkyButtonsPreview() {
    SkyTheme {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.size(width = 280.dp, height = 160.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SkyPrimaryButton(text = "Use my location", onClick = {}, icon = UiIconType.LOCATION_FILL)
            SkyGhostButton(text = "Search a city instead", onClick = {}, icon = UiIconType.SEARCH)
        }
    }
}