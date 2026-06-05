package com.sportygroup.weatherapp.feature.forecast.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.sportygroup.weatherapp.core.designsystem.preview.SkyPreview
import com.sportygroup.weatherapp.core.designsystem.preview.ThemePreviews
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sportygroup.weatherapp.core.designsystem.component.SkyIconButton
import com.sportygroup.weatherapp.core.designsystem.icon.UiIcon
import com.sportygroup.weatherapp.core.designsystem.icon.UiIconType
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme
import com.sportygroup.weatherapp.feature.forecast.R

/** City selector + settings entry point at the top of the home screen. */
@Composable
fun ForecastTopBar(
    cityName: String,
    isCurrentLocation: Boolean,
    onOpenSearch: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = onOpenSearch)
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            UiIcon(
                icon = if (isCurrentLocation) UiIconType.LOCATION_FILL else UiIconType.LOCATION,
                size = 20.dp,
                tint = if (isCurrentLocation) SkyTheme.colors.primary else SkyTheme.colors.textMedium,
                contentDescription = null,
            )
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = cityName,
                        color = SkyTheme.colors.textHigh,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 19.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    UiIcon(
                        icon = UiIconType.CHEVRON_DOWN,
                        size = 17.dp,
                        tint = SkyTheme.colors.textMedium,
                        modifier = Modifier.padding(start = 4.dp),
                    )
                }
                if (isCurrentLocation) {
                    Text(
                        text = stringResource(R.string.forecast_current_location),
                        color = SkyTheme.colors.textMedium,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                    )
                }
            }
        }
        SkyIconButton(
            icon = UiIconType.SLIDERS,
            contentDescription = stringResource(R.string.forecast_settings_content_description),
            onClick = onOpenSettings,
        )
    }
}

@ThemePreviews
@Composable
private fun ForecastTopBarPreview() {
    SkyPreview {
        ForecastTopBar(
            cityName = "Malaga",
            isCurrentLocation = true,
            onOpenSearch = {},
            onOpenSettings = {},
        )
    }
}