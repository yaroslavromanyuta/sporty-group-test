package com.sportygroup.weatherapp.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.sportygroup.weatherapp.core.designsystem.icon.UiIcon
import com.sportygroup.weatherapp.core.designsystem.icon.UiIconType
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme

/** Circular, subtly tinted icon button used in the top bar and search header. */
@Composable
fun SkyIconButton(
    icon: UiIconType,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(SkyTheme.size.iconContainerLg)
            .clip(CircleShape)
            .background(SkyTheme.colors.chip)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        UiIcon(
            icon = icon,
            size = SkyTheme.size.iconLgPlus,
            tint = SkyTheme.colors.textHigh,
            contentDescription = contentDescription,
        )
    }
}
