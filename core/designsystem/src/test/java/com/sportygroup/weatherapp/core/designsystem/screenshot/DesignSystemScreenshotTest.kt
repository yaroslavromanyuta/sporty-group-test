package com.sportygroup.weatherapp.core.designsystem.screenshot

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sportygroup.weatherapp.core.designsystem.component.SkyCard
import com.sportygroup.weatherapp.core.designsystem.component.SkyGhostButton
import com.sportygroup.weatherapp.core.designsystem.component.SkyIconButton
import com.sportygroup.weatherapp.core.designsystem.component.SkyPrimaryButton
import com.sportygroup.weatherapp.core.designsystem.component.SkySearchBar
import com.sportygroup.weatherapp.core.designsystem.component.SkySectionHeader
import com.sportygroup.weatherapp.core.designsystem.icon.UiIcon
import com.sportygroup.weatherapp.core.designsystem.icon.UiIconType
import com.sportygroup.weatherapp.core.designsystem.icon.WeatherIcon
import com.sportygroup.weatherapp.core.designsystem.icon.WeatherType
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme
import org.junit.Test

/** Screenshot coverage for every reusable design-system component, across themes + accessibility. */
class DesignSystemScreenshotTest(variant: ThemeVariant) : RoborazziScreenshotTest(variant) {

    @Test
    fun buttons() = snapshot("buttons") {
        Column(
            modifier = Modifier.size(width = 280.dp, height = 160.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SkyPrimaryButton(text = "Use my location", onClick = {}, icon = UiIconType.LOCATION_FILL)
            SkyGhostButton(text = "Search a city instead", onClick = {}, icon = UiIconType.SEARCH)
        }
    }

    @Test
    fun card() = snapshot("card") {
        SkyCard {
            Text(text = "SkyCast card", color = SkyTheme.colors.textHigh)
        }
    }

    @Test
    fun iconButtons() = snapshot("iconButtons") {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SkyIconButton(icon = UiIconType.SLIDERS, contentDescription = "Settings", onClick = {})
            SkyIconButton(icon = UiIconType.BACK, contentDescription = "Back", onClick = {})
            SkyIconButton(icon = UiIconType.SEARCH, contentDescription = "Search", onClick = {})
        }
    }

    @Test
    fun searchBar() = snapshot("searchBar") {
        Column(
            modifier = Modifier.size(width = 320.dp, height = 140.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SkySearchBar(value = "", onValueChange = {}, onClear = {})
            SkySearchBar(value = "Malaga", onValueChange = {}, onClear = {})
        }
    }

    @Test
    fun sectionHeader() = snapshot("sectionHeader") {
        Column(modifier = Modifier.padding(12.dp)) {
            SkySectionHeader(title = "Hourly")
            SkySectionHeader(title = "This week", actionText = "7 days", onActionClick = {})
        }
    }

    @Test
    fun uiIcons() = snapshot("uiIcons") {
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

    @Test
    fun weatherIcons() = snapshot("weatherIcons") {
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
