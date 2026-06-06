package com.sportygroup.weatherapp.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme

/** Uppercased section label with an optional trailing action (e.g. "7 days"). */
@Composable
fun SkySectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = SkyTheme.spacing.xs, vertical = SkyTheme.spacing.xxxs),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title.uppercase(),
            color = SkyTheme.colors.textMedium,
            style = SkyTheme.typography.sectionLabel,
        )
        if (actionText != null) {
            Text(
                text = actionText,
                color = SkyTheme.colors.primary,
                style = SkyTheme.typography.caption,
                textAlign = TextAlign.End,
                modifier = if (onActionClick != null) Modifier.clickable(onClick = onActionClick) else Modifier,
            )
        }
    }
}