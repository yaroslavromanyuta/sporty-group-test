package com.sportygroup.weatherapp.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.sportygroup.weatherapp.core.designsystem.R
import com.sportygroup.weatherapp.core.designsystem.icon.UiIcon
import com.sportygroup.weatherapp.core.designsystem.icon.UiIconType
import com.sportygroup.weatherapp.core.designsystem.preview.SkyPreview
import com.sportygroup.weatherapp.core.designsystem.preview.ThemePreviews
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme

/** Pill-shaped search input with a leading magnifier and a clear affordance. */
@Composable
fun SkySearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = stringResource(R.string.ds_search_placeholder),
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(SkyTheme.size.searchBarHeight)
            .clip(SkyTheme.shapes.pill)
            .background(SkyTheme.colors.card)
            .border(SkyTheme.size.borderThin, SkyTheme.colors.cardBorder, SkyTheme.shapes.pill)
            .padding(horizontal = SkyTheme.spacing.ml),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SkyTheme.spacing.md),
    ) {
        UiIcon(icon = UiIconType.SEARCH, size = SkyTheme.size.iconLgPlus, tint = SkyTheme.colors.textMedium)
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    color = SkyTheme.colors.textLow,
                    style = SkyTheme.typography.body,
                    fontWeight = FontWeight.Medium,
                )
            }
            val searchFieldContentDescription = stringResource(R.string.ds_search_placeholder)
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = SkyTheme.typography.body.copy(
                    color = SkyTheme.colors.textHigh,
                    fontWeight = FontWeight.SemiBold,
                ),
                cursorBrush = SolidColor(SkyTheme.colors.primary),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        contentDescription = searchFieldContentDescription
                    },
            )
        }
        if (value.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .size(SkyTheme.size.clearButton)
                    .clip(CircleShape)
                    .background(SkyTheme.colors.chip)
                    .clickable(onClick = onClear),
                contentAlignment = Alignment.Center,
            ) {
                UiIcon(
                    icon = UiIconType.CLOSE,
                    size = SkyTheme.size.iconXs,
                    tint = SkyTheme.colors.textMedium,
                    contentDescription = stringResource(R.string.ds_clear_search_content_description),
                )
            }
        }
    }
}

@ThemePreviews
@Composable
private fun SkySearchBarPreview() {
    SkyPreview {
        Column(
            modifier = Modifier.size(width = 320.dp, height = 140.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SkySearchBar(value = "", onValueChange = {}, onClear = {})
            SkySearchBar(value = "Malaga", onValueChange = {}, onClear = {})
        }
    }
}