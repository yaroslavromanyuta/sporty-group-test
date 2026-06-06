package com.sportygroup.weatherapp.feature.forecast.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.sportygroup.weatherapp.core.designsystem.preview.SkyPreview
import com.sportygroup.weatherapp.core.designsystem.preview.ThemePreviews
import androidx.compose.ui.unit.sp
import com.sportygroup.weatherapp.core.designsystem.component.SkyCard
import com.sportygroup.weatherapp.core.designsystem.component.SkyGhostButton
import com.sportygroup.weatherapp.core.designsystem.component.SkyPrimaryButton
import com.sportygroup.weatherapp.core.designsystem.icon.UiIcon
import com.sportygroup.weatherapp.core.designsystem.icon.UiIconType
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme
import com.sportygroup.weatherapp.feature.forecast.R

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
    val spacing = SkyTheme.spacing
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = spacing.xlPlus, vertical = spacing.xxl),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(SkyTheme.size.circleXl)
                .background(SkyTheme.colors.primarySoft, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(SkyTheme.size.circleMd)
                    .background(SkyTheme.colors.card, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                UiIcon(
                    icon = UiIconType.LOCATION_FILL,
                    size = SkyTheme.size.iconGiant,
                    tint = SkyTheme.colors.primary,
                    contentDescription = null,
                )
            }
        }
        SkyCard(
            modifier = Modifier.padding(top = spacing.xxxl),
            contentPadding = PaddingValues(spacing.xxl),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.forecast_welcome_title),
                    color = SkyTheme.colors.textHigh,
                    style = SkyTheme.typography.headline,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = stringResource(R.string.forecast_welcome_message),
                    color = SkyTheme.colors.textMedium,
                    style = SkyTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = spacing.md, bottom = spacing.xlPlus),
                )
                if (permissionPermanentlyDenied) {
                    SkyPrimaryButton(
                        text = stringResource(R.string.forecast_open_settings),
                        onClick = onOpenAppSettings,
                        icon = UiIconType.LOCATION_FILL,
                    )
                } else {
                    SkyPrimaryButton(
                        text = stringResource(R.string.forecast_use_current_location),
                        onClick = onUseCurrentLocation,
                        icon = UiIconType.LOCATION_FILL,
                    )
                }
                if (canSearchManually) {
                    SkyGhostButton(
                        text = stringResource(R.string.forecast_search_manually),
                        onClick = onSearch,
                        icon = UiIconType.SEARCH,
                        modifier = Modifier.padding(top = spacing.m),
                    )
                }
            }
        }
        val deniedMessage = when {
            permissionPermanentlyDenied ->
                stringResource(R.string.forecast_permission_permanently_denied)
            permissionDenied ->
                stringResource(R.string.forecast_permission_denied)
            else -> null
        }
        if (deniedMessage != null) {
            Text(
                text = deniedMessage,
                color = SkyTheme.colors.danger,
                style = SkyTheme.typography.caption,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = spacing.lg),
            )
        }
    }
}

@ThemePreviews
@Composable
private fun InitialChoiceContentPreview() {
    SkyPreview {
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

@ThemePreviews
@Composable
private fun InitialChoiceContentDeniedPreview() {
    SkyPreview {
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

@ThemePreviews
@Composable
private fun InitialChoiceContentPermanentlyDeniedPreview() {
    SkyPreview {
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
