package com.sportygroup.weatherapp.feature.settings.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sportygroup.weatherapp.core.designsystem.icon.UiIcon
import com.sportygroup.weatherapp.core.designsystem.icon.UiIconType
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme
import com.sportygroup.weatherapp.feature.settings.R
import com.sportygroup.weatherapp.lib.settings.model.ThemeMode

private data class ThemeOption(val mode: ThemeMode, val labelRes: Int, val icon: UiIconType)

private val themeOptions = listOf(
    ThemeOption(ThemeMode.SYSTEM, R.string.settings_theme_system, UiIconType.HALFMOON),
    ThemeOption(ThemeMode.LIGHT, R.string.settings_theme_light, UiIconType.SUN),
    ThemeOption(ThemeMode.DARK, R.string.settings_theme_dark, UiIconType.MOON),
)

/** Three tappable theme cards: System / Light / Dark. */
@Composable
fun SettingsThemePicker(
    selected: ThemeMode,
    onSelect: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth().padding(SkyTheme.spacing.lg)) {
        Text(
            text = stringResource(R.string.settings_theme_label),
            color = SkyTheme.colors.textHigh,
            style = SkyTheme.typography.body,
            modifier = Modifier.padding(bottom = SkyTheme.spacing.ml),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(SkyTheme.spacing.md)) {
            themeOptions.forEach { option ->
                ThemeCard(
                    option = option,
                    selected = option.mode == selected,
                    onClick = { onSelect(option.mode) },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun ThemeCard(
    option: ThemeOption,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val background = if (selected) {
        SkyTheme.colors.primarySoft
    } else {
        SkyTheme.colors.chip
    }
    val contentColor = if (selected) SkyTheme.colors.primary else SkyTheme.colors.textMedium
    Column(
        modifier = modifier
            .heightIn(min = 82.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(background)
            .border(
                BorderStroke(
                    if (selected) 2.dp else 1.5.dp,
                    if (selected) SkyTheme.colors.primary else SkyTheme.colors.cardBorder,
                ),
                RoundedCornerShape(16.dp),
            )
            .selectable(selected = selected, role = Role.RadioButton, onClick = onClick)
            .padding(vertical = SkyTheme.spacing.mPlus, horizontal = SkyTheme.spacing.s),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(SkyTheme.spacing.sm),
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (selected) SkyTheme.colors.primarySoft else SkyTheme.colors.track,
                ),
            contentAlignment = Alignment.Center,
        ) {
            UiIcon(icon = option.icon, size = 20.dp, tint = contentColor)
        }
        Text(
            text = stringResource(option.labelRes),
            color = contentColor,
            style = SkyTheme.typography.captionSmall,
            fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp,
        )
    }
}