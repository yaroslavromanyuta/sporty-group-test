package com.sportygroup.weatherapp.feature.forecast.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.sportygroup.weatherapp.core.designsystem.preview.SkyPreview
import com.sportygroup.weatherapp.core.designsystem.preview.ThemePreviews
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sportygroup.weatherapp.core.designsystem.icon.UiIcon
import com.sportygroup.weatherapp.core.designsystem.icon.UiIconType
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme
import com.sportygroup.weatherapp.feature.forecast.presentation.model.CityUiModel
import com.sportygroup.weatherapp.feature.forecast.presentation.preview.ForecastPreviewData

/** Tappable city result row used in search results and the recent list. */
@Composable
fun CityRow(
    city: CityUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: UiIconType = UiIconType.LOCATION,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = SkyTheme.spacing.m, vertical = SkyTheme.spacing.mdPlus),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SkyTheme.spacing.mPlus),
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .background(SkyTheme.colors.primarySoft, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center,
        ) {
            UiIcon(icon = leadingIcon, size = 19.dp, tint = SkyTheme.colors.primary)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = city.name,
                color = SkyTheme.colors.textHigh,
                style = SkyTheme.typography.body,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = city.region,
                color = SkyTheme.colors.textMedium,
                style = SkyTheme.typography.caption,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@ThemePreviews
@Composable
private fun CityRowPreview() {
    SkyPreview {
        CityRow(city = ForecastPreviewData.searchResults.first(), onClick = {})
    }
}