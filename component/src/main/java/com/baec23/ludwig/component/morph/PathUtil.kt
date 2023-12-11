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
            is PathNode.HorizontalTo -> currentPosition.set(node.x, currentPosition.y)
            is PathNode.RelativeHorizontalTo -> currentPosition.offset(node.dx, 0f)
            is PathNode.VerticalTo -> currentPosition.set(currentPosition.x, node.y)
            is PathNode.RelativeVerticalTo -> currentPosition.offset(0f, node.dy)
            is PathNode.CurveTo -> currentPosition.set(node.x3, node.y3)
            is PathNode.RelativeCurveTo -> currentPosition.offset(node.dx3, node.dy3)
            is PathNode.QuadTo -> currentPosition.set(node.x2, node.y2)
            is PathNode.RelativeQuadTo -> currentPosition.offset(node.dx2, node.dy2)
            is PathNode.ReflectiveCurveTo -> currentPosition.set(node.x2, node.y2)
            is PathNode.RelativeReflectiveCurveTo -> currentPosition.offset(node.dx2, node.dy2)
            is PathNode.ReflectiveQuadTo -> currentPosition.set(node.x, node.y)
            is PathNode.RelativeReflectiveQuadTo -> currentPosition.offset(node.dx, node.dy)
            is PathNode.ArcTo -> currentPosition.set(
                currentPosition.x + node.arcStartX,
                currentPosition.y + node.arcStartY
            )

            is PathNode.RelativeArcTo -> currentPosition.offset(node.arcStartDx, node.arcStartDy)
            PathNode.Close -> {}
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

fun List<PathNode>.toCustomString(): String {
    val sb = StringBuilder()
    this.forEach { pathNode ->
        sb.append(pathNode.toCustomString())
    }
    return sb.toString()
}

fun List<PathNode>.getLength(): Float{
    val path = PathParser().addPathNodes(this).toPath()
    val pathMeasurer = PathMeasure()
    pathMeasurer.setPath(path, false)
    return pathMeasurer.length
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