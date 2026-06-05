package com.sportygroup.weatherapp.feature.forecast.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sportygroup.weatherapp.core.designsystem.component.SkyCard
import com.sportygroup.weatherapp.core.designsystem.component.SkyGhostButton
import com.sportygroup.weatherapp.core.designsystem.component.SkyPrimaryButton
import com.sportygroup.weatherapp.core.designsystem.icon.UiIcon
import com.sportygroup.weatherapp.core.designsystem.icon.UiIconType
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme

/** Location permission state, offering to enable location or search manually. */
@Composable
fun PermissionRequiredContent(
    canSearchManually: Boolean,
    onEnableLocation: () -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 22.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .background(SkyTheme.colors.primarySoft, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(106.dp)
                    .background(SkyTheme.colors.card, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                UiIcon(
                    icon = UiIconType.LOCATION_FILL,
                    size = 62.dp,
                    tint = SkyTheme.colors.primary,
                    contentDescription = null,
                )
            }
        }
        SkyCard(
            modifier = Modifier.padding(top = 28.dp),
            contentPadding = PaddingValues(24.dp),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Enable location",
                    color = SkyTheme.colors.textHigh,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 23.sp,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "SkyCast uses your location to show accurate weather for where you are " +
                        "right now. You can change this anytime.",
                    color = SkyTheme.colors.textMedium,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 10.dp, bottom = 22.dp),
                )
                SkyPrimaryButton(
                    text = "Use my location",
                    onClick = onEnableLocation,
                    icon = UiIconType.LOCATION_FILL,
                )
                if (canSearchManually) {
                    SkyGhostButton(
                        text = "Search a city instead",
                        onClick = onSearch,
                        icon = UiIconType.SEARCH,
                        modifier = Modifier.padding(top = 12.dp),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PermissionRequiredContentPreview() {
    SkyTheme {
        PermissionRequiredContent(
            canSearchManually = true,
            onEnableLocation = {},
            onSearch = {},
        )
    }
}