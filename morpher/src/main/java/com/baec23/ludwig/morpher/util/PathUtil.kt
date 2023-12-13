package com.baec23.ludwig.morpher.util


import android.graphics.PointF
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.graphics.vector.PathParser
import com.baec23.ludwig.morpher.model.PathSegment
import com.baec23.ludwig.morpher.model.interpolate
import kotlin.math.cos

/**
 * Splits a path (defined by List<PathNode>) into visual subpaths.
 * Each subpath will start with a MoveTo.
 * All RelativeMoveTo are converted to MoveTo.
 * Sequential MoveTo/RelativeMoveTo are collapsed into a single MoveTo.
 * Close is replaced with LineTo for interop with unclosed paths.
 */
fun List<PathNode>.splitPaths(): List<List<PathNode>> {
    val toReturn = mutableListOf<List<PathNode>>()
    val currSubpath = mutableListOf<PathNode>()
    val currPosition = PointF(0f, 0f)
    this.forEach { node ->
        //Subpath must start with a MoveTo or RelativeMoveTo
        if (currSubpath.isEmpty() && !(node is PathNode.MoveTo || node is PathNode.RelativeMoveTo)) {
            currSubpath.add(PathNode.MoveTo(x = currPosition.x, y = currPosition.y))
        }
        when (node) {
            is PathNode.MoveTo -> {
                if (currSubpath.isNotEmpty() && currSubpath.last() is PathNode.MoveTo) {
                    currSubpath.removeLast()
                }
                if (currSubpath.isNotEmpty()) {
                    val firstMove = currSubpath.first() as PathNode.MoveTo
                    if (currPosition.x == firstMove.x && currPosition.y == firstMove.y) {
                        currSubpath.add(PathNode.Close)
                    }
                    toReturn.add(currSubpath.toList())
                    currSubpath.clear()
                }
                currPosition.set(node.x, node.y)
                currSubpath.add(node)
            }

            is PathNode.RelativeMoveTo -> {
                if (currSubpath.isNotEmpty() && currSubpath.last() is PathNode.MoveTo) {
                    currSubpath.removeLast()
                }
                if (currSubpath.isNotEmpty()) {
                    toReturn.add(currSubpath.toList())
                    currSubpath.clear()
                }
                currPosition.offset(node.dx, node.dy)
                currSubpath.add(PathNode.MoveTo(currPosition.x, currPosition.y))
            }

            is PathNode.LineTo -> {
                currPosition.set(node.x, node.y)
                currSubpath.add(node)
            }

            is PathNode.RelativeLineTo -> {
                currPosition.offset(node.dx, node.dy)
                currSubpath.add(node)
            }

            is PathNode.HorizontalTo -> {
                currPosition.set(node.x, currPosition.y)
                currSubpath.add(node)
            }

            is PathNode.RelativeHorizontalTo -> {
                currPosition.offset(node.dx, 0f)
                currSubpath.add(node)
            }

            is PathNode.VerticalTo -> {
                currPosition.set(currPosition.x, node.y)
                currSubpath.add(node)
            }

            is PathNode.RelativeVerticalTo -> {
                currPosition.offset(0f, node.dy)
                currSubpath.add(node)
            }

            is PathNode.CurveTo -> {
                currPosition.set(node.x3, node.y3)
                currSubpath.add(node)
            }

            is PathNode.RelativeCurveTo -> {
                currPosition.offset(node.dx3, node.dy3)
                currSubpath.add(node)
            }

            is PathNode.QuadTo -> {
                currPosition.set(node.x2, node.y2)
                currSubpath.add(node)
            }

            is PathNode.RelativeQuadTo -> {
                currPosition.offset(node.dx2, node.dy2)
                currSubpath.add(node)
            }

            is PathNode.ReflectiveCurveTo -> {
                currPosition.set(node.x2, node.y2)
                currSubpath.add(node)
            }

            is PathNode.RelativeReflectiveCurveTo -> {
                currPosition.offset(node.dx2, node.dy2)
                currSubpath.add(node)
            }

            is PathNode.ReflectiveQuadTo -> {
                currPosition.set(node.x, node.y)
                currSubpath.add(node)
            }

            is PathNode.RelativeReflectiveQuadTo -> {
                currPosition.offset(node.dx, node.dy)
                currSubpath.add(node)
            }

            is PathNode.ArcTo -> {
                currPosition.set(
                    currPosition.x + node.arcStartX,
                    currPosition.y + node.arcStartY
                )
                currSubpath.add(node)
            }

            is PathNode.RelativeArcTo -> {
                currPosition.offset(node.arcStartDx, node.arcStartDy)
                currSubpath.add(node)
            }

            PathNode.Close -> {
                currSubpath.add(node)
            }
        }
    }
    if (currSubpath.isNotEmpty()) {
        toReturn.add(currSubpath.toList())
    }

    return toReturn.filter { subpath -> subpath.find { it !is PathNode.MoveTo && it !is PathNode.RelativeMoveTo && it != PathNode.Close } != null }
        .toList()
}


fun PathNode.lerp(target: PathNode, fraction: Float): PathNode {
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
                x1 = newX1,
                y1 = newY1,
                x2 = newX2,
                y2 = newY2,
                x3 = newX3,
                y3 = newY3
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


fun PathNode.lerp(target: Offset, fraction: Float): PathNode {
    if (this == PathNode.Close) {
        return this
    }
    return when (this) {
        is PathNode.ArcTo -> {
            val newArcStartX = lerp(this.arcStartX, target.x, fraction)
            val newArcStartY = lerp(this.arcStartY, target.y, fraction)
            val newHorizontalEllipseRadius =
                lerp(this.horizontalEllipseRadius, target.x, fraction)
            val newVerticalEllipseRadius =
                lerp(this.verticalEllipseRadius, target.y, fraction)
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
                x1 = newX1,
                y1 = newY1,
                x2 = newX2,
                y2 = newY2,
                x3 = newX3,
                y3 = newY3
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

fun PathNode.calcLength(startOffset: Offset): Float {
    val path =
        PathParser().addPathNodes(listOf(PathNode.MoveTo(startOffset.x, startOffset.y), this))
            .toPath()
    val pathMeasurer = PathMeasure()
    pathMeasurer.setPath(path, false)
    return pathMeasurer.length
}

fun List<PathNode>.normalize(
    offset: Offset,
    scaleFactorX: Float,
    scaleFactorY: Float
): List<PathNode> {
    val mutable = this.toMutableList()

    //Because if starts with RelativeMoveTo, causes issues - this is collapsed into a single MoveTo later in splitPaths
    mutable.add(
        0,
        PathNode.MoveTo(0f, 0f)
    )

    return mutable.mapIndexed { index, node ->
        when (node) {
            is PathNode.ArcTo -> node.copy(
                arcStartX = (node.arcStartX - offset.x) * scaleFactorX,
                arcStartY = (node.arcStartY - offset.y) * scaleFactorY,
                horizontalEllipseRadius = node.horizontalEllipseRadius * scaleFactorX,
                verticalEllipseRadius = node.verticalEllipseRadius * scaleFactorY
            )

            is PathNode.CurveTo -> node.copy(
                x1 = (node.x1 - offset.x) * scaleFactorX,
                y1 = (node.y1 - offset.y) * scaleFactorY,
                x2 = (node.x2 - offset.x) * scaleFactorX,
                y2 = (node.y2 - offset.y) * scaleFactorY,
                x3 = (node.x3 - offset.x) * scaleFactorX,
                y3 = (node.y3 - offset.y) * scaleFactorY
            )

            is PathNode.HorizontalTo -> node.copy(
                x = (node.x - offset.x) * scaleFactorX
            )

            is PathNode.LineTo -> node.copy(
                x = (node.x - offset.x) * scaleFactorX,
                y = (node.y - offset.y) * scaleFactorY
            )

            is PathNode.MoveTo -> {
                node.copy(
                    x = (node.x - offset.x) * scaleFactorX,
                    y = (node.y - offset.y) * scaleFactorY
                )
            }

            is PathNode.QuadTo -> node.copy(
                x1 = (node.x1 - offset.x) * scaleFactorX,
                y1 = (node.y1 - offset.y) * scaleFactorY,
                x2 = (node.x2 - offset.x) * scaleFactorX,
                y2 = (node.y2 - offset.y) * scaleFactorY
            )

            is PathNode.ReflectiveCurveTo -> node.copy(
                x1 = (node.x1 - offset.x) * scaleFactorX,
                y1 = (node.y1 - offset.y) * scaleFactorY,
                x2 = (node.x2 - offset.x) * scaleFactorX,
                y2 = (node.y2 - offset.y) * scaleFactorY
            )

            is PathNode.ReflectiveQuadTo -> node.copy(
                x = (node.x - offset.x) * scaleFactorX,
                y = (node.y - offset.y) * scaleFactorY
            )

            is PathNode.RelativeHorizontalTo -> {
                node.copy(dx = node.dx * scaleFactorX)
            }

            is PathNode.VerticalTo -> node.copy(
                y = (node.y - offset.y) * scaleFactorY
            )

            is PathNode.RelativeArcTo -> node.copy(
                horizontalEllipseRadius = node.horizontalEllipseRadius * scaleFactorX,
                verticalEllipseRadius = node.verticalEllipseRadius * scaleFactorY,
                arcStartDx = node.arcStartDx * scaleFactorX,
                arcStartDy = node.arcStartDy * scaleFactorY
            )

            is PathNode.RelativeCurveTo -> node.copy(
                dx1 = node.dx1 * scaleFactorX,
                dy1 = node.dy1 * scaleFactorY,
                dx2 = node.dx2 * scaleFactorX,
                dy2 = node.dy2 * scaleFactorY,
                dx3 = node.dx3 * scaleFactorX,
                dy3 = node.dy3 * scaleFactorY
            )

            is PathNode.RelativeQuadTo -> node.copy(
                dx1 = node.dx1 * scaleFactorX,
                dy1 = node.dy1 * scaleFactorY,
                dx2 = node.dx2 * scaleFactorX,
                dy2 = node.dy2 * scaleFactorY
            )

            is PathNode.RelativeReflectiveCurveTo -> node.copy(
                dx1 = node.dx1 * scaleFactorX,
                dy1 = node.dy1 * scaleFactorY,
                dx2 = node.dx2 * scaleFactorX,
                dy2 = node.dy2 * scaleFactorY
            )

            is PathNode.RelativeReflectiveQuadTo -> node.copy(
                dx = node.dx * scaleFactorX,
                dy = node.dy * scaleFactorY
            )

            is PathNode.RelativeLineTo -> node.copy(
                dx = node.dx * scaleFactorX,
                dy = node.dy * scaleFactorY
            )

            is PathNode.RelativeMoveTo -> {
                node.copy(
                    dx = node.dx * scaleFactorX,
                    dy = node.dy * scaleFactorY
                )
            }

            is PathNode.RelativeVerticalTo -> node.copy(
                dy = node.dy * scaleFactorY
            )

            PathNode.Close -> node
        }
    }
}

fun List<PathNode>.toPathSegments(): List<PathSegment> {
    val toReturn = mutableListOf<PathSegment>()
    var currPosition = Offset(0f, 0f)

    this.forEach { node ->
        val startOffset = currPosition
        var nodeToAdd: PathNode = PathNode.Close
        when (node) {
            //Lines
            is PathNode.LineTo -> {
                val cp1 = interpolate(currPosition, Offset(node.x, node.y), 0.33f)
                val cp2 = interpolate(currPosition, Offset(node.x, node.y), 0.66f)
                val x1 = cp1.x
                val y1 = cp1.y
                val x2 = cp2.x
                val y2 = cp2.y
                val x3 = node.x
                val y3 = node.y
                nodeToAdd = PathNode.CurveTo(x1, y1, x2, y2, x3, y3)
                currPosition = Offset(node.x, node.y)
            }

            is PathNode.RelativeLineTo -> {
                val x = currPosition.x + node.dx
                val y = currPosition.y + node.dy
                val cp1 = interpolate(currPosition, Offset(x, y), 0.33f)
                val cp2 = interpolate(currPosition, Offset(x, y), 0.66f)
                val x1 = cp1.x
                val y1 = cp1.y
                val x2 = cp2.x
                val y2 = cp2.y
                val x3 = x
                val y3 = y
                nodeToAdd = PathNode.CurveTo(x1, y1, x2, y2, x3, y3)
                currPosition = Offset(x, y)
            }

            is PathNode.HorizontalTo -> {
                val x = node.x
                val y = currPosition.y
                val cp1 = interpolate(currPosition, Offset(x, y), 0.33f)
                val cp2 = interpolate(currPosition, Offset(x, y), 0.66f)
                val x1 = cp1.x
                val y1 = cp1.y
                val x2 = cp2.x
                val y2 = cp2.y
                val x3 = x
                val y3 = y
                nodeToAdd = PathNode.CurveTo(x1, y1, x2, y2, x3, y3)
                currPosition = Offset(x, y)
            }

            is PathNode.RelativeHorizontalTo -> {
                val x = currPosition.x + node.dx
                val y = currPosition.y
                val cp1 = interpolate(currPosition, Offset(x, y), 0.33f)
                val cp2 = interpolate(currPosition, Offset(x, y), 0.66f)
                val x1 = cp1.x
                val y1 = cp1.y
                val x2 = cp2.x
                val y2 = cp2.y
                val x3 = x
                val y3 = y
                nodeToAdd = PathNode.CurveTo(x1, y1, x2, y2, x3, y3)
                currPosition = Offset(x, y)
            }

            is PathNode.VerticalTo -> {
                val x = currPosition.x
                val y = node.y
                val cp1 = interpolate(currPosition, Offset(x, y), 0.33f)
                val cp2 = interpolate(currPosition, Offset(x, y), 0.66f)
                val x1 = cp1.x
                val y1 = cp1.y
                val x2 = cp2.x
                val y2 = cp2.y
                val x3 = x
                val y3 = y
                nodeToAdd = PathNode.CurveTo(x1, y1, x2, y2, x3, y3)
                currPosition = Offset(x, y)
            }

            is PathNode.RelativeVerticalTo -> {
                val x = currPosition.x
                val y = currPosition.y + node.dy
                val cp1 = interpolate(currPosition, Offset(x, y), 0.33f)
                val cp2 = interpolate(currPosition, Offset(x, y), 0.66f)
                val x1 = cp1.x
                val y1 = cp1.y
                val x2 = cp2.x
                val y2 = cp2.y
                val x3 = x
                val y3 = y
                nodeToAdd = PathNode.CurveTo(x1, y1, x2, y2, x3, y3)
                currPosition = Offset(x, y)
            }

            //Curves
            is PathNode.CurveTo -> {
                currPosition = Offset(node.x3, node.y3)
                nodeToAdd = node
            }

            is PathNode.RelativeCurveTo -> {
                val x1 = currPosition.x + node.dx1
                val y1 = currPosition.y + node.dy1
                val x2 = currPosition.x + node.dx2
                val y2 = currPosition.y + node.dy2
                val x3 = currPosition.x + node.dx3
                val y3 = currPosition.y + node.dy3
                currPosition = Offset(x3, y3)
                nodeToAdd = PathNode.CurveTo(x1, y1, x2, y2, x3, y3)
            }

            is PathNode.ReflectiveCurveTo -> {
                when (val prevNode = toReturn.lastOrNull()?.pathNode) {
                    is PathNode.CurveTo -> {
                        val x1 = currPosition.x + (currPosition.x - prevNode.x2)
                        val y1 = currPosition.y + (currPosition.y - prevNode.y2)
                        val x2 = node.x1
                        val y2 = node.y1
                        val x3 = node.x2
                        val y3 = node.y2

                        nodeToAdd = PathNode.CurveTo(x1, y1, x2, y2, x3, y3)
                        currPosition = Offset(x3, y3)
                    }

                    else -> {
                        nodeToAdd =
                            PathNode.CurveTo(
                                currPosition.x,
                                currPosition.y,
                                node.x1,
                                node.y1,
                                node.x2,
                                node.y2
                            )

                        currPosition = Offset(node.x2, node.y2)
                    }
                }
            }

            is PathNode.RelativeReflectiveCurveTo -> {
                when (val prevNode = toReturn.lastOrNull()?.pathNode) {
                    is PathNode.CurveTo -> {
                        val x1 = currPosition.x + (currPosition.x - prevNode.x2)
                        val y1 = currPosition.y + (currPosition.y - prevNode.y2)
                        val x2 = currPosition.x + node.dx1
                        val y2 = currPosition.y + node.dy1
                        val x3 = currPosition.x + node.dx2
                        val y3 = currPosition.y + node.dy2

                        nodeToAdd = PathNode.CurveTo(x1, y1, x2, y2, x3, y3)
                        currPosition = Offset(x3, y3)
                    }

                    else -> {
                        nodeToAdd =
                            PathNode.CurveTo(
                                currPosition.x,
                                currPosition.y,
                                currPosition.x + node.dx1,
                                currPosition.y + node.dy1,
                                currPosition.x + node.dx2,
                                currPosition.y + node.dy2
                            )

                        currPosition = Offset(currPosition.x + node.dx2, currPosition.y + node.dy2)
                    }
                }
            }

            //Quads
            is PathNode.QuadTo -> {
                val x = currPosition.x
                val y = currPosition.y
                val x1 = node.x1
                val y1 = node.y1
                val x2 = node.x2
                val y2 = node.y2

                val cp1 = Offset(x + 2.0f / 3.0f * (x1 - x), y + 2.0f / 3.0f * (y1 - y))
                val cp2 = Offset(x2 + 2.0f / 3.0f * (x1 - x2), y2 + 2.0f / 3.0f * (y1 - y2))

                nodeToAdd = PathNode.CurveTo(cp1.x, cp1.y, cp2.x, cp2.y, x2, y2)
                currPosition = Offset(x2, y2)
            }

            is PathNode.RelativeQuadTo -> {
                val x = currPosition.x
                val y = currPosition.y
                val x1 = currPosition.x + node.dx1
                val y1 = currPosition.y + node.dy1
                val x2 = currPosition.x + node.dx2
                val y2 = currPosition.y + node.dy2

                val cp1 = Offset(x + 2.0f / 3.0f * (x1 - x), y + 2.0f / 3.0f * (y1 - y))
                val cp2 = Offset(x2 + 2.0f / 3.0f * (x1 - x2), y2 + 2.0f / 3.0f * (y1 - y2))

                nodeToAdd = PathNode.CurveTo(cp1.x, cp1.y, cp2.x, cp2.y, x2, y2)
                currPosition = Offset(x2, y2)
            }

            is PathNode.ReflectiveQuadTo -> {
                val reflectiveControlPoint = when (val prevNode = toReturn.lastOrNull()?.pathNode) {
                    is PathNode.CurveTo -> {
                        // Reflect based on a specific control point or average
                        // Adjust this based on how the QuadTo was converted to CurveTo
                        val reflectiveX = (prevNode.x1 + prevNode.x2) / 2
                        val reflectiveY = (prevNode.y1 + prevNode.y2) / 2
                        Offset(
                            x = 2 * currPosition.x - reflectiveX,
                            y = 2 * currPosition.y - reflectiveY
                        )
                    }

                    else -> currPosition
                }

                val endOffset = Offset(node.x, node.y)
                // Convert the quadratic control point to cubic control points
                val cp1 = currPosition + (reflectiveControlPoint - currPosition) * (2.0f / 3.0f)
                val cp2 = endOffset + (reflectiveControlPoint - endOffset) * (2.0f / 3.0f)
                nodeToAdd = PathNode.CurveTo(cp1.x, cp1.y, cp2.x, cp2.y, endOffset.x, endOffset.y)
                currPosition = endOffset
            }

            is PathNode.RelativeReflectiveQuadTo -> {
                val endOffset = currPosition + Offset(node.dx, node.dy)
                val reflectiveControlPoint = when (val prevNode = toReturn.lastOrNull()?.pathNode) {
                    is PathNode.CurveTo -> {
                        val reflectiveX = (prevNode.x1 + prevNode.x2) / 2
                        val reflectiveY = (prevNode.y1 + prevNode.y2) / 2
                        Offset(
                            x = currPosition.x + (currPosition.x - reflectiveX),
                            y = currPosition.y + (currPosition.y - reflectiveY)
                        )
                    }

                    else -> currPosition
                }
                // Convert the quadratic control point to cubic control points
                val cp1 = currPosition + (reflectiveControlPoint - currPosition) * (2.0f / 3.0f)
                val cp2 = endOffset + (reflectiveControlPoint - endOffset) * (2.0f / 3.0f)
                nodeToAdd = PathNode.CurveTo(cp1.x, cp1.y, cp2.x, cp2.y, endOffset.x, endOffset.y)
                currPosition = endOffset
            }
            //Arcs
            is PathNode.ArcTo -> {
                val arcCurves = approximateArcToCurves(
                    start = currPosition,
                    radius = node.horizontalEllipseRadius, // For circular arc
                    sweepAngle = node.theta,
                    end = Offset(node.arcStartX, node.arcStartY)
                )
                currPosition = Offset(node.arcStartX, node.arcStartY)
                arcCurves.forEach { curve ->
                    toReturn.add(
                        PathSegment(
                            startPosition = startOffset,
                            endPosition = currPosition,
                            pathNode = curve
                        )
                    )
                }
                nodeToAdd = PathNode.Close
            }

            is PathNode.RelativeArcTo -> {
                // Convert relative coordinates to absolute
                val absoluteEndX = currPosition.x + node.arcStartDx
                val absoluteEndY = currPosition.y + node.arcStartDy

                // Approximate the arc with cubic bezier curves
                val arcCurves = approximateArcToCurves(
                    start = currPosition,
                    radius = node.horizontalEllipseRadius, // Assuming circular arc for simplicity
                    sweepAngle = node.theta,
                    end = Offset(absoluteEndX, absoluteEndY)
                )

                // Update the current position
                currPosition = Offset(absoluteEndX, absoluteEndY)

                // Add each curve as a segment
                arcCurves.forEach { curve ->
                    toReturn.add(
                        PathSegment(
                            startPosition = startOffset,
                            endPosition = currPosition,
                            pathNode = curve
                        )
                    )
                }
                nodeToAdd = PathNode.Close
            }

            //Other
            is PathNode.MoveTo -> {
                currPosition = Offset(node.x, node.y)
                nodeToAdd = node
            }

            is PathNode.RelativeMoveTo -> {
                val x = currPosition.x + node.dx
                val y = currPosition.y + node.dy
                currPosition = Offset(x, y)
                nodeToAdd = PathNode.MoveTo(x, y)
            }

            PathNode.Close -> {
//                nodeToAdd = node
            }
        }
        val endOffset = currPosition
        if (nodeToAdd !is PathNode.Close) {
            toReturn.add(
                PathSegment(
                    startPosition = startOffset,
                    endPosition = endOffset,
                    pathNode = nodeToAdd
                )
            )
        }
    }
    return toReturn.toList()
}

private fun approximateArcToCurves(
    start: Offset,
    radius: Float,
    sweepAngle: Float,
    end: Offset
): List<PathNode.CurveTo> {
    // This function will approximate the arc by a single cubic Bezier curve.
    // For larger arcs, it's recommended to divide the arc into smaller segments.

    // Midpoint calculation
    val midPointX = (start.x + end.x) / 2f
    val midPointY = (start.y + end.y) / 2f

    // Angle in radians
    val angleRad = Math.toRadians(sweepAngle.toDouble() / 2)

    // Distance from midpoint to arc midpoint
    val distToMidArc = radius * (1 - cos(angleRad))

    // Control points distance
    val controlDist = (4 / 3) * distToMidArc

    val control1 = Offset(
        (start.x + controlDist * (midPointX - start.x) / distToMidArc).toFloat(),
        (start.y + controlDist * (midPointY - start.y) / distToMidArc).toFloat()
    )

    val control2 = Offset(
        (end.x + controlDist * (midPointX - end.x) / distToMidArc).toFloat(),
        (end.y + controlDist * (midPointY - end.y) / distToMidArc).toFloat()
    )

    return listOf(PathNode.CurveTo(control1.x, control1.y, control2.x, control2.y, end.x, end.y))
}
fun List<PathNode>.calcLength(): Float {
    val path = PathParser().addPathNodes(this).toPath()
    val pathMeasurer = PathMeasure()
    pathMeasurer.setPath(path, false)
    return pathMeasurer.length
}


fun List<PathNode>.trim(): List<PathNode> {
    val firstIndex = this.indexOfFirst { it !is PathNode.MoveTo && it !is PathNode.RelativeMoveTo }
    var lastIndex = this.indexOfLast { it == PathNode.Close }
    if (lastIndex == -1) {
        lastIndex = this.size
    }
    return this.subList(firstIndex, lastIndex)
}

fun List<PathNode>.toPath(): Path {
    return PathParser().addPathNodes(this).toPath()
}


fun List<PathNode>.toCustomString(): String {
    val sb = StringBuilder()
    this.forEach { pathNode ->
        sb.append(pathNode.toCustomString())
    }
    return sb.toString()
}

fun PathNode.toCustomString(): String {
    return when (this) {
        is PathNode.ArcTo -> """
            ===ArcTo===
            arcStartX = $arcStartX
            arcStartY = $arcStartY
            """

        PathNode.Close -> """
            ===Close===
            """

        is PathNode.CurveTo -> """
            ===CurveTo===
            x1 = $x1
            y1 = $y1
            x2 = $x2
            y2 = $y2
            x3 = $x3
            y3 = $y3
            """

        is PathNode.HorizontalTo -> """
            ===HorizontalTo===
            x = $x
            """

        is PathNode.LineTo -> """
            ===LineTo===
            x = $x
            y = $y
            """

        is PathNode.MoveTo -> """
            ===MoveTo===
            x = $x
            y = $y
            """

        is PathNode.QuadTo -> """
            ===QuadTo===
            controlX = $x1
            controlY = $y1
            endX = $x2
            endY = $y2
            """

        is PathNode.ReflectiveCurveTo -> """
            ===ReflectiveCurveTo===
            controlX = $x1
            controlY = $y1
            endX = $x2
            endY = $y2
            """

        is PathNode.ReflectiveQuadTo -> """
            ===ReflectiveQuadTo===
            x = $x
            y = $y
            """

        is PathNode.RelativeArcTo -> """
            ===RelativeArcTo===            
            """

        is PathNode.RelativeCurveTo -> """
            ===RelativeCurveTo===            
            """

        is PathNode.RelativeHorizontalTo -> """
            ===RelativeHorizontalTo===
            dx = $dx
            """

        is PathNode.RelativeLineTo -> """
            ===RelativeLineTo===
            dx = $dx
            dy = $dy
            """

        is PathNode.RelativeMoveTo -> """
            ===RelativeMoveTo===
            dx = $dx
            dy = $dy            
            """

        is PathNode.RelativeQuadTo -> """
            ===RelativeQuadTo===            
            """

        is PathNode.RelativeReflectiveCurveTo -> """
            ===RelativeReflectiveCurveTo===            
            """

        is PathNode.RelativeReflectiveQuadTo -> """
            ===RelativeReflectiveQuadTo===            
            """

        is PathNode.RelativeVerticalTo -> """
            ===RelativeVerticalTo===
            dy = $dy
            """

        is PathNode.VerticalTo -> """
            ===VerticalTo===
            y = $y
            """
    }
}

fun <T> List<T>.rotateToIndex(index: Int): List<T> {
    // Check if the index is within the bounds of the list
    if (index < 0 || index >= size) {
        throw IllegalArgumentException("Index out of bounds")
    }

    // If the index is 0, no rotation is needed
    if (index == 0) return this

    // Rotate the list
    return this.subList(index, this.size) + this.subList(0, index)
}

fun lerp(start: Float, end: Float, fraction: Float): Float {
    return start + (end - start) * fraction
}
