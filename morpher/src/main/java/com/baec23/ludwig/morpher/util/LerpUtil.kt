package com.baec23.ludwig.morpher.util

import android.graphics.PointF
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.PathNode


internal fun PathNode.lerp(target: PathNode, fraction: Float): PathNode {
    if (this::class != target::class) {
        throw Exception()
    }
    return when (this) {
        is PathNode.ArcTo -> {
            target as PathNode.ArcTo
            val newArcStartX = lerp(this.arcStartX, target.arcStartX, fraction)
            val newArcStartY = lerp(this.arcStartY, target.arcStartY, fraction)
            val newHorizontalEllipseRadius =
                lerp(this.horizontalEllipseRadius, target.horizontalEllipseRadius, fraction)
            val newVerticalEllipseRadius =
                lerp(this.verticalEllipseRadius, target.verticalEllipseRadius, fraction)
            val newTheta = lerp(this.theta, target.theta, fraction)
            this.copy(
                arcStartX = newArcStartX,
                arcStartY = newArcStartY,
                horizontalEllipseRadius = newHorizontalEllipseRadius,
                verticalEllipseRadius = newVerticalEllipseRadius,
                theta = newTheta
            )
        }

        is PathNode.CurveTo -> {
            target as PathNode.CurveTo
            val newX1 = lerp(this.x1, target.x1, fraction)
            val newY1 = lerp(this.y1, target.y1, fraction)
            val newX2 = lerp(this.x2, target.x2, fraction)
            val newY2 = lerp(this.y2, target.y2, fraction)
            val newX3 = lerp(this.x3, target.x3, fraction)
            val newY3 = lerp(this.y3, target.y3, fraction)
            PathNode.CurveTo(
                x1 = newX1, y1 = newY1, x2 = newX2, y2 = newY2, x3 = newX3, y3 = newY3
            )
        }

        is PathNode.HorizontalTo -> {
            target as PathNode.HorizontalTo
            val newX = lerp(this.x, target.x, fraction)
            PathNode.HorizontalTo(x = newX)
        }

        is PathNode.LineTo -> {
            target as PathNode.LineTo
            val newX = lerp(this.x, target.x, fraction)
            val newY = lerp(this.y, target.y, fraction)
            PathNode.LineTo(x = newX, y = newY)
        }

        is PathNode.MoveTo -> {
            target as PathNode.MoveTo
            val newX = lerp(this.x, target.x, fraction)
            val newY = lerp(this.y, target.y, fraction)
            PathNode.MoveTo(x = newX, y = newY)
        }

        is PathNode.QuadTo -> {
            target as PathNode.QuadTo
            val newX1 = lerp(this.x1, target.x1, fraction)
            val newY1 = lerp(this.y1, target.y1, fraction)
            val newX2 = lerp(this.x2, target.x2, fraction)
            val newY2 = lerp(this.y2, target.y2, fraction)
            PathNode.QuadTo(x1 = newX1, y1 = newY1, x2 = newX2, y2 = newY2)
        }

        is PathNode.ReflectiveCurveTo -> {
            target as PathNode.ReflectiveCurveTo
            val newX1 = lerp(this.x1, target.x1, fraction)
            val newY1 = lerp(this.y1, target.y1, fraction)
            val newX2 = lerp(this.x2, target.x2, fraction)
            val newY2 = lerp(this.y2, target.y2, fraction)
            PathNode.ReflectiveCurveTo(x1 = newX1, y1 = newY1, x2 = newX2, y2 = newY2)
        }

        is PathNode.ReflectiveQuadTo -> {
            target as PathNode.ReflectiveQuadTo
            val newX = lerp(this.x, target.x, fraction)
            val newY = lerp(this.y, target.y, fraction)
            PathNode.ReflectiveQuadTo(x = newX, y = newY)
        }

        is PathNode.VerticalTo -> {
            target as PathNode.VerticalTo
            val newY = lerp(this.y, target.y, fraction)
            PathNode.VerticalTo(y = newY)
        }

        else -> this
    }
}


internal fun PathNode.lerp(target: Offset, fraction: Float): PathNode {
    if (this == PathNode.Close) {
        return this
    }
    return when (this) {
        is PathNode.ArcTo -> {
            val newArcStartX = lerp(this.arcStartX, target.x, fraction)
            val newArcStartY = lerp(this.arcStartY, target.y, fraction)
            val newHorizontalEllipseRadius = lerp(this.horizontalEllipseRadius, target.x, fraction)
            val newVerticalEllipseRadius = lerp(this.verticalEllipseRadius, target.y, fraction)
            this.copy(
                arcStartX = newArcStartX,
                arcStartY = newArcStartY,
                horizontalEllipseRadius = newHorizontalEllipseRadius,
                verticalEllipseRadius = newVerticalEllipseRadius,
            )
        }

        is PathNode.CurveTo -> {
            val newX1 = lerp(this.x1, target.x, fraction)
            val newY1 = lerp(this.y1, target.y, fraction)
            val newX2 = lerp(this.x2, target.x, fraction)
            val newY2 = lerp(this.y2, target.y, fraction)
            val newX3 = lerp(this.x3, target.x, fraction)
            val newY3 = lerp(this.y3, target.y, fraction)
            PathNode.CurveTo(
                x1 = newX1, y1 = newY1, x2 = newX2, y2 = newY2, x3 = newX3, y3 = newY3
            )
        }

        is PathNode.HorizontalTo -> {
            val newX = lerp(this.x, target.x, fraction)
            PathNode.HorizontalTo(x = newX)
        }

        is PathNode.LineTo -> {
            val newX = lerp(this.x, target.x, fraction)
            val newY = lerp(this.y, target.y, fraction)
            PathNode.LineTo(x = newX, y = newY)
        }

        is PathNode.MoveTo -> {
            val newX = lerp(this.x, target.x, fraction)
            val newY = lerp(this.y, target.y, fraction)
            PathNode.MoveTo(x = newX, y = newY)
        }

        is PathNode.QuadTo -> {
            val newX1 = lerp(this.x1, target.x, fraction)
            val newY1 = lerp(this.y1, target.y, fraction)
            val newX2 = lerp(this.x2, target.x, fraction)
            val newY2 = lerp(this.y2, target.y, fraction)
            PathNode.QuadTo(x1 = newX1, y1 = newY1, x2 = newX2, y2 = newY2)
        }

        is PathNode.ReflectiveCurveTo -> {
            val newX1 = lerp(this.x1, target.x, fraction)
            val newY1 = lerp(this.y1, target.y, fraction)
            val newX2 = lerp(this.x2, target.x, fraction)
            val newY2 = lerp(this.y2, target.y, fraction)
            PathNode.ReflectiveCurveTo(x1 = newX1, y1 = newY1, x2 = newX2, y2 = newY2)
        }

        is PathNode.ReflectiveQuadTo -> {
            val newX = lerp(this.x, target.x, fraction)
            val newY = lerp(this.y, target.y, fraction)
            PathNode.ReflectiveQuadTo(x = newX, y = newY)
        }

        is PathNode.VerticalTo -> {
            val newY = lerp(this.y, target.y, fraction)
            PathNode.VerticalTo(y = newY)
        }

        else -> {
            this
        }
    }
}

internal fun lerp(start: Float, end: Float, fraction: Float): Float {
    return start + (end - start) * fraction
}

internal fun lerp(start: Offset, end: Offset, t: Float): Offset {
    return Offset(
        x = (1 - t) * start.x + t * end.x,
        y = (1 - t) * start.y + t * end.y
    )
}

internal fun lerp(start: PointF, end: PointF, t: Float): PointF {
    return PointF(
        (1 - t) * start.x + t * end.x,
        (1 - t) * start.y + t * end.y
    )
}