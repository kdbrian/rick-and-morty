package com.kdbrian.rickmorty.util

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

fun Modifier.shimmer(
    shimmerColors: List<Color> = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.3f),
        Color.LightGray.copy(alpha = 0.6f),
    ),
    durationMillis: Int = 1200,
    angleInDegrees: Float = 20f
): Modifier = composed {

    val transition = rememberInfiniteTransition(label = "shimmerTransition")

    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerProgress"
    )

    val angleInRad = (angleInDegrees / 180f) * PI.toFloat()
    val direction = Offset(
        cos(angleInRad),
        sin(angleInRad)
    )

    this.drawWithCache {

        val width = size.width
        val height = size.height

        // Travel distance diagonal for smooth pass
        val diagonal = sqrt(width * width + height * height)

        val translate = progress * diagonal

        val start = Offset(
            x = -direction.x * diagonal + direction.x * translate,
            y = -direction.y * diagonal + direction.y * translate
        )

        val end = Offset(
            x = direction.x * translate,
            y = direction.y * translate
        )

        val brush = Brush.linearGradient(
            colors = shimmerColors,
            start = start,
            end = end
        )

        onDrawWithContent {
            drawRect(brush = brush)
        }
    }
}
