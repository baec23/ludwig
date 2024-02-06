package com.baec23.ludwig.morpher.component.text

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun AnimatedText(
    modifier: Modifier = Modifier,
    text: String,
) {
    val animationProgress = remember { Animatable(0f) }
    var textChars by remember { mutableStateOf(CharArray(20)) }
    LaunchedEffect(key1 = text) {
        val newCharArray = CharArray(20)
        text.forEachIndexed { index, char ->
            newCharArray[index] = char
        }
        for (i in text.length..<20) {
            newCharArray[i] = ' '
        }
        textChars = newCharArray
    }
    AnimatedTextLayout(modifier = modifier) {

        repeat(20) {
            AnimatedChar(modifier = Modifier.width(18.dp), char = textChars[it], )
        }
    }
}

@Composable
fun AnimatedTextLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(modifier = modifier, content = content) { measurables, constraints ->
        val numItems = measurables.count()
        val maxYOffset = 50

        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }
        val maxHeight = placeables.maxOfOrNull { placeable -> placeable.height } ?: 0
        layout(constraints.maxWidth, maxHeight) {
            var xPos = 0
            placeables.forEachIndexed { index, placeable ->
                placeable.placeRelative(x = xPos, y = 0)
                xPos += placeable.width
            }
        }
    }
}

private fun calcMyProgress(animationProgress: Float, index: Int, numItems: Int): Float {
    val interval = 1f / numItems
    val myRange = index * interval..<(index + 1) * interval
    return when {
        animationProgress < myRange.start -> 0f
        animationProgress > myRange.endExclusive -> 1f
        else -> {
            return (animationProgress - myRange.start) / interval
        }
    }
}

data class AnimatedTextData(
    val yOffset: Int,
    val xOffset: Int,
    val scale: Float,
    val opacity: Float
)

