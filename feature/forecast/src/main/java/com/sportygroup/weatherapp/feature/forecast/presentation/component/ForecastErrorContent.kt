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
import com.sportygroup.weatherapp.core.designsystem.component.SkyCard
import com.sportygroup.weatherapp.core.designsystem.component.SkyGhostButton
import com.sportygroup.weatherapp.core.designsystem.component.SkyPrimaryButton
import com.sportygroup.weatherapp.core.designsystem.icon.UiIcon
import com.sportygroup.weatherapp.core.designsystem.icon.UiIconType
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme
import com.sportygroup.weatherapp.feature.forecast.R
import com.sportygroup.weatherapp.feature.forecast.presentation.mapper.ErrorMessage

/** Full-screen error state with retry and search-another-city actions. */
@Composable
fun ForecastErrorContent(
    error: ErrorMessage,
    canRetry: Boolean,
    canSearchAnotherCity: Boolean,
    onRetry: () -> Unit,
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
                .size(SkyTheme.size.circleLg)
                .background(SkyTheme.colors.dangerSoft, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            UiIcon(
                icon = UiIconType.WARNING,
                size = SkyTheme.size.iconHuge,
                tint = SkyTheme.colors.danger,
                contentDescription = null,
            )
        }
        SkyCard(
            modifier = Modifier.padding(top = spacing.xxxl),
            contentPadding = PaddingValues(spacing.xxl),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = error.title,
                    color = SkyTheme.colors.textHigh,
                    style = SkyTheme.typography.headline,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = error.message,
                    color = SkyTheme.colors.textMedium,
                    style = SkyTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = spacing.md, bottom = spacing.xlPlus),
                )
                if (canRetry) {
                    SkyPrimaryButton(
                        text = stringResource(R.string.forecast_error_try_again),
                        onClick = onRetry,
                        icon = UiIconType.REFRESH,
                    )
                }
                if (canSearchAnotherCity) {
                    SkyGhostButton(
                        text = stringResource(R.string.forecast_error_search_another),
                        onClick = onSearch,
                        icon = UiIconType.SEARCH,
                        modifier = Modifier.padding(top = spacing.m),
                    )
                }
            }
        }
        Text(
            text = stringResource(R.string.forecast_error_code, error.code),
            color = SkyTheme.colors.textLow,
            style = SkyTheme.typography.captionSmall,
            modifier = Modifier.padding(top = spacing.lg),
        )
    }
}

@ThemePreviews
@Composable
private fun ForecastErrorContentPreview() {
    SkyPreview {
        ForecastErrorContent(
            error = ErrorMessage(
                title = "Something went wrong",
                message = "We couldn't reach the weather service. Check your connection and try again.",
                code = "NETWORK_TIMEOUT",
            ),
            canRetry = true,
            canSearchAnotherCity = true,
            onRetry = {},
            onSearch = {},
        )
    }
}
