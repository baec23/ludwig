package com.baec23.ludwig.core.fadinglazy

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun FadingLazyRow(
    modifier: Modifier = Modifier,
    gradientWidthPercent: Float = 0.25f,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    horizontalArrangement: Arrangement.Horizontal =
        if (!reverseLayout) Arrangement.Start else Arrangement.End,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: LazyListScope.() -> Unit
) {
    val firstItemVisiblePercent = state.getVisiblePercentForFirstItem()
    val lastItemVisiblePercent = state.getVisiblePercentForLastItem()

    Box(modifier = modifier
        .addHorizontalFade(
            firstRowVisiblePercent = firstItemVisiblePercent,
            lastRowVisiblePercent = lastItemVisiblePercent,
            gradientWidthPercent = gradientWidthPercent
        )
    ) {
        LazyRow(
            modifier = modifier,
            state = state,
            contentPadding = contentPadding,
            reverseLayout = reverseLayout,
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment,
            flingBehavior = flingBehavior,
            userScrollEnabled = userScrollEnabled,
            content = content,
        )
    }
}

@Composable
fun FadingLazyColumn(
    modifier: Modifier = Modifier,
    gradientHeightPercent: Float = 0.25f,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: LazyListScope.() -> Unit
) {
    val firstItemVisiblePercent = state.getVisiblePercentForFirstItem()
    val lastItemVisiblePercent = state.getVisiblePercentForLastItem()

    Box(
        modifier = modifier
            .addVerticalFade(
                firstRowVisiblePercent = firstItemVisiblePercent,
                lastRowVisiblePercent = lastItemVisiblePercent,
                gradientHeightPercent = gradientHeightPercent
            )
    ) {
        LazyColumn(
            modifier = modifier,
            state = state,
            contentPadding = contentPadding,
            reverseLayout = reverseLayout,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            flingBehavior = flingBehavior,
            userScrollEnabled = userScrollEnabled,
            content = content,
        )
    }
}

@Composable
fun FadingLazyHorizontalGrid(
    modifier: Modifier = Modifier,
    gradientWidthPercent: Float = 0.25f,
    rows: GridCells,
    state: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    horizontalArrangement: Arrangement.Horizontal =
        if (!reverseLayout) Arrangement.Start else Arrangement.End,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: LazyGridScope.() -> Unit
) {
    val firstColumnVisiblePercent = state.getVisiblePercentForFirstColumn()
    val lastColumnVisiblePercent = state.getVisiblePercentForLastColumn()

    Box(modifier = modifier
        .addHorizontalFade(
            firstRowVisiblePercent = firstColumnVisiblePercent,
            lastRowVisiblePercent = lastColumnVisiblePercent,
            gradientWidthPercent = gradientWidthPercent
        )
    ) {
        LazyHorizontalGrid(
            modifier = modifier,
            rows = rows,
            state = state,
            contentPadding = contentPadding,
            reverseLayout = reverseLayout,
            horizontalArrangement = horizontalArrangement,
            verticalArrangement = verticalArrangement,
            flingBehavior = flingBehavior,
            userScrollEnabled = userScrollEnabled,
            content = content,
        )
    }
}

@Composable
fun FadingLazyVerticalGrid(
    modifier: Modifier = Modifier,
    gradientHeightPercent: Float = 0.25f,
    columns: GridCells,
    state: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: LazyGridScope.() -> Unit
) {
    val firstRowVisiblePercent = state.getVisiblePercentForFirstRow()
    val lastRowVisiblePercent = state.getVisiblePercentForLastRow()

    Box(
        modifier = modifier
            .addVerticalFade(
                firstRowVisiblePercent = firstRowVisiblePercent,
                lastRowVisiblePercent = lastRowVisiblePercent,
                gradientHeightPercent = gradientHeightPercent
            )
    ) {
        LazyVerticalGrid(
            modifier = modifier,
            columns = columns,
            state = state,
            contentPadding = contentPadding,
            reverseLayout = reverseLayout,
            horizontalArrangement = horizontalArrangement,
            verticalArrangement = verticalArrangement,
            flingBehavior = flingBehavior,
            userScrollEnabled = userScrollEnabled,
            content = content,
        )
    }
}

internal fun Modifier.addVerticalFade(
    firstRowVisiblePercent: Float,
    lastRowVisiblePercent: Float,
    gradientHeightPercent: Float,
): Modifier {
    return this.run {
        if (firstRowVisiblePercent < 1f) {
            this
                .graphicsLayer { alpha = 0.99f }
                .drawWithContent {
                    val colors = listOf(
                        Color.Transparent,
                        Color.Black,
                    )
                    drawContent()
                    val gradientHeight = size.height * gradientHeightPercent
                    drawRect(
                        size = Size(width = size.width, height = gradientHeight),
                        brush = Brush.verticalGradient(
                            colors,
                            endY = gradientHeight - (gradientHeight * firstRowVisiblePercent)
                        ),
                        blendMode = BlendMode.DstIn
                    )
                }
        } else {
            this
        }
    }.run {
        if (lastRowVisiblePercent < 1f) {
            this
                .graphicsLayer { alpha = 0.99f }
                .drawWithContent {
                    val colors = listOf(
                        Color.Black,
                        Color.Transparent,
                    )
                    drawContent()

                    val gradientHeight = size.height * gradientHeightPercent
                    drawRect(
                        topLeft = Offset(x = 0f, y = size.height - gradientHeight),
                        brush = Brush.verticalGradient(
                            colors,
                            startY = (size.height - gradientHeight) + (gradientHeight) * lastRowVisiblePercent
                        ),
                        blendMode = BlendMode.DstIn
                    )
                }
        } else {
            this
        }
    }
}

internal fun Modifier.addHorizontalFade(
    firstRowVisiblePercent: Float,
    lastRowVisiblePercent: Float,
    gradientWidthPercent: Float
): Modifier {
    return this.run {
        if (firstRowVisiblePercent < 1f) {
            this
                .graphicsLayer { alpha = 0.99f }
                .drawWithContent {
                    val colors = listOf(
                        Color.Transparent,
                        Color.Black,
                    )
                    drawContent()
                    val gradientWidth = size.width * gradientWidthPercent
                    drawRect(
                        size = Size(width = gradientWidth, height = size.height),
                        brush = Brush.horizontalGradient(
                            colors,
                            endX = gradientWidth - (gradientWidth * firstRowVisiblePercent)
                        ),
                        blendMode = BlendMode.DstIn
                    )
                }
        } else {
            this
        }
    }.run {
        if (lastRowVisiblePercent < 1f) {
            this
                .graphicsLayer { alpha = 0.99f }
                .drawWithContent {
                    val colors = listOf(
                        Color.Black,
                        Color.Transparent,
                    )
                    drawContent()
                    val gradientWidth = size.width * gradientWidthPercent
                    drawRect(
                        topLeft = Offset(x = size.width - gradientWidth, y = 0f),
                        brush = Brush.horizontalGradient(
                            colors,
                            startX = (size.width - gradientWidth) + (gradientWidth) * lastRowVisiblePercent
                        ),
                        blendMode = BlendMode.DstIn
                    )
                }
        } else {
            this
        }
    }
}

internal fun LazyListState.getVisiblePercentForFirstItem(): Float {
    val visibleItemsInfo = this.layoutInfo.visibleItemsInfo
    if (visibleItemsInfo.isEmpty())
        return 0f
    val itemInfo = visibleItemsInfo.find { it.index == 0 } ?: return 0f

    val viewportStart = layoutInfo.viewportStartOffset
    val itemEnd = itemInfo.offset + itemInfo.size
    return ((itemEnd - viewportStart.toFloat()) / itemInfo.size)
}

internal fun LazyListState.getVisiblePercentForLastItem(): Float {
    val visibleItemsInfo = this.layoutInfo.visibleItemsInfo
    if (visibleItemsInfo.isEmpty())
        return 0f
    val itemInfo =
        visibleItemsInfo.find { it.index == layoutInfo.totalItemsCount - 1 } ?: return 0f

    val viewportEnd = layoutInfo.viewportEndOffset
    val itemStart = itemInfo.offset
    return ((viewportEnd - itemStart.toFloat()) / itemInfo.size)
}

internal fun LazyGridState.getVisiblePercentForFirstColumn(): Float {
    val visibleItemsInfo = this.layoutInfo.visibleItemsInfo
    if (visibleItemsInfo.isEmpty())
        return 0f

    val itemInfo = visibleItemsInfo.find { it.index == 0 } ?: return 0f
    val itemEnd = itemInfo.offset.x + itemInfo.size.width
    val viewportStart = layoutInfo.viewportStartOffset

    return ((itemEnd - viewportStart.toFloat()) / itemInfo.size.width)
}

internal fun LazyGridState.getVisiblePercentForLastColumn(): Float {
    val visibleItemsInfo = this.layoutInfo.visibleItemsInfo
    if (visibleItemsInfo.isEmpty())
        return 0f

    val itemInfo =
        visibleItemsInfo.find { it.index == this.layoutInfo.totalItemsCount - 1 } ?: return 0f
    val viewportEnd = layoutInfo.viewportEndOffset
    val itemStart = itemInfo.offset.x

    return ((viewportEnd - itemStart.toFloat()) / itemInfo.size.width)
}

internal fun LazyGridState.getVisiblePercentForFirstRow(): Float {
    val visibleItemsInfo = this.layoutInfo.visibleItemsInfo
    if (visibleItemsInfo.isEmpty())
        return 0f

    val itemInfo = visibleItemsInfo.find { it.index == 0 } ?: return 0f
    val itemEnd = itemInfo.offset.y + itemInfo.size.height
    val viewportStart = layoutInfo.viewportStartOffset

    return ((itemEnd - viewportStart.toFloat()) / itemInfo.size.height)
}

internal fun LazyGridState.getVisiblePercentForLastRow(): Float {
    val visibleItemsInfo = this.layoutInfo.visibleItemsInfo
    if (visibleItemsInfo.isEmpty())
        return 0f

    val itemInfo =
        visibleItemsInfo.find { it.index == this.layoutInfo.totalItemsCount - 1 } ?: return 0f
    val viewportEnd = layoutInfo.viewportEndOffset
    val itemStart = itemInfo.offset.y

    return ((viewportEnd - itemStart.toFloat()) / itemInfo.size.height)
}