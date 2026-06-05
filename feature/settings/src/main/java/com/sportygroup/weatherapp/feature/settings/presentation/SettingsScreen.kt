package com.sportygroup.weatherapp.feature.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sportygroup.weatherapp.core.designsystem.component.SkyIconButton
import com.sportygroup.weatherapp.core.designsystem.icon.UiIconType
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme
import com.sportygroup.weatherapp.feature.settings.presentation.component.SegmentOption
import com.sportygroup.weatherapp.feature.settings.presentation.component.SettingsDivider
import com.sportygroup.weatherapp.feature.settings.presentation.component.SettingsSection
import com.sportygroup.weatherapp.feature.settings.presentation.component.SettingsSegmentedControl
import com.sportygroup.weatherapp.feature.settings.presentation.component.SettingsThemePicker
import com.sportygroup.weatherapp.feature.settings.presentation.model.SettingsUiModel
import com.sportygroup.weatherapp.feature.settings.presentation.state.SettingsUiAction
import com.sportygroup.weatherapp.feature.settings.presentation.state.SettingsUiState
import com.sportygroup.weatherapp.lib.settings.model.MeasurementSystem
import com.sportygroup.weatherapp.lib.settings.model.TemperatureUnit

/** Stateless Settings screen. */
@Composable
fun SettingsScreen(
    state: SettingsUiState,
    onAction: (SettingsUiAction) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val settings = state.settings
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(SkyTheme.colors.gradientTop, SkyTheme.colors.gradientBottom),
                ),
            )
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp, bottom = 36.dp),
    ) {
        Row(
            modifier = Modifier.padding(bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            SkyIconButton(icon = UiIconType.BACK, contentDescription = "Back", onClick = onBack)
            Text(
                text = "Settings",
                color = SkyTheme.colors.textHigh,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
            )
        }

        SettingsSection(label = "Units") {
            SettingsSegmentedControl(
                label = "Measurement system",
                helper = "Affects wind speed, pressure, and visibility",
                options = listOf(
                    SegmentOption(MeasurementSystem.METRIC, "Metric"),
                    SegmentOption(MeasurementSystem.IMPERIAL, "Imperial"),
                ),
                selected = settings.measurementSystem,
                onSelect = { onAction(SettingsUiAction.SelectMeasurementSystem(it)) },
            )
            SettingsDivider()
            SettingsSegmentedControl(
                label = "Temperature",
                options = listOf(
                    SegmentOption(TemperatureUnit.CELSIUS, "Celsius"),
                    SegmentOption(TemperatureUnit.FAHRENHEIT, "Fahrenheit"),
                ),
                selected = settings.temperatureUnit,
                onSelect = { onAction(SettingsUiAction.SelectTemperatureUnit(it)) },
            )
        }

        Spacer(Modifier.height(22.dp))

        SettingsSection(label = "Appearance") {
            SettingsThemePicker(
                selected = settings.themeMode,
                onSelect = { onAction(SettingsUiAction.SelectThemeMode(it)) },
            )
        }

        Spacer(Modifier.height(16.dp))
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            Text(
                text = "SkyCast 1.0",
                color = SkyTheme.colors.textLow,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
            )
            Text(
                text = "Weather data by Open-Meteo",
                color = SkyTheme.colors.textLow,
                fontWeight = FontWeight.Medium,
                fontSize = 11.5.sp,
            )
        }
    }
}

@Preview(heightDp = 760)
@Composable
private fun SettingsScreenPreview() {
    SkyTheme {
        SettingsScreen(
            state = SettingsUiState(
                SettingsUiModel(
                    measurementSystem = MeasurementSystem.METRIC,
                    temperatureUnit = TemperatureUnit.CELSIUS,
                    themeMode = com.sportygroup.weatherapp.lib.settings.model.ThemeMode.SYSTEM,
                ),
            ),
            onAction = {},
            onBack = {},
        )
    }
}