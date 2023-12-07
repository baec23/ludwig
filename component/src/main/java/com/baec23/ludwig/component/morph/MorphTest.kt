package com.baec23.ludwig.component.morph

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
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
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.dp
import androidx.core.graphics.flatten
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

val TAG = "MorpherTest"

@Composable
fun Morpher() {
//    val pathString = "M382-240 154-468l57-57 171 171 367-367 57 57-424 424Z"
    val pathString =
        "M400-80v-164l294-292q12-12 26.5-18t30.5-6q16 0 30.5 6t26.5 18l49 50q11 12 17 26.5t6 29.5q0 15-6.5 29.5T856-374L564-80H400Zm397-348-50-49 50 49ZM480-160h50l162-162-25-25-25-25-162 162v50Zm187-187-25-25 25 25 25 25-25-25ZM430-590l-56-57 73-73H313q-9 26-28 45t-45 28v334q35 13 57.5 43.5T320-200q0 50-35 85t-85 35q-50 0-85-35t-35-85q0-39 22.5-69t57.5-43v-335q-35-13-57.5-43.5T80-760q0-50 35-85t85-35q39 0 69.5 22.5T313-800h134l-73-73 56-57 170 170-170 170Zm330-290q50 0 85 35t35 85q0 50-35 85t-85 35q-50 0-85-35t-35-85q0-50 35-85t85-35ZM200-160q17 0 28.5-11.5T240-200q0-17-11.5-28.5T200-240q-17 0-28.5 11.5T160-200q0 17 11.5 28.5T200-160Zm0-560q17 0 28.5-11.5T240-760q0-17-11.5-28.5T200-800q-17 0-28.5 11.5T160-760q0 17 11.5 28.5T200-720Zm560 0q17 0 28.5-11.5T800-760q0-17-11.5-28.5T760-800q-17 0-28.5 11.5T720-760q0 17 11.5 28.5T760-720ZM200-200Zm0-560Zm560 0Z"
//    val pathString = "m384-294 96-74 96 74-36-122 90-64H518l-38-124-38 124H330l90 64-36 122ZM233-80l93-304L80-560h304l96-320 96 320h304L634-384l93 304-247-188L233-80Zm247-369Z"

    val a = PathParser().parsePathString(pathString)
    val pathNodes = a.toNodes()
    val subpathNodes = pathNodes.splitIntoClosedPaths()
    Log.d(TAG, "${subpathNodes.size}")

    val path = PathParser().parsePathString(pathString).toPath()

    val bounds = path.getBounds()
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
        scaledPath.flatten(50f)
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


    val aNodes =
        androidx.core.graphics.PathParser.createNodesFromPathData("M382-240 154-468l57-57 171 171 367-367 57 57-424 424Z")
    val bNodes =
        androidx.core.graphics.PathParser.createNodesFromPathData("M647-440H160v-80h487L423-744l57-56 320 320-320 320-57-56 224-224Z")

    val testPath = Path().apply {
        val points = pathData.targetOffsets
        moveTo(points.first().x, points.first().y)
        for (i in points.indices) {
            lineTo(points[i].x, points[i].y)
        }
        lineTo(points[0].x, points[0].y)
    }


    Log.d(TAG, "Morpher: Can Morph? ${androidx.core.graphics.PathParser.canMorph(aNodes, bNodes)}")

    LaunchedEffect(Unit) {
        progress.animateTo(
            1f,
            animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse)
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier
            .padding(top = 32.dp, start = 64.dp, bottom = 32.dp, end = 32.dp)
            .aspectRatio(bounds.width / bounds.height)
            .size(400.dp)
            .align(Alignment.Center),
            onDraw = {
                drawCircle(color = Color.LightGray, center = circleCenter, radius = circleRadius)
                val interpolatedPoints = pathData.startOffsets.mapIndexed { index, offset ->
                    lerpOffset(
                        offset,
                        pathData.targetOffsets[index],
                        progress.value
                    )
                }
                val interpolatedPath = Path().apply {
                    moveTo(interpolatedPoints.first().x, interpolatedPoints.first().y)
                    for (i in interpolatedPoints.indices) {
                        lineTo(interpolatedPoints[i].x, interpolatedPoints[i].y)
                    }
                    lineTo(interpolatedPoints[0].x, interpolatedPoints[0].y)
                }
                drawPath(path = interpolatedPath, color = Color.Black, style = Stroke(width = 5f))
//                drawPath(path = interpolatedPath, color = Color.Black)
//                drawPoints(
//                    points = interpolatedPoints,
//                    color = Color.Green,
//                    pointMode = PointMode.Points,
//                    strokeWidth = 20f
//                )
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

fun getCirclePoints(center: Offset, radius: Float, numberOfPoints: Int): List<Offset> {
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
            var smallestDistanceCircleIndex = 0
            var smallestDistanceOffsetsIndex = 0
            var smallestDistance = Float.MAX_VALUE

            circlePoints.forEachIndexed { circleIndex, circlePoint ->
                offsets.forEachIndexed { offsetIndex, offset ->
                    val distance = sqrt(
                        (circlePoint.x - offset.x).pow(2) +
                                (circlePoint.y - offset.y).pow(2)
                    )
                    if (distance < smallestDistance) {
                        smallestDistance = distance
                        smallestDistanceCircleIndex = circleIndex
                        smallestDistanceOffsetsIndex = offsetIndex
                    }
                }
            }
//            Log.d(TAG, "fromOffsets: smallestdistanceIndex = ${smallestDistanceCircleIndex}")
            val rotatedOffsets = offsets.rotateToIndex(smallestDistanceOffsetsIndex)
            val rotatedCirclePoints = circlePoints.rotateToIndex(smallestDistanceCircleIndex)
            val start = rotatedOffsets.first()
            return PathData(start, rotatedOffsets, rotatedCirclePoints)
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
