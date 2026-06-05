package com.sportygroup.weatherapp.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme

/** Temperature unit options surfaced by [SkyUnitToggle]. */
enum class TemperatureUnitOption(val label: String) {
    CELSIUS("°C"),
    FAHRENHEIT("°F"),
}

/** Segmented °C / °F selector. */
@Composable
fun SkyUnitToggle(
    selected: TemperatureUnitOption,
    onSelect: (TemperatureUnitOption) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(SkyTheme.colors.chip)
            .padding(3.dp),
    ) {
        TemperatureUnitOption.entries.forEach { option ->
            Segment(
                label = option.label,
                selected = option == selected,
                onClick = { onSelect(option) },
            )
        }
    }
}

@Composable
private fun Segment(label: String, selected: Boolean, onClick: () -> Unit) {
    val background = if (selected) SkyTheme.colors.card else Color.Transparent
    val textColor = if (selected) SkyTheme.colors.primary else SkyTheme.colors.textMedium
    Text(
        text = label,
        color = textColor,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .clip(CircleShape)
            .background(background)
            .clickable(onClick = onClick)
            .widthIn(min = 34.dp)
            .padding(horizontal = 10.dp, vertical = 5.dp),
    )
}

@Preview
@Composable
private fun SkyUnitTogglePreview() {
    SkyTheme {
        SkyUnitToggle(selected = TemperatureUnitOption.CELSIUS, onSelect = {})
    }
}