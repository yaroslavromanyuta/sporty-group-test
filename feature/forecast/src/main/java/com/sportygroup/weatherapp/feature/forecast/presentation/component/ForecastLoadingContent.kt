package com.sportygroup.weatherapp.feature.forecast.presentation.component

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.sportygroup.weatherapp.core.designsystem.preview.SkyPreview
import com.sportygroup.weatherapp.core.designsystem.preview.ThemePreviews
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sportygroup.weatherapp.core.designsystem.component.SkyCard
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme
import com.sportygroup.weatherapp.feature.forecast.R

/** Shimmer skeleton shown while the first forecast loads. */
@Composable
fun ForecastLoadingContent(modifier: Modifier = Modifier) {
    val spacing = SkyTheme.spacing
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = spacing.screenHorizontal, vertical = spacing.sm),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = spacing.m),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ShimmerBox(width = SkeletonSize.cityNameWidth, height = SkeletonSize.cityNameHeight)
            ShimmerBox(width = SkeletonSize.unitToggleWidth, height = SkeletonSize.unitToggleHeight)
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = spacing.m),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.lgPlus),
        ) {
            ShimmerBox(width = SkeletonSize.heroIcon, height = SkeletonSize.heroIcon)
            ShimmerBox(
                width = SkeletonSize.heroTempWidth,
                height = SkeletonSize.heroTempHeight,
                shape = SkyTheme.shapes.row,
            )
            ShimmerBox(width = SkeletonSize.heroCaptionWidth, height = SkeletonSize.heroCaptionHeight)
        }
        Spacer(Modifier.height(spacing.ml))
        SkyCard(contentPadding = androidx.compose.foundation.layout.PaddingValues(spacing.ml)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                repeat(5) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(spacing.md),
                    ) {
                        ShimmerBox(width = SkeletonSize.chipLabelWidth, height = SkeletonSize.chipLabelHeight)
                        ShimmerBox(width = SkeletonSize.chipIcon, height = SkeletonSize.chipIcon)
                        ShimmerBox(width = SkeletonSize.chipTempWidth, height = SkeletonSize.chipTempHeight)
                    }
                }
            }
        }
        Spacer(Modifier.height(spacing.xxl))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(SkyTheme.size.progressIndicator),
                strokeWidth = SkyTheme.size.stroke,
                color = SkyTheme.colors.primary,
            )
            Text(
                text = stringResource(R.string.forecast_loading),
                color = SkyTheme.colors.textMedium,
                style = SkyTheme.typography.label,
                modifier = Modifier.padding(start = spacing.sm),
            )
        }
    }
}

/**
 * Bespoke geometry for the loading skeleton's shimmer blocks. These are private placeholder
 * dimensions (they mimic the real content's layout) rather than shared design tokens, so they
 * live here as named values instead of inline literals or [SkyTheme.size] entries.
 */
private object SkeletonSize {
    val cityNameWidth = 150.dp
    val cityNameHeight = 26.dp
    val unitToggleWidth = 78.dp
    val unitToggleHeight = 36.dp
    val heroIcon = 120.dp
    val heroTempWidth = 140.dp
    val heroTempHeight = 60.dp
    val heroCaptionWidth = 120.dp
    val heroCaptionHeight = 18.dp
    val chipLabelWidth = 34.dp
    val chipLabelHeight = 12.dp
    val chipIcon = 32.dp
    val chipTempWidth = 28.dp
    val chipTempHeight = 14.dp
}

@Composable
private fun ShimmerBox(width: Dp, height: Dp, shape: Shape = SkyTheme.shapes.pill) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1300),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmer-progress",
    )
    val base = SkyTheme.colors.track
    val highlight = if (SkyTheme.colors.isDark) Color.White.copy(alpha = 0.08f) else Color.White
    val brush = Brush.linearGradient(
        colors = listOf(base, highlight, base),
        start = Offset(progress * 300f - 300f, 0f),
        end = Offset(progress * 300f, 0f),
    )
    Box(
        modifier = Modifier
            .size(width = width, height = height)
            .clip(shape)
            .background(base)
            .background(brush),
    )
}

@ThemePreviews
@Composable
private fun ForecastLoadingContentPreview() {
    SkyPreview {
        ForecastLoadingContent()
    }
}