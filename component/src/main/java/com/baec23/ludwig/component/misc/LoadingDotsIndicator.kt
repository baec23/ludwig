@file:OptIn(ExperimentalAnimationApi::class)

package com.baec23.ludwig.component.misc

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
@Composable
internal fun LoadingDotsIndicator(
    modifier: Modifier = Modifier,
    dotSpacing: Dp = 8.dp
) {
    var loadingAnimationState by remember { mutableStateOf(LoadingAnimationState.Blank) }

    LaunchedEffect(loadingAnimationState) {
        delay(400)
        loadingAnimationState = when (loadingAnimationState) {
            LoadingAnimationState.Blank -> {
                LoadingAnimationState.FirstDotVisible
            }

            LoadingAnimationState.FirstDotVisible -> {
                LoadingAnimationState.SecondDotVisible
            }

            LoadingAnimationState.SecondDotVisible -> {
                LoadingAnimationState.ThirdDotVisible
            }

            LoadingAnimationState.ThirdDotVisible -> {
                delay(400)
                LoadingAnimationState.Blank
            }
        }
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dotSpacing)
        ) {
            Box(
                modifier = Modifier.weight(0.3f),
                contentAlignment = Alignment.Center
            ) {
                LoadingDot(
                    isVisible = loadingAnimationState != LoadingAnimationState.Blank
                )
            }
            Box(
                modifier = Modifier.weight(0.3f),
                contentAlignment = Alignment.Center
            ) {
                LoadingDot(
                    isVisible = loadingAnimationState == LoadingAnimationState.SecondDotVisible || loadingAnimationState == LoadingAnimationState.ThirdDotVisible
                )
            }
            Box(
                modifier = Modifier.weight(0.3f),
                contentAlignment = Alignment.Center
            ) {
                LoadingDot(
                    isVisible = loadingAnimationState == LoadingAnimationState.ThirdDotVisible
                )
            }
        }
    }
}

@Composable
fun LoadingDot(
    modifier: Modifier = Modifier,
    isVisible: Boolean = false
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ),
        exit = fadeOut()
    ) {
        Box(
            modifier = modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(Color.Black)
        )
    }
}

private enum class LoadingAnimationState {
    Blank,
    FirstDotVisible,
    SecondDotVisible,
    ThirdDotVisible
}