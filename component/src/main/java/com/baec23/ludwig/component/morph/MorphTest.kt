package com.baec23.ludwig.component.morph

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.flatten
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

val TAG = "MorpherTest"

@Composable
fun Morpher() {
    val pathString = "M382-240 154-468l57-57 171 171 367-367 57 57-424 424Z"
    val path = PathParser().parsePathString(pathString).toPath()
//    val path = PathParser().parsePathString(
//        "M13.63 248.31C13.63 248.31 51.84 206.67 84.21 169.31C140" +
//                ".84 103.97 202.79 27.66 150.14 14.88C131.01 10.23 116.36 29.88 107.26 45.33C69.7 108.92 58.03 214.33 57.54 302.57C67.75 271.83 104.43 190.85 140.18 193.08C181.47 195.65 145.26 257.57 154.53 284.39C168.85 322.18 208.22 292.83 229.98 277.45C265.92 252.03 288.98 231.22 288.98 200.45C288.98 161.55 235.29 174.02 223.3 205.14C213.93 229.44 214.3 265.89 229.3 284.14C247.49 306.28 287.67 309.93 312.18 288.46C337 266.71 354.66 234.56 368.68 213.03C403.92 158.87 464.36 86.15 449.06 30.03C446.98 22.4 440.36 16.57 432.46 16.26C393.62 14.75 381.84 99.18 375.35 129.31C368.78 159.83 345.17 261.31 373.11 293.06C404.43 328.58 446.29 262.4 464.66 231.67C468.66 225.31 472.59 218.43 476.08 213.07C511.33 158.91 571.77 86.19 556.46 30.07C554.39 22.44 547.77 16.61 539.87 16.3C501.03 14.79 489.25 99.22 482.76 129.35C476.18 159.87 452.58 261.35 480.52 293.1C511.83 328.62 562.4 265.53 572.64 232.86C587.34 185.92 620.94 171.58 660.91 180.29C616 166.66 580.86 199.67 572.64 233.16C566.81 256.93 573.52 282.16 599.25 295.77C668.54 332.41 742.8 211.69 660.91 180.29C643.67 181.89 636.15 204.77 643.29 227.78C654.29 263.97 704.29 268.27 733.08 256"
//    ).toPath()
    val bounds = path.getBounds()
    Log.d(TAG, "Morpher: ${bounds}")
    val totalLength = remember {
        val pathMeasure = PathMeasure()
        pathMeasure.setPath(path, false)
        pathMeasure.length
    }
    val boxWidth = 400.dp.value // Assuming this is the width of your Box
    val boxHeight = boxWidth * (bounds.height / bounds.width) // Maintain aspect ratio
    val scaleFactor = remember {

        val scaleX = boxWidth / bounds.width
        val scaleY = boxHeight / bounds.height
        minOf(
            scaleX,
            scaleY
        ) // Choose the smaller scale factor to ensure the path fits within the box
    }


    val scaledPath = android.graphics.Path(path.asAndroidPath()).apply {

        transform(android.graphics.Matrix().apply {
            postScale(scaleFactor, scaleFactor)
            postTranslate(0f, -bounds.center.y) // Adjust for any negative offsets
        })
    }

    val lines = remember {
        scaledPath.flatten()
    }

    val pathData = remember {
        val offsets = mutableListOf<Offset>()
        lines.forEach { line ->
            offsets.add(Offset(line.start.x, line.start.y))
            offsets.add(Offset(line.end.x, line.end.y))
        }
        PathData.fromOffsets(offsets)
    }
    val (circleCenter, circleRadius) = findCircleEncompassingPoints(pathData.startOffsets)

    val progress = remember {
        Animatable(0f)
    }
    LaunchedEffect(Unit) {
        progress.animateTo(
            1f,
            animationSpec = infiniteRepeatable(tween(2000))
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier
            .padding(top = 32.dp, start = 64.dp, bottom = 32.dp, end = 32.dp)
            .aspectRatio(bounds.width / bounds.height)
            .size(400.dp)
            .align(Alignment.Center),
            onDraw = {
                drawCircle(color = Color.Red, center = circleCenter, radius = circleRadius)
                val interpolatedPoints = pathData.startOffsets.mapIndexed { index, offset ->
                    lerpOffset(
                        offset,
                        pathData.targetOffsets[index],
                        progress.value
                    )
                }
                val nextInterpolatedPoints = pathData.startOffsets.mapIndexed { index, offset ->
                    lerpOffset(
                        offset,
                        pathData.targetOffsets[index],
                        progress.value+0.05f
                    )
                }
                val controlPoints = generateSmoothControlPoints(nextInterpolatedPoints)

                val interpolatedPath = Path().apply {
                    moveTo(pathData.start.x, pathData.start.y)
                    for (i in 0 until interpolatedPoints.size){
                        quadraticBezierTo(controlPoints[i].x,controlPoints[i].y,interpolatedPoints[i].x,interpolatedPoints[i].y)
//                        lineTo(interpolatedPoints[i].x,interpolatedPoints[i].y)
                    }
                }
                drawPath(path = interpolatedPath, color = Color.Blue)
                drawPoints(points = controlPoints, color=Color.Green, pointMode = PointMode.Points, strokeWidth = 10f)
            })
    }

}

fun findCircleEncompassingPoints(points: List<Offset>): Pair<Offset, Float> {
    // Calculate centroid
    val centroid = points.fold(Offset(0f, 0f)) { acc, offset ->
        Offset(acc.x + offset.x, acc.y + offset.y)
    } / points.size.toFloat()

    // Find the farthest distance from centroid
    val radius = points.maxOf { offset ->
        sqrt((offset.x - centroid.x).pow(2) + (offset.y - centroid.y).pow(2))
    }

    return Pair(centroid, radius)
}

fun getCirclePoints(center: Offset, radius: Float, numberOfPoints: Int): List<Offset>{
    val points = mutableListOf<Offset>()
    val angleStep = 2 * Math.PI / numberOfPoints

    for (i in 0 until numberOfPoints) {
        val angle = angleStep * i
        val x = center.x + radius * cos(angle).toFloat()
        val y = center.y + radius * sin(angle).toFloat()
        points.add(Offset(x, y))
    }
    return points.toList()
}

fun generateSmoothControlPoints(points: List<Offset>): List<Offset> {
    if (points.size < 2) throw IllegalArgumentException("At least two points are required")

    val controlPoints = mutableListOf<Offset>()

    for (i in 0 until points.size - 1) {
        val current = points[i]
        val next = if (i < points.size - 1) points[i + 1] else points[i]

        // Calculate midpoints
        val midPointNext = Offset((current.x + next.x) / 2, (current.y + next.y) / 2)

        if (i == 0) {
            // For the first point, add it as a control point
            controlPoints.add(current)
        }

        // Add midpoint as a control point for all segments
        controlPoints.add(midPointNext)
    }

    // Add the last point as a control point
    controlPoints.add(points.last())

    return controlPoints
}

data class PathData(
    val start: Offset,
    val startOffsets: List<Offset>,
    val targetOffsets: List<Offset>,
) {
    companion object Builder {
        fun fromOffsets(offsets: List<Offset>): PathData {
            val (circleCenter, circleRadius) = findCircleEncompassingPoints(offsets)
            val circlePoints =
                getCirclePoints(circleCenter, circleRadius, offsets.size)
            var smallestDistanceIndex = 0
            var smallestDistance = Float.MAX_VALUE
            circlePoints.forEachIndexed { index, circlePoint ->
                val offset = offsets[index]
                val distance = sqrt(
                    (circlePoint.x - offset.x).pow(2) +
                            (circlePoint.y - offset.y).pow(2)
                )
                if(distance < smallestDistance){
                    smallestDistance = distance
                    smallestDistanceIndex = index
                }
            }
            val rotatedOffsets = offsets.rotateToIndex(smallestDistanceIndex)
            val start = rotatedOffsets.first()
            return PathData(start, rotatedOffsets, circlePoints)
        }
    }
}

fun <T> List<T>.rotateToIndex(index: Int): List<T> {
    if (this.isEmpty() || index !in indices) {
        throw IllegalArgumentException("Invalid index")
    }
    return this.subList(index, this.size) + this.subList(0, index)
}
fun lerpOffset(start: Offset, stop: Offset, fraction: Float): Offset {
    return Offset(
        x = lerp(start.x, stop.x, fraction),
        y = lerp(start.y, stop.y, fraction)
    )
}

fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return (1 - fraction) * start + fraction * stop
}

data class QuadData(
    val controlPoint: Offset,
    val position: Offset
)


@Preview
@Composable
private fun MorpherPreview() {
    Morpher()

}