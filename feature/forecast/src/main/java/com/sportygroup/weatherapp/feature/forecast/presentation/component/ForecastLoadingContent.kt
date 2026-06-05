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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import com.sportygroup.weatherapp.core.designsystem.preview.SkyPreview
import com.sportygroup.weatherapp.core.designsystem.preview.ThemePreviews
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sportygroup.weatherapp.core.designsystem.component.SkyCard
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme

/** Shimmer skeleton shown while the first forecast loads. */
@Composable
fun ForecastLoadingContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ShimmerBox(width = 150.dp, height = 26.dp, radius = 13.dp)
            ShimmerBox(width = 78.dp, height = 36.dp, radius = 18.dp)
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            ShimmerBox(width = 120.dp, height = 120.dp, radius = 60.dp)
            ShimmerBox(width = 140.dp, height = 60.dp, radius = 16.dp)
            ShimmerBox(width = 120.dp, height = 18.dp, radius = 9.dp)
        }
        Spacer(Modifier.height(14.dp))
        SkyCard(contentPadding = androidx.compose.foundation.layout.PaddingValues(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                repeat(5) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        ShimmerBox(width = 34.dp, height = 12.dp, radius = 6.dp)
                        ShimmerBox(width = 32.dp, height = 32.dp, radius = 16.dp)
                        ShimmerBox(width = 28.dp, height = 14.dp, radius = 7.dp)
                    }
                }
            }
        }
        Spacer(Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                strokeWidth = 2.5.dp,
                color = SkyTheme.colors.primary,
            )
            Text(
                text = "Fetching latest forecast…",
                color = SkyTheme.colors.textMedium,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}

@Composable
private fun ShimmerBox(width: Dp, height: Dp, radius: Dp) {
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
            .clip(if (radius >= height / 2) CircleShape else RoundedCornerShape(radius))
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