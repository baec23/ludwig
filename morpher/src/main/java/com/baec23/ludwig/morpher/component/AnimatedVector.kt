package com.baec23.ludwig.morpher.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.baec23.ludwig.morpher.MorphAnimator
import com.baec23.ludwig.morpher.model.VectorSource
import kotlinx.coroutines.async

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
    var morphAnimator by remember { mutableStateOf<MorphAnimator?>(null) }

    LaunchedEffect(startSource, endSource, canvasSize) {
        if (canvasSize.width == 0) {
            return@LaunchedEffect
        }
        val pathData = async {
            MorphAnimator.generatePathData(
                startSource,
                endSource,
                canvasSize.width.toFloat(),
                canvasSize.height.toFloat()
            )
        }.await()
        val animationData = async {
            MorphAnimator.generateAnimationData(pathData = pathData, smoothness = 500)
        }.await()
        morphAnimator = MorphAnimator(pathData, animationData)
    }

    LaunchedEffect(startSource, endSource, canvasSize) {
        if (canvasSize.width == 0) {
            return@LaunchedEffect
        }
        morphAnimator = MorphAnimator(
            start = startSource,
            end = endSource,
            width = canvasSize.width.toFloat(),
            height = canvasSize.height.toFloat()
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
                morphAnimator?.let { animator ->
                    val pairedPaths =
                        animator.getInterpolatedPairedPath(progress)
                    drawPath(
                        pairedPaths,
                        color = strokeColor,
                        style = Stroke(
                            width = strokeWidth,
                            join = StrokeJoin.Round,
                            cap = StrokeCap.Round
                        )
                    )

                    val extraStartPathsAnimationProgress = if (progress <= extraPathsBreakpoint) {
                        progress / extraPathsBreakpoint
                    } else {
                        1f
                    }
                    val extraStartPaths =
                        animator.getInterpolatedUnpairedStartPath(extraStartPathsAnimationProgress)
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


                    val extraEndPathsBreakpoint = 1f - extraPathsBreakpoint
                    val extraEndPathsAnimationProgress = if (progress < extraEndPathsBreakpoint) {
                        0f
                    } else {
                        (progress - extraEndPathsBreakpoint) / (1f - extraEndPathsBreakpoint)
                    }
                    val extraEndPaths =
                        animator.getInterpolatedUnpairedEndPath(1f - extraEndPathsAnimationProgress)
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
            }
        )
    }
}