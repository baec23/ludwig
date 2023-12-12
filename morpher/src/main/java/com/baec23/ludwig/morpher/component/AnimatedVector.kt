package com.baec23.ludwig.morpher.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import com.baec23.ludwig.morpher.VectorAnimator
import com.baec23.ludwig.morpher.model.VectorSource

@Composable
fun AnimatedVector(
    modifier: Modifier = Modifier,
    startSource: VectorSource,
    endSource: VectorSource,
    progress: Float,
    strokeWidth: Float = 20f,
    strokeColor: Color = Color.Black,
    extraPathsBreakpoint: Float = 0.2f
) {
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    val vectorAnimator = remember(key1 = canvasSize) {
        VectorAnimator(
            startSource,
            endSource,
            width = if (canvasSize != IntSize.Zero) canvasSize.width.toFloat() else 50f,
            height = if (canvasSize != IntSize.Zero) canvasSize.height.toFloat() else 50f
        )
    }

    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned { coordinates ->
                    canvasSize = coordinates.size
                },
            onDraw = {
                val pairedPaths =
                    vectorAnimator.getInterpolatedPairedPath(progress)

                val extraStartPathsAnimationProgress = if (progress <= extraPathsBreakpoint) {
                    progress / extraPathsBreakpoint
                } else {
                    1f
                }
                val extraStartPaths =
                    vectorAnimator.getInterpolatedUnpairedStartPath(extraStartPathsAnimationProgress)

                val extraEndPathsBreakpoint = 1f - extraPathsBreakpoint
                val extraEndPathsAnimationProgress = if (progress < extraEndPathsBreakpoint) {
                    0f
                } else {
                    (progress - extraEndPathsBreakpoint) / (1f - extraEndPathsBreakpoint)
                }
                val extraEndPaths =
                    vectorAnimator.getInterpolatedUnpairedEndPath(1f - extraEndPathsAnimationProgress)

                drawPath(
                    pairedPaths,
                    color = strokeColor,
                    style = Stroke(
                        width = strokeWidth,
                        join = StrokeJoin.Round,
                        cap = StrokeCap.Round
                    )
                )

                if (extraStartPathsAnimationProgress < 1f) {
                    drawPath(
                        extraStartPaths,
                        color = strokeColor,
                        style = Stroke(
                            width = strokeWidth,
                            join = StrokeJoin.Round,
                            cap = StrokeCap.Round
                        ),
                        alpha = minOf(maxOf(1f - extraStartPathsAnimationProgress, 0f), 1f)
                    )
                }

                if (extraEndPathsAnimationProgress > 0f) {
                    drawPath(
                        extraEndPaths,
                        color = strokeColor,
                        style = Stroke(
                            width = strokeWidth,
                            join = StrokeJoin.Round,
                            cap = StrokeCap.Round
                        ),
                        alpha = minOf(maxOf(extraEndPathsAnimationProgress, 0f), 1f)
                    )
                }
            }
        )
    }
}