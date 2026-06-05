package com.sportygroup.weatherapp.feature.forecast.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.sportygroup.weatherapp.core.designsystem.preview.SkyPreview
import com.sportygroup.weatherapp.core.designsystem.preview.ThemePreviews
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sportygroup.weatherapp.feature.forecast.R
import com.sportygroup.weatherapp.core.designsystem.component.SkyIconButton
import com.sportygroup.weatherapp.core.designsystem.component.SkySearchBar
import com.sportygroup.weatherapp.core.designsystem.component.SkySectionHeader
import com.sportygroup.weatherapp.core.designsystem.icon.UiIcon
import com.sportygroup.weatherapp.core.designsystem.icon.UiIconType
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme
import com.sportygroup.weatherapp.feature.forecast.presentation.component.CityRow
import com.sportygroup.weatherapp.feature.forecast.presentation.model.CityUiModel
import com.sportygroup.weatherapp.feature.forecast.presentation.preview.ForecastPreviewData
import com.sportygroup.weatherapp.feature.forecast.presentation.state.CitySearchUiAction
import com.sportygroup.weatherapp.feature.forecast.presentation.state.CitySearchUiState

/** Stateless city search screen. */
@Composable
fun CitySearchScreen(
    state: CitySearchUiState,
    onAction: (CitySearchUiAction) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(SkyTheme.colors.gradientTop, SkyTheme.colors.gradientBottom),
                ),
            )
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp, bottom = 16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            SkyIconButton(
                icon = UiIconType.BACK,
                contentDescription = stringResource(R.string.search_back_content_description),
                onClick = onBack,
            )
            Text(
                text = stringResource(R.string.search_choose_location),
                color = SkyTheme.colors.textHigh,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
            )
        }

        SkySearchBar(
            value = state.query,
            onValueChange = { onAction(CitySearchUiAction.OnQueryChanged(it)) },
            onClear = { onAction(CitySearchUiAction.OnClearQuery) },
        )

        when {
            state.showEmpty -> EmptyResults(query = state.query)
            state.showResults -> ResultList(
                cities = state.results,
                leadingIcon = UiIconType.LOCATION,
                onPick = { onAction(CitySearchUiAction.OnCitySelected(it)) },
            )
            else -> SuggestionsList(
                recent = state.recent,
                onPick = { onAction(CitySearchUiAction.OnCitySelected(it)) },
                onUseCurrentLocation = { onAction(CitySearchUiAction.OnUseCurrentLocation) },
            )
        }
    }
}

@Composable
private fun ResultList(
    cities: List<CityUiModel>,
    leadingIcon: UiIconType,
    onPick: (CityUiModel) -> Unit,
) {
    LazyColumn(modifier = Modifier.padding(top = 18.dp)) {
        items(cities, key = { "${it.name}-${it.latitude}-${it.longitude}" }) { city ->
            CityRow(city = city, onClick = { onPick(city) }, leadingIcon = leadingIcon)
        }
    }
}

@Composable
private fun SuggestionsList(
    recent: List<CityUiModel>,
    onPick: (CityUiModel) -> Unit,
    onUseCurrentLocation: () -> Unit,
) {
    Column(modifier = Modifier.padding(top = 22.dp)) {
        if (recent.isNotEmpty()) {
            SkySectionHeader(title = stringResource(R.string.search_recent))
            recent.forEach { city ->
                CityRow(
                    city = city,
                    onClick = { onPick(city) },
                    leadingIcon = UiIconType.HISTORY,
                )
            }
            Spacer(Modifier.height(8.dp))
        }
        SkySectionHeader(title = stringResource(R.string.search_suggested))
        UseCurrentLocationRow(onClick = onUseCurrentLocation)
    }
}

@Composable
private fun UseCurrentLocationRow(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(13.dp),
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .background(SkyTheme.colors.primarySoft, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center,
        ) {
            UiIcon(icon = UiIconType.LOCATION_FILL, size = 19.dp, tint = SkyTheme.colors.primary)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.forecast_use_current_location),
                color = SkyTheme.colors.textHigh,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
            Text(
                text = stringResource(R.string.search_detect_city),
                color = SkyTheme.colors.textMedium,
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
            )
        }
        UiIcon(icon = UiIconType.CHEVRON, size = 18.dp, tint = SkyTheme.colors.textLow)
    }
}

@Composable
private fun EmptyResults(query: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(76.dp)
                .background(SkyTheme.colors.primarySoft, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            UiIcon(icon = UiIconType.SEARCH, size = 32.dp, tint = SkyTheme.colors.primary)
        }
        Text(
            text = stringResource(R.string.search_no_matches, query),
            color = SkyTheme.colors.textHigh,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp),
        )
        Text(
            text = stringResource(R.string.search_no_matches_hint),
            color = SkyTheme.colors.textMedium,
            fontWeight = FontWeight.Medium,
            fontSize = 14.5.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 6.dp),
        )
    }
}

@ThemePreviews
@Composable
private fun CitySearchResultsPreview() {
    SkyPreview {
        CitySearchScreen(
            state = CitySearchUiState(query = "Ma", results = ForecastPreviewData.searchResults),
            onAction = {},
            onBack = {},
        )
    }
}

@ThemePreviews
@Composable
private fun CitySearchSuggestionsPreview() {
    SkyPreview {
        CitySearchScreen(
            state = CitySearchUiState(recent = ForecastPreviewData.recent),
            onAction = {},
            onBack = {},
        )
    }
}

@ThemePreviews
@Composable
private fun CitySearchEmptyPreview() {
    SkyPreview {
        CitySearchScreen(
            state = CitySearchUiState(query = "Zzzzz", results = emptyList()),
            onAction = {},
            onBack = {},
        )
    }
}