package com.baec23.ludwig.component.morph

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.graphics.vector.PathParser

fun PathNode.lerp(target: PathNode, fraction: Float): PathNode {
    if (this::class != target::class) {
        Log.d(TAG, "PathNode lerp: - thisClass=${this::class} - targetClass = ${target::class}")
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
            Log.d(TAG, "lerp: this is a problem... ${this}")
            this
        }
    }
}

fun PathNode.getLength(startOffset: Offset): Float {
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
    mutable.add(0, PathNode.MoveTo(0f, 0f))

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
                nodeToAdd = node
                currPosition = Offset(node.arcStartX, node.arcStartY)
            }

            is PathNode.RelativeArcTo -> {
                val arcStartX = currPosition.x + node.arcStartDx
                val arcStartY = currPosition.y + node.arcStartDy
                nodeToAdd =
                    PathNode.ArcTo(
                        horizontalEllipseRadius = node.horizontalEllipseRadius,
                        verticalEllipseRadius = node.verticalEllipseRadius,
                        theta = node.theta,
                        isMoreThanHalf = node.isMoreThanHalf,
                        isPositiveArc = node.isPositiveArc,
                        arcStartX = arcStartX,
                        arcStartY = arcStartY
                    )

                currPosition = Offset(arcStartX, arcStartY)
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
    toReturn.add(
        PathSegment(
            startPosition = currPosition,
            endPosition = currPosition,
            pathNode = PathNode.Close
        )
    )
    val moveTos = toReturn.takeWhile { it.pathNode is PathNode.MoveTo }
    val lastMoveTo = moveTos.last()
    toReturn.removeAll(moveTos)
    toReturn.add(0, lastMoveTo)
    return toReturn.toList()
}