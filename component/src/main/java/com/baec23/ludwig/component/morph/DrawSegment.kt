package com.baec23.ludwig.component.morph

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.vector.PathNode

data class DrawSegment(
    val start: Offset,
    val end: Offset,
    val distance: Float,
    val pathNodes: List<PathNode>
){
    fun getMidPoint(){

    }
}

fun List<PathNode>.toDrawSegments(): List<DrawSegment> {
    val toReturn = mutableListOf<DrawSegment>()
    var currPosition = Offset(0f, 0f)

    val pathNodes = mutableListOf<PathNode>()
    val pathMeasurer = PathMeasure()

    this.forEach { node ->
        val start = currPosition
        pathNodes.clear()
        pathNodes.add(PathNode.MoveTo(currPosition.x, currPosition.y))

        when (node) {
            PathNode.Close -> {}
            is PathNode.CurveTo -> {
                pathNodes.add(node)
                currPosition = Offset(node.x3, node.y3)
            }
            is PathNode.HorizontalTo -> {
                pathNodes.add(node)
                currPosition = Offset(node.x, currPosition.y)
            }

            is PathNode.LineTo -> {
                pathNodes.add(node)
                currPosition = Offset(node.x, node.y)
            }

            is PathNode.MoveTo -> {
                pathNodes.add(node)
                currPosition = Offset(node.x, node.y)
            }

            is PathNode.QuadTo -> {
                pathNodes.add(node)
                currPosition = Offset(node.x2, node.y2)
            }

            is PathNode.ReflectiveCurveTo -> {
                pathNodes.add(node)
                currPosition = Offset(node.x2, node.y2)
            }

            is PathNode.ReflectiveQuadTo -> {
                pathNodes.add(node)
                currPosition = Offset(node.x, node.y)
            }

            is PathNode.RelativeCurveTo -> {
                val x1 = currPosition.x + node.dx1
                val y1 = currPosition.y + node.dy1
                val x2 = currPosition.x + node.dx2
                val y2 = currPosition.y + node.dy2
                val x3 = currPosition.x + node.dx3
                val y3 = currPosition.y + node.dy3
                currPosition = Offset(x3, y3)
                pathNodes.add(PathNode.CurveTo(x1, y1, x2, y2, x3, y3))
            }

            is PathNode.RelativeHorizontalTo -> {
                val x = currPosition.x + node.dx
                pathNodes.add(PathNode.LineTo(x, currPosition.y))
                currPosition = Offset(x, currPosition.y)
            }

            is PathNode.RelativeLineTo -> {
                val x = currPosition.x + node.dx
                val y = currPosition.y + node.dy
                currPosition = Offset(x, y)
                pathNodes.add(PathNode.LineTo(x, y))
            }

            is PathNode.RelativeMoveTo -> {
                val x = currPosition.x + node.dx
                val y = currPosition.y + node.dy
                currPosition = Offset(x, y)
                pathNodes.add(PathNode.MoveTo(x, y))
            }

            is PathNode.RelativeQuadTo -> {
                val x1 = currPosition.x + node.dx1
                val y1 = currPosition.y + node.dy1
                val x2 = currPosition.x + node.dx2
                val y2 = currPosition.y + node.dy2
                currPosition = Offset(x2, y2)
                pathNodes.add(PathNode.QuadTo(x1, y1, x2, y2))
            }

            is PathNode.RelativeReflectiveCurveTo -> {
                // Check the last command in toReturn
                val prevNode = pathNodes.lastOrNull()
                var prevX1 = 0f
                var prevY1 = 0f

                when (prevNode) {
                    is PathNode.CurveTo -> {
                        prevX1 = prevNode.x1
                        prevY1 = prevNode.y1
                    }

                    is PathNode.RelativeCurveTo -> {
                        prevX1 = currPosition.x - prevNode.dx1
                        prevY1 = currPosition.y - prevNode.dy1
                    }

                    else -> {
                        prevX1 = currPosition.x
                        prevY1 = currPosition.y
                    }
                }

                // Reflect previous control point around current position
                val reflX1 = currPosition.x * 2 - prevX1
                val reflY1 = currPosition.y * 2 - prevY1

                // Calculate second control point and endpoint of the curve
                val x2 = currPosition.x + node.dx1
                val y2 = currPosition.y + node.dy1
                val x3 = currPosition.x + node.dx2
                val y3 = currPosition.y + node.dy2

                // Add CurveTo command
                pathNodes.add(PathNode.CurveTo(reflX1, reflY1, x2, y2, x3, y3))

                // Update current position
                currPosition = Offset(x3, y3)
            }

            is PathNode.RelativeReflectiveQuadTo -> {
                // Check the last command in toReturn
                val lastNode = pathNodes.lastOrNull()
                var prevControlX = 0f
                var prevControlY = 0f

                when (lastNode) {
                    is PathNode.QuadTo -> {
                        prevControlX = lastNode.x1
                        prevControlY = lastNode.y1
                    }

                    is PathNode.RelativeQuadTo -> {
                        prevControlX = currPosition.x - lastNode.dx1
                        prevControlY = currPosition.y - lastNode.dy1
                    }

                    else -> {
                        // Default behavior if the last command isn't a quad curve
                        prevControlX = currPosition.x
                        prevControlY = currPosition.y
                    }
                }

                // Reflect previous control point around current position
                val reflX = currPosition.x * 2 - prevControlX
                val reflY = currPosition.y * 2 - prevControlY

                // Calculate the endpoint of the curve
                val endX = currPosition.x + node.dx
                val endY = currPosition.y + node.dy

                // Add QuadTo command
                pathNodes.add(PathNode.QuadTo(reflX, reflY, endX, endY))

                // Update current position
                currPosition = Offset(endX, endY)
            }

            is PathNode.RelativeVerticalTo -> {
                val y = currPosition.y + node.dy
                pathNodes.add(PathNode.LineTo(currPosition.x, y))
                currPosition = Offset(currPosition.x, y)
            }

            is PathNode.VerticalTo -> {
                pathNodes.add(PathNode.LineTo(currPosition.x, node.y))
                currPosition = Offset(currPosition.x, node.y)
            }

            is PathNode.ArcTo -> {
                pathNodes.add(node)
                currPosition = Offset(node.arcStartX, node.arcStartY)
            }

            is PathNode.RelativeArcTo -> {
                val arcStartX = currPosition.x + node.arcStartDx
                val arcStartY = currPosition.y + node.arcStartDy
                pathNodes.add(
                    PathNode.ArcTo(
                        horizontalEllipseRadius = node.horizontalEllipseRadius,
                        verticalEllipseRadius = node.verticalEllipseRadius,
                        theta = node.theta,
                        isMoreThanHalf = node.isMoreThanHalf,
                        isPositiveArc = node.isPositiveArc,
                        arcStartX = arcStartX,
                        arcStartY = arcStartY
                    )
                )
                currPosition = Offset(arcStartX, arcStartY)
            }
        }
        pathNodes.add(PathNode.Close)
        val path = pathNodes.toPath()
        pathMeasurer.setPath(path, false)
        val distance = pathMeasurer.length
        val end = currPosition
        if(distance > 0){
            toReturn.add(DrawSegment(start = start, end=end, distance = distance, pathNodes = pathNodes.toList()))
        }
    }
    return toReturn
}