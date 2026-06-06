package com.sportygroup.weatherapp.feature.settings.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sportygroup.weatherapp.core.designsystem.icon.UiIcon
import com.sportygroup.weatherapp.core.designsystem.icon.UiIconType
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme

/** A selectable option within [SettingsSegmentedControl]. */
data class SegmentOption<T>(val value: T, val label: String)

/** Full-width 2+ option segmented control with a row label and optional helper text. */
@Composable
fun <T> SettingsSegmentedControl(
    label: String,
    options: List<SegmentOption<T>>,
    selected: T,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    helper: String? = null,
) {
    Column(modifier = modifier.fillMaxWidth().padding(SkyTheme.spacing.lg)) {
        Text(
            text = label,
            color = SkyTheme.colors.textHigh,
            style = SkyTheme.typography.body,
        )
        if (helper != null) {
            Text(
                text = helper,
                color = SkyTheme.colors.textMedium,
                style = SkyTheme.typography.captionSmall,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = SkyTheme.spacing.xs),
            )
        }
        Row(
            modifier = Modifier
                .padding(top = SkyTheme.spacing.m)
                .fillMaxWidth()
                .clip(SkyTheme.shapes.pill)
                .background(SkyTheme.colors.primarySoft)
                .padding(SkyTheme.spacing.xxs),
            horizontalArrangement = Arrangement.spacedBy(SkyTheme.spacing.none),
        ) {
            options.forEach { option ->
                Segment(
                    label = option.label,
                    selected = option.value == selected,
                    onClick = { onSelect(option.value) },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun Segment(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val background = if (selected) SkyTheme.colors.card else Color.Transparent
    val textColor = if (selected) SkyTheme.colors.primary else SkyTheme.colors.textMedium
    Row(
        modifier = modifier
            .height(SkyTheme.size.segmentHeight)
            .clip(SkyTheme.shapes.pill)
            .background(background)
            .selectable(selected = selected, role = Role.RadioButton, onClick = onClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            color = textColor,
            style = SkyTheme.typography.bodyMedium,
            fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Bold,
        )
        if (selected) {
            UiIcon(
                icon = UiIconType.CHECK,
                size = SkyTheme.size.iconS,
                tint = SkyTheme.colors.primary,
                modifier = Modifier.padding(start = SkyTheme.spacing.s),
            )
        }
    }
}
