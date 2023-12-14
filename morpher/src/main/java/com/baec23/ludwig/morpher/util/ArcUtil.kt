package com.baec23.ludwig.morpher.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.PathNode

internal fun approximateArcToCurves(
    start: Offset,
    verticalEllipseRadius: Float,
    horizontalEllipseRadius: Float,
    isPositiveArc: Boolean,
    isMoreThanHalf: Boolean,
    theta: Float,
    end: Offset
): List<PathNode.CurveTo> {
    //TODO: How the hell
    val cpx = ((end.x+start.x)/2)
    val cpy = ((end.y+start.y)/2)
    return listOf(PathNode.CurveTo(start.x, start.y, cpx, cpy, end.x, end.y))
}
