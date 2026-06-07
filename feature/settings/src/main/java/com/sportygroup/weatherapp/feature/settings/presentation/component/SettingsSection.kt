package com.sportygroup.weatherapp.feature.settings.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sportygroup.weatherapp.core.designsystem.component.SkyCard
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme

/** Uppercased section label above a padding-free [SkyCard] holding settings rows. */
@Composable
fun SettingsSection(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label.uppercase(),
            color = SkyTheme.colors.textMedium,
            style = SkyTheme.typography.overline,
            letterSpacing = 0.7.sp,
            modifier = Modifier.padding(start = SkyTheme.spacing.xs, bottom = SkyTheme.spacing.md),
        )
        SkyCard(contentPadding = PaddingValues(SkyTheme.spacing.none)) {
            Column { content() }
        }
    }
}

/** Thin divider used between rows inside a [SettingsSection] card. */
@Composable
fun SettingsDivider() {
    HorizontalDivider(
        color = SkyTheme.colors.cardBorder,
        modifier = Modifier.padding(horizontal = SkyTheme.spacing.lg),
    )
}
