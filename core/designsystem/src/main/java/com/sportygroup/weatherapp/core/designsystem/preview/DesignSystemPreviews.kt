package com.sportygroup.weatherapp.core.designsystem.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sportygroup.weatherapp.core.designsystem.component.SkyIconButton
import com.sportygroup.weatherapp.core.designsystem.component.SkySectionHeader
import com.sportygroup.weatherapp.core.designsystem.icon.UiIcon
import com.sportygroup.weatherapp.core.designsystem.icon.UiIconType
import com.sportygroup.weatherapp.core.designsystem.icon.WeatherIcon
import com.sportygroup.weatherapp.core.designsystem.icon.WeatherType

@ThemePreviews
@Composable
private fun SkyIconButtonPreview() {
    SkyPreview {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SkyIconButton(icon = UiIconType.SLIDERS, contentDescription = "Settings", onClick = {})
            SkyIconButton(icon = UiIconType.BACK, contentDescription = "Back", onClick = {})
            SkyIconButton(icon = UiIconType.SEARCH, contentDescription = "Search", onClick = {})
        }
    }
}

@ThemePreviews
@Composable
private fun SkySectionHeaderPreview() {
    SkyPreview {
        Column(modifier = Modifier.padding(12.dp)) {
            SkySectionHeader(title = "Hourly")
            SkySectionHeader(title = "This week", actionText = "7 days", onActionClick = {})
        }
    }
}

@ThemePreviews
@Composable
private fun UiIconPreview() {
    SkyPreview {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            UiIcon(icon = UiIconType.LOCATION_FILL)
            UiIcon(icon = UiIconType.SEARCH)
            UiIcon(icon = UiIconType.THERMO)
            UiIcon(icon = UiIconType.WIND)
            UiIcon(icon = UiIconType.MOON)
            UiIcon(icon = UiIconType.SUN)
        }
    }
}

@ThemePreviews
@Composable
private fun WeatherIconPreview() {
    SkyPreview {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            WeatherIcon(type = WeatherType.SUNNY, size = 48.dp)
            WeatherIcon(type = WeatherType.PARTLY, size = 48.dp)
            WeatherIcon(type = WeatherType.RAIN, size = 48.dp)
            WeatherIcon(type = WeatherType.STORM, size = 48.dp)
            WeatherIcon(type = WeatherType.CLEAR_NIGHT, size = 48.dp)
        }
    }
}