package com.baec23.ludwig.core.spinner

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.times
import kotlin.math.abs

@Composable
fun BoundedSpinner(
    modifier: Modifier = Modifier,
    items: List<String>,
    numRows: Int = 5,
    textStyle: TextStyle = MaterialTheme.typography.displaySmall,
    unselectedTextColor: Color = Color.Unspecified,
    selectedTextColor: Color = Color.Unspecified,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    initialSelectedIndex: Int = 0,
    offsetScaleMultiplier: Float = 0.25f,
    offsetAlphaMultiplier: Float = 0.25f,
    offsetRotationMultiplier: Float = 25f,
    onSelectedIndexChanged: (Int) -> Unit,
) {
    var currIndex by rememberSaveable { mutableStateOf(initialSelectedIndex) }

    val itemHeightDp = with(LocalDensity.current) { textStyle.lineHeight.toDp() }
    val itemHeightPx = with(LocalDensity.current) { textStyle.lineHeight.toPx() }
    val totalHeight = itemHeightPx * items.size
    val rowOffset = (numRows) / 2
    val longestItemLength = items.maxOf { it.length }

    var rawScrollOffset by rememberSaveable { mutableStateOf(currIndex * itemHeightPx + itemHeightPx / 2) }
    val scrollableState = rememberScrollableState { delta ->
        rawScrollOffset = java.lang.Float.min(
            java.lang.Float.max((rawScrollOffset - delta) % totalHeight, itemHeightPx / 2),
            totalHeight - itemHeightPx / 2
        )
        delta
    }

    var itemDisplayOffset by rememberSaveable { mutableStateOf(0f) }

    LaunchedEffect(rawScrollOffset) {
        currIndex = (rawScrollOffset / itemHeightPx).toInt()
        itemDisplayOffset = rawScrollOffset % itemHeightPx
    }

    LaunchedEffect(currIndex) {
        onSelectedIndexChanged(currIndex)
    }

    LaunchedEffect(scrollableState.isScrollInProgress) {
        if (!scrollableState.isScrollInProgress) {
            val currIndexCenterOffset = currIndex * itemHeightPx + itemHeightPx / 2
            animate(
                initialValue = rawScrollOffset,
                targetValue = currIndexCenterOffset,
                animationSpec = spring(
                    dampingRatio = 0.65f, stiffness = 1000f
                )
            ) { value: Float, _ ->
                rawScrollOffset = value
            }
        }
    }

    Box(
        modifier = modifier.scrollable(
            state = scrollableState, orientation = Orientation.Vertical
        )
    ) {
        Column(
            modifier = Modifier
                .width(longestItemLength * itemHeightDp / 1.5f)
                .padding(vertical = itemHeightDp / 4), horizontalAlignment = horizontalAlignment
        ) {
            for (i in currIndex - rowOffset until currIndex) {
                if (i >= 0 && i < items.size) {
                    SpinnerItem(
                        text = items[i],
                        textStyle = textStyle.copy(color = unselectedTextColor),
                        itemDisplayOffset = itemDisplayOffset,
                        offsetScaleMultiplier = offsetScaleMultiplier,
                        offsetAlphaMultiplier = offsetAlphaMultiplier,
                        offsetRotationMultiplier = offsetRotationMultiplier,
                        indexOffset = i - currIndex
                    )
                } else {
                    val spacerHeight =
                        (1f - (abs(i - currIndex) * offsetScaleMultiplier)) * itemHeightDp
                    Spacer(modifier = Modifier.height(spacerHeight))
                }
            }
            SpinnerItem(
                text = items[currIndex],
                textStyle = textStyle.copy(color = selectedTextColor),
                itemDisplayOffset = itemDisplayOffset,
                offsetScaleMultiplier = offsetScaleMultiplier,
                offsetAlphaMultiplier = offsetAlphaMultiplier,
                offsetRotationMultiplier = offsetRotationMultiplier,
                indexOffset = 0
            )
            for (i in currIndex + 1..currIndex + rowOffset) {
                if (i >= 0 && i < items.size) {
                    SpinnerItem(
                        text = items[i],
                        textStyle = textStyle.copy(color = unselectedTextColor),
                        itemDisplayOffset = itemDisplayOffset,
                        offsetScaleMultiplier = offsetScaleMultiplier,
                        offsetAlphaMultiplier = offsetAlphaMultiplier,
                        offsetRotationMultiplier = offsetRotationMultiplier,
                        indexOffset = i - currIndex
                    )
                } else {
                    val spacerHeight =
                        (1f - (abs(i - currIndex) * offsetScaleMultiplier)) * itemHeightDp
                    Spacer(modifier = Modifier.height(spacerHeight))
                }
            }
        }
    }
}

@Composable
fun WrappingSpinner(
    modifier: Modifier = Modifier,
    items: List<String>,
    numRows: Int = 5,
    textStyle: TextStyle = MaterialTheme.typography.displaySmall,
    unselectedTextColor: Color = Color.Unspecified,
    selectedTextColor: Color = Color.Unspecified,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    initialSelectedIndex: Int = 0,
    offsetScaleMultiplier: Float = 0.25f,
    offsetAlphaMultiplier: Float = 0.25f,
    offsetRotationMultiplier: Float = 25f,
    onSelectedIndexChanged: (Int) -> Unit,
) {
    val wrappingItems = WrappingList(items)
    var currIndex by rememberSaveable { mutableStateOf(initialSelectedIndex) }

    val itemHeightDp = with(LocalDensity.current) { textStyle.lineHeight.toDp() }
    val itemHeightPx = with(LocalDensity.current) { textStyle.lineHeight.toPx() }
    val totalHeight = itemHeightPx * items.size
    val rowOffset = (numRows) / 2
    val longestItemLength = items.maxOf { it.length }

    var rawScrollOffset by rememberSaveable { mutableStateOf(currIndex * itemHeightPx + itemHeightPx / 2) }
    val scrollableState = rememberScrollableState { delta ->
        rawScrollOffset = (rawScrollOffset - delta) % totalHeight
        if (rawScrollOffset < 0) rawScrollOffset += totalHeight
        delta
    }

    var itemDisplayOffset by rememberSaveable { mutableStateOf(0f) }

    LaunchedEffect(rawScrollOffset) {
        currIndex = (rawScrollOffset / itemHeightPx).toInt()
        itemDisplayOffset = rawScrollOffset % itemHeightPx
    }

    LaunchedEffect(currIndex) {
        onSelectedIndexChanged(currIndex)
    }

    LaunchedEffect(scrollableState.isScrollInProgress) {
        if (!scrollableState.isScrollInProgress) {
            val currIndexCenterOffset = currIndex * itemHeightPx + itemHeightPx / 2
            animate(
                initialValue = rawScrollOffset,
                targetValue = currIndexCenterOffset,
                animationSpec = spring(
                    dampingRatio = 0.65f, stiffness = 1000f
                )
            ) { value: Float, _ ->
                rawScrollOffset = value
            }
        }
    }

    Box(
        modifier = modifier.scrollable(
            state = scrollableState, orientation = Orientation.Vertical
        )
    ) {
        Column(
            modifier = Modifier
                .width(longestItemLength * itemHeightDp / 2f)
                .padding(vertical = itemHeightDp / 4), horizontalAlignment = horizontalAlignment
        ) {
            for (i in currIndex - rowOffset until currIndex) {
                SpinnerItem(
                    text = wrappingItems[i],
                    textStyle = textStyle.copy(color = unselectedTextColor),
                    itemDisplayOffset = itemDisplayOffset,
                    offsetScaleMultiplier = offsetScaleMultiplier,
                    offsetAlphaMultiplier = offsetAlphaMultiplier,
                    offsetRotationMultiplier = offsetRotationMultiplier,
                    indexOffset = i - currIndex
                )
            }
            SpinnerItem(
                text = wrappingItems[currIndex],
                textStyle = textStyle.copy(color = selectedTextColor),
                itemDisplayOffset = itemDisplayOffset,
                offsetScaleMultiplier = offsetScaleMultiplier,
                offsetAlphaMultiplier = offsetAlphaMultiplier,
                offsetRotationMultiplier = offsetRotationMultiplier,
                indexOffset = 0
            )
            for (i in currIndex + 1..currIndex + rowOffset) {
                SpinnerItem(
                    text = wrappingItems[i],
                    textStyle = textStyle.copy(color = unselectedTextColor),
                    itemDisplayOffset = itemDisplayOffset,
                    offsetScaleMultiplier = offsetScaleMultiplier,
                    offsetAlphaMultiplier = offsetAlphaMultiplier,
                    offsetRotationMultiplier = offsetRotationMultiplier,
                    indexOffset = i - currIndex
                )
            }
        }
    }
}

@Composable
fun SpinnerItem(
    text: String,
    textStyle: TextStyle,
    itemDisplayOffset: Float,
    indexOffset: Int,
    offsetScaleMultiplier: Float = 0.25f,
    offsetAlphaMultiplier: Float = 0.25f,
    offsetRotationMultiplier: Float = 25f,
) {
    val indexOffsetMultiplier = 1f - abs(indexOffset) * offsetScaleMultiplier
    val itemHeightDp = with(LocalDensity.current) { textStyle.lineHeight.toDp() }
    val itemHeightPx = with(LocalDensity.current) { textStyle.lineHeight.toPx() }
    val mTextStyle = textStyle.copy(fontSize = textStyle.fontSize * indexOffsetMultiplier)
    Text(
        modifier = Modifier
            .height(itemHeightDp * indexOffsetMultiplier)
            .graphicsLayer {
                translationY = (itemHeightPx / 2 - itemDisplayOffset) * indexOffsetMultiplier
                if (indexOffset == 0) {
                    val offsetFromCenter = abs(itemHeightPx / 2 - itemDisplayOffset)
                    if (itemDisplayOffset < itemHeightPx / 2) {
                        rotationX =
                            -(offsetRotationMultiplier * offsetFromCenter / (itemHeightPx / 2))
                    } else if (itemDisplayOffset > itemHeightPx / 2) {
                        rotationX = offsetRotationMultiplier * offsetFromCenter / (itemHeightPx / 2)
                    }
                    scaleY = 1f - (offsetScaleMultiplier * offsetFromCenter / itemHeightPx / 2)
                    scaleX = 1f - (offsetScaleMultiplier * offsetFromCenter / itemHeightPx / 2)
                    alpha = 1f - (offsetScaleMultiplier * offsetFromCenter / itemHeightPx / 2)
                } else if (indexOffset < 0) {
                    rotationX =
                        -((indexOffset * offsetRotationMultiplier) - (offsetRotationMultiplier * itemDisplayOffset / itemHeightPx))
                    alpha =
                        1f - (offsetAlphaMultiplier * abs(indexOffset)) - (offsetAlphaMultiplier * itemDisplayOffset / itemHeightPx)
                } else {
                    rotationX =
                        -((indexOffset * offsetRotationMultiplier) + (offsetRotationMultiplier * (itemHeightPx - itemDisplayOffset) / itemHeightPx))
                    alpha =
                        1f - (offsetAlphaMultiplier * abs(indexOffset)) - (offsetAlphaMultiplier * (itemHeightPx - itemDisplayOffset) / itemHeightPx)
                }
            }, text = text, style = mTextStyle
    )
}