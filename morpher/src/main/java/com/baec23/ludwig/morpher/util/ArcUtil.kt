package com.baec23.ludwig.morpher.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.PathNode
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

//internal fun approximateArcToCurves(
//    start: Offset,
//    verticalEllipseRadius: Float,
//    horizontalEllipseRadius: Float,
//    isPositiveArc: Boolean,
//    isMoreThanHalf: Boolean,
//    theta: Float,
//    end: Offset
//): List<PathNode.CurveTo> {
//
//
//    //TODO: How the hell
//    val cpx = ((end.x+start.x)/2)
//    val cpy = ((end.y+start.y)/2)
//    return listOf(PathNode.CurveTo(start.x, start.y, cpx, cpy, end.x, end.y))
//}

internal fun approximateArcToCurves(
    start: Offset,
    verticalEllipseRadius: Float,
    horizontalEllipseRadius: Float,
    isPositiveArc: Boolean,
    isMoreThanHalf: Boolean,
    theta: Float,
    end: Offset
): List<PathNode.CurveTo> {
    val curveSegments = mutableListOf<PathNode.CurveTo>()

    // Calculate the center of the ellipse (cx, cy) based on the given arc definition
    // This step depends on how your ArcTo definition interprets the start, end, and flags
    // The following is a placeholder calculation and may need adjustment
    val (cx, cy) = calculateEllipseCenter(
        start,
        end,
        horizontalEllipseRadius,
        verticalEllipseRadius,
        theta,
        isMoreThanHalf,
        isPositiveArc,

        )

    // Calculate the start and sweep angles for the arc
    val startAngle = atan2((start.y - cy).toDouble(), (start.x - cx).toDouble())
    var endAngle = atan2((end.y - cy).toDouble(), (end.x - cx).toDouble())
    var sweepAngle = endAngle - startAngle

    // Adjust sweep angle based on arc flags
    if (isMoreThanHalf) {
        sweepAngle = if (sweepAngle <= 0) 2 * Math.PI + sweepAngle else sweepAngle
    } else {
        sweepAngle = if (sweepAngle >= 0) sweepAngle - 2 * Math.PI else sweepAngle
    }
    if (!isPositiveArc) {
        sweepAngle = -sweepAngle
    }

    // Now, approximate the arc using cubic Bezier curves
    arcToBezierSegments(
        cx, cy, horizontalEllipseRadius.toDouble(), verticalEllipseRadius.toDouble(),
        start.x.toDouble(), start.y.toDouble(), theta.toDouble(), startAngle, sweepAngle
    )

    return curveSegments
}

private fun calculateEllipseCenter(
    start: Offset,
    end: Offset,
    horizontalEllipseRadius: Float,
    verticalEllipseRadius: Float,
    theta: Float,
    isMoreThanHalf: Boolean,
    isPositiveArc: Boolean
): Pair<Double, Double> {
    val thetaRadians = Math.toRadians(theta.toDouble())
    val cosTheta = cos(thetaRadians)
    val sinTheta = sin(thetaRadians)

    // Transform start and end points
    val x0p = (start.x * cosTheta + start.y * sinTheta) / horizontalEllipseRadius
    val y0p = (-start.x * sinTheta + start.y * cosTheta) / verticalEllipseRadius
    val x1p = (end.x * cosTheta + end.y * sinTheta) / horizontalEllipseRadius
    val y1p = (-end.x * sinTheta + end.y * cosTheta) / verticalEllipseRadius

    // Compute differences and averages
    val dx = x0p - x1p
    val dy = y0p - y1p
    val xm = (x0p + x1p) / 2
    val ym = (y0p + y1p) / 2

    // Solve for intersecting unit circles
    val dsq = dx * dx + dy * dy
    if (dsq == 0.0) {
        throw IllegalArgumentException("Start and end points are coincident")
    }
    val disc = 1.0 / dsq - 1.0 / 4.0
    if (disc < 0.0) {
        throw IllegalArgumentException("Start and end points are too far apart")
    }
    val s = sqrt(disc)
    val sdx = s * dx
    val sdy = s * dy

    // Determine center point
    val cx: Double
    val cy: Double
    if (isMoreThanHalf == isPositiveArc) {
        cx = xm - sdy
        cy = ym + sdx
    } else {
        cx = xm + sdy
        cy = ym - sdx
    }

    return Pair(cx * horizontalEllipseRadius, cy * verticalEllipseRadius)
}

private fun arcToBezierSegments(
    centerX: Double, centerY: Double, horizontalEllipseRadius: Double, verticalEllipseRadius: Double,
    startX: Double, startY: Double, theta: Double, startAngle: Double, sweepAngle: Double
): List<PathNode.CurveTo> {
    val curveSegments = mutableListOf<PathNode.CurveTo>()

    // Maximum of 45 degrees per cubic Bezier segment
    val numSegments = ceil(abs(sweepAngle * 4 / Math.PI)).toInt()

    var eta1 = startAngle
    val cosTheta = cos(theta)
    val sinTheta = sin(theta)
    var sx = startX
    var sy = startY

    for (i in 0 until numSegments) {
        val eta2 = eta1 + sweepAngle / numSegments
        val sinEta2 = sin(eta2)
        val cosEta2 = cos(eta2)
        val e2x = centerX + horizontalEllipseRadius * cosTheta * cosEta2 - verticalEllipseRadius * sinTheta * sinEta2
        val e2y = centerY + horizontalEllipseRadius * sinTheta * cosEta2 + verticalEllipseRadius * cosTheta * sinEta2

        val sinEta1 = sin(eta1)
        val cosEta1 = cos(eta1)
        val ep1x = -horizontalEllipseRadius * cosTheta * sinEta1 - verticalEllipseRadius * sinTheta * cosEta1
        val ep1y = -horizontalEllipseRadius * sinTheta * sinEta1 + verticalEllipseRadius * cosTheta * cosEta1
        val ep2x = -horizontalEllipseRadius * cosTheta * sinEta2 - verticalEllipseRadius * sinTheta * cosEta2
        val ep2y = -horizontalEllipseRadius * sinTheta * sinEta2 + verticalEllipseRadius * cosTheta * cosEta2

        val tanDiff2 = tan((eta2 - eta1) / 2)
        val alpha = sin(eta2 - eta1) * (sqrt(4 + 3 * tanDiff2 * tanDiff2) - 1) / 3

        val q1x = sx + alpha * ep1x
        val q1y = sy + alpha * ep1y
        val q2x = e2x - alpha * ep2x
        val q2y = e2y - alpha * ep2y

        curveSegments.add(
            PathNode.CurveTo(
                q1x.toFloat(), q1y.toFloat(),
                q2x.toFloat(), q2y.toFloat(),
                e2x.toFloat(), e2y.toFloat()
            )
        )

        eta1 = eta2
        sx = e2x
        sy = e2y
    }

    return curveSegments
}