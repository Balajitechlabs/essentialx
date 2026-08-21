/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: UI Module
 * File: ShimmerModifier.kt
 * Description: UI layout element for ShimmerModifier.kt.
 */

package com.sameerasw.essentials.ui.components.modifiers

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.shimmer(): Modifier =
    composed {
        val transition = rememberInfiniteTransition(label = "shimmer")
        val translateAnim =
            transition.animateFloat(
                initialValue = 0f,
                targetValue = 1000f,
                animationSpec =
                    infiniteRepeatable(
                        animation =
                            tween(
                                durationMillis = 1000,
                                easing = FastOutSlowInEasing,
                            ),
                        repeatMode = RepeatMode.Restart,
                    ),
                label = "shimmer_translate",
            )

        val shimmerColors =
            remember {
                listOf(
                    Color.Unspecified,
                    Color.Unspecified,
                    Color.Unspecified,
                )
            }.let {
                listOf(
                    MaterialTheme.colorScheme.surfaceContainerHighest,
                    MaterialTheme.colorScheme.surfaceContainerHigh,
                    MaterialTheme.colorScheme.surfaceContainerHighest,
                )
            }

        this.drawBehind {
            val brush =
                Brush.linearGradient(
                    colors = shimmerColors,
                    start = Offset(translateAnim.value - 500f, translateAnim.value - 500f),
                    end = Offset(translateAnim.value, translateAnim.value),
                )
            drawRect(brush)
        }
    }
