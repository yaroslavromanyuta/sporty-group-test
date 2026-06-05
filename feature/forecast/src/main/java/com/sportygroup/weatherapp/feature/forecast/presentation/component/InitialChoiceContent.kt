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

/**
 * Start screen and decision point: the user chooses between current-location weather and
 * manual city search. Tapping "Use my location" triggers the permission flow in the route
 * (not here). When [permissionDenied] is true, a message reminds the user that manual
 * search is still available.
 */
@Composable
fun InitialChoiceContent(
    permissionDenied: Boolean,
    permissionPermanentlyDenied: Boolean,
    canSearchManually: Boolean,
    onUseCurrentLocation: () -> Unit,
    onOpenAppSettings: () -> Unit,
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
                    text = "Welcome to SkyCast",
                    color = SkyTheme.colors.textHigh,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 23.sp,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "Use your current location for local weather, or search for any city. " +
                        "We only request location after you choose it.",
                    color = SkyTheme.colors.textMedium,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 10.dp, bottom = 22.dp),
                )
                if (permissionPermanentlyDenied) {
                    SkyPrimaryButton(
                        text = "Open settings",
                        onClick = onOpenAppSettings,
                        icon = UiIconType.LOCATION_FILL,
                    )
                } else {
                    SkyPrimaryButton(
                        text = "Use current location",
                        onClick = onUseCurrentLocation,
                        icon = UiIconType.LOCATION_FILL,
                    )
                }
                if (canSearchManually) {
                    SkyGhostButton(
                        text = "Search city manually",
                        onClick = onSearch,
                        icon = UiIconType.SEARCH,
                        modifier = Modifier.padding(top = 12.dp),
                    )
                }
            }
        }
        val deniedMessage = when {
            permissionPermanentlyDenied ->
                "Location permission is turned off. Enable it in Settings, or search for a city manually."
            permissionDenied ->
                "Location permission denied. You can still search for a city manually."
            else -> null
        }
        if (deniedMessage != null) {
            Text(
                text = deniedMessage,
                color = SkyTheme.colors.danger,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp),
            )
        }
    }
}

@Preview
@Composable
private fun InitialChoiceContentPreview() {
    SkyTheme {
        InitialChoiceContent(
            permissionDenied = false,
            permissionPermanentlyDenied = false,
            canSearchManually = true,
            onUseCurrentLocation = {},
            onOpenAppSettings = {},
            onSearch = {},
        )
    }
}

@Preview
@Composable
private fun InitialChoiceContentDeniedPreview() {
    SkyTheme {
        InitialChoiceContent(
            permissionDenied = true,
            permissionPermanentlyDenied = false,
            canSearchManually = true,
            onUseCurrentLocation = {},
            onOpenAppSettings = {},
            onSearch = {},
        )
    }
}

@Preview
@Composable
private fun InitialChoiceContentPermanentlyDeniedPreview() {
    SkyTheme {
        InitialChoiceContent(
            permissionDenied = true,
            permissionPermanentlyDenied = true,
            canSearchManually = true,
            onUseCurrentLocation = {},
            onOpenAppSettings = {},
            onSearch = {},
        )
    }
}