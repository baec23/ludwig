package com.baec23.ludwig.component.morph


import android.graphics.PointF
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.graphics.vector.PathParser
import androidx.core.graphics.PathSegment

data class SvgViewBox(
    val minX: Int, val minY: Int, val width: Int, val height: Int
)

fun equalizePathNodes(
    path1: List<PathNode>, path2: List<PathNode>
): Pair<List<PathNode>, List<PathNode>> {

    val drawCommands1 = path1.filterIsInstance<PathNode.LineTo>()
    val drawCommands2 = path2.filterIsInstance<PathNode.LineTo>()

    val newPath1 = path1.toMutableList()
    val newPath2 = path2.toMutableList()

    when {
        drawCommands1.size > drawCommands2.size -> {
            // Add extra nodes to newPath2
            // Implement logic to add intermediate nodes or split existing nodes
        }

        drawCommands1.size < drawCommands2.size -> {
            // Add extra nodes to newPath1
            // Implement logic to add intermediate nodes or split existing nodes
        }
    }

    return Pair(newPath1, newPath2)
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

fun List<PathNode>.convertToAbsoluteCommands(): List<PathNode> {
    val toReturn = mutableListOf<PathNode>()
    var currPosition = Offset(0f, 0f)

    this.forEach { node ->
        when (node) {
            PathNode.Close -> toReturn.add(node)
            is PathNode.CurveTo -> {
                currPosition = Offset(node.x3, node.y3)
                toReturn.add(node)
            }

            is PathNode.HorizontalTo -> {
                toReturn.add(PathNode.LineTo(node.x, currPosition.y))
                currPosition = Offset(node.x, currPosition.y)
            }

            is PathNode.LineTo -> {
                currPosition = Offset(node.x, node.y)
                toReturn.add(node)
            }

            is PathNode.MoveTo -> {
                currPosition = Offset(node.x, node.y)
                toReturn.add(node)
            }

            is PathNode.QuadTo -> {
                currPosition = Offset(node.x2, node.y2)
                toReturn.add(node)
            }

            is PathNode.ReflectiveCurveTo -> {
                currPosition = Offset(node.x2, node.y2)
                toReturn.add(node)
            }

            is PathNode.ReflectiveQuadTo -> {
                currPosition = Offset(node.x, node.y)
                toReturn.add(node)
            }

            is PathNode.RelativeCurveTo -> {
                val x1 = currPosition.x + node.dx1
                val y1 = currPosition.y + node.dy1
                val x2 = currPosition.x + node.dx2
                val y2 = currPosition.y + node.dy2
                val x3 = currPosition.x + node.dx3
                val y3 = currPosition.y + node.dy3
                currPosition = Offset(x3, y3)
                toReturn.add(PathNode.CurveTo(x1, y1, x2, y2, x3, y3))
            }

            is PathNode.RelativeHorizontalTo -> {
                val x = currPosition.x + node.dx
                toReturn.add(PathNode.LineTo(x, currPosition.y))
                currPosition = Offset(x, currPosition.y)
            }

            is PathNode.RelativeLineTo -> {
                val x = currPosition.x + node.dx
                val y = currPosition.y + node.dy
                currPosition = Offset(x, y)
                toReturn.add(PathNode.LineTo(x, y))
            }

            is PathNode.RelativeMoveTo -> {
                val x = currPosition.x + node.dx
                val y = currPosition.y + node.dy
                currPosition = Offset(x, y)
                toReturn.add(PathNode.MoveTo(x, y))
            }

            is PathNode.RelativeQuadTo -> {
                val x1 = currPosition.x + node.dx1
                val y1 = currPosition.y + node.dy1
                val x2 = currPosition.x + node.dx2
                val y2 = currPosition.y + node.dy2
                currPosition = Offset(x2, y2)
                toReturn.add(PathNode.QuadTo(x1, y1, x2, y2))
            }

            is PathNode.RelativeReflectiveCurveTo -> {
                // Check the last command in toReturn
                val prevNode = toReturn.lastOrNull()
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
                toReturn.add(PathNode.CurveTo(reflX1, reflY1, x2, y2, x3, y3))

                // Update current position
                currPosition = Offset(x3, y3)
            }

            is PathNode.RelativeReflectiveQuadTo -> {
                // Check the last command in toReturn
                val lastNode = toReturn.lastOrNull()
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
                toReturn.add(PathNode.QuadTo(reflX, reflY, endX, endY))

                // Update current position
                currPosition = Offset(endX, endY)
            }

            is PathNode.RelativeVerticalTo -> {
                val y = currPosition.y + node.dy
                toReturn.add(PathNode.LineTo(currPosition.x, y))
                currPosition = Offset(currPosition.x, y)
            }

            is PathNode.VerticalTo -> {
                toReturn.add(PathNode.LineTo(currPosition.x, node.y))
                currPosition = Offset(currPosition.x, node.y)
            }

            is PathNode.ArcTo -> {
                toReturn.add(node)
                currPosition = Offset(node.arcStartX, node.arcStartY)
            }

            is PathNode.RelativeArcTo -> {
                val arcStartX = currPosition.x + node.arcStartDx
                val arcStartY = currPosition.y + node.arcStartDy
                toReturn.add(
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
    }
    return toReturn
}

fun List<PathNode>.splitIntoClosedPaths(): List<List<PathNode>> {
    val toReturn = mutableListOf<List<PathNode>>()
    val currSubpath = mutableListOf<PathNode>()
    val subpathHasContentList = mutableListOf<Boolean>()
    val currentPosition = PointF(0f, 0f)
    var currSubpathHasContent = false
    this.forEach { node ->
        if (!(node is PathNode.RelativeMoveTo || node is PathNode.MoveTo || node == PathNode.Close)) {
            currSubpathHasContent = true
        }
        when (node) {
            is PathNode.MoveTo -> currentPosition.set(node.x, node.y)
            is PathNode.RelativeMoveTo -> currentPosition.offset(node.dx, node.dy)
            is PathNode.LineTo -> currentPosition.set(node.x, node.y)
            is PathNode.RelativeLineTo -> currentPosition.offset(node.dx, node.dy)
            is PathNode.HorizontalTo -> currentPosition.x = node.x
            is PathNode.RelativeHorizontalTo -> currentPosition.x += node.dx
            is PathNode.VerticalTo -> currentPosition.y = node.y
            is PathNode.RelativeVerticalTo -> currentPosition.y += node.dy
            is PathNode.CurveTo -> currentPosition.set(node.x3, node.y3)
            is PathNode.RelativeCurveTo -> currentPosition.offset(node.dx3, node.dy3)
            is PathNode.QuadTo -> currentPosition.set(node.x2, node.y2)
            is PathNode.RelativeQuadTo -> currentPosition.offset(node.dx2, node.dy2)
            is PathNode.ReflectiveCurveTo -> currentPosition.set(node.x2, node.y2)
            is PathNode.RelativeReflectiveCurveTo -> currentPosition.offset(node.dx2, node.dy2)
            is PathNode.ReflectiveQuadTo -> currentPosition.set(node.x, node.y)
            is PathNode.RelativeReflectiveQuadTo -> currentPosition.offset(node.dx, node.dy)
            else -> {} //Arc and close not implemented
        }
        currSubpath.add(node)
        if (node == PathNode.Close) {
            toReturn.add(currSubpath.toList())
            subpathHasContentList.add(currSubpathHasContent)
            currSubpathHasContent = false
            currSubpath.clear()
            currSubpath.add(PathNode.MoveTo(x = currentPosition.x, y = currentPosition.y))
        }
    }
    return toReturn.filterIndexed { index, sublist ->
        subpathHasContentList[index]
    }
}

fun List<PathNode>.toPath(): Path {
    return PathParser().addPathNodes(this).toPath()
}

fun List<PathNode>.splitIntoPaths(): List<Path> {
    val toReturn = mutableListOf<List<PathNode>>()
    val currSubpath = mutableListOf<PathNode>()
    val subpathHasContentList = mutableListOf<Boolean>()
    val currentPosition = PointF(0f, 0f)
    var currSubpathHasContent = false
    this.forEach { node ->
        if (!(node is PathNode.RelativeMoveTo || node is PathNode.MoveTo || node == PathNode.Close)) {
            currSubpathHasContent = true
        }
        when (node) {
            is PathNode.MoveTo -> currentPosition.set(node.x, node.y)
            is PathNode.RelativeMoveTo -> currentPosition.offset(node.dx, node.dy)
            is PathNode.LineTo -> currentPosition.set(node.x, node.y)
            is PathNode.RelativeLineTo -> currentPosition.offset(node.dx, node.dy)
            is PathNode.HorizontalTo -> currentPosition.x = node.x
            is PathNode.RelativeHorizontalTo -> currentPosition.x += node.dx
            is PathNode.VerticalTo -> currentPosition.y = node.y
            is PathNode.RelativeVerticalTo -> currentPosition.y += node.dy
            is PathNode.CurveTo -> currentPosition.set(node.x3, node.y3)
            is PathNode.RelativeCurveTo -> currentPosition.offset(node.dx3, node.dy3)
            is PathNode.QuadTo -> currentPosition.set(node.x2, node.y2)
            is PathNode.RelativeQuadTo -> currentPosition.offset(node.dx2, node.dy2)
            is PathNode.ReflectiveCurveTo -> currentPosition.set(node.x2, node.y2)
            is PathNode.RelativeReflectiveCurveTo -> currentPosition.offset(node.dx2, node.dy2)
            is PathNode.ReflectiveQuadTo -> currentPosition.set(node.x, node.y)
            is PathNode.RelativeReflectiveQuadTo -> currentPosition.offset(node.dx, node.dy)
            else -> {} //Arc and close not implemented
        }
        currSubpath.add(node)
        if (node == PathNode.Close) {
            toReturn.add(currSubpath.toList())
            subpathHasContentList.add(currSubpathHasContent)
            currSubpathHasContent = false
            currSubpath.clear()
            currSubpath.add(PathNode.MoveTo(x = currentPosition.x, y = currentPosition.y))
        }
    }
    val filtered = toReturn.filterIndexed { index, sublist ->
        subpathHasContentList[index]
    }
    return filtered.map { sublist -> PathParser().addPathNodes(sublist).toPath() }
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

private fun Iterable<PathSegment>.toPath(): Path {
    val toReturn = Path()
    toReturn.moveTo(this.first().start.x, this.first().start.y)
    this.forEach {
        toReturn.lineTo(it.end.x, it.end.y)
        Log.d(TAG, "toPath: ADDED LINETO")
    }
    return toReturn
}

fun Path.toScaledPath(bounds: Rect, targetWidth: Int, targetHeight: Int): Path {
    val xScaleFactor = targetWidth / bounds.width
    val yScaleFactor = targetHeight / bounds.height
    val xOffset = bounds.left
    val yOffset = bounds.top
    val androidPath = this.asAndroidPath()
    androidPath.transform(android.graphics.Matrix().apply {
        postTranslate(-xOffset, -yOffset)
        postScale(xScaleFactor, yScaleFactor)
    })
    return androidPath.asComposePath()
}

data class DrawSegment(
    val start: Offset,
    val end: Offset,
    val distance: Float,
    val pathNodes: List<PathNode>
)
