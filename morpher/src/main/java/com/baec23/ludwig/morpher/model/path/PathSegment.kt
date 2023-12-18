package com.baec23.ludwig.morpher.model.path

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.graphics.vector.PathParser
import com.baec23.ludwig.morpher.util.lerp

data class PathSegment(
    val startPosition: Offset,
    val endPosition: Offset,
    val pathNode: PathNode,
) {
    val path: Path = PathParser().addPathNodes(
        listOf(
            PathNode.MoveTo(startPosition.x, startPosition.y),
            pathNode
        )
    ).toPath()
    val length: Float
    private val pathMeasurer: PathMeasure = PathMeasure()

    init {
        pathMeasurer.setPath(path, false)
        length = pathMeasurer.length
    }

    fun split(fraction: Float): List<PathSegment> {
        val start = this.startPosition
        this.pathNode as PathNode.CurveTo
        val cp1 = Offset(this.pathNode.x1, this.pathNode.y1)
        val cp2 = Offset(this.pathNode.x2, this.pathNode.y2)
        val end = this.endPosition

        val (values1, values2) = deCasteljauSplit(start, cp1, cp2, end, fraction)

        val node1 = PathNode.CurveTo(
            x1 = values1[1].x,
            y1 = values1[1].y,
            x2 = values1[2].x,
            y2 = values1[2].y,
            x3 = values1[3].x,
            y3 = values1[3].y
        )
        val node2 = PathNode.CurveTo(
            x1 = values2[1].x,
            y1 = values2[1].y,
            x2 = values2[2].x,
            y2 = values2[2].y,
            x3 = values2[3].x,
            y3 = values2[3].y
        )
        val pathSegment1 =
            PathSegment(startPosition = startPosition, endPosition = values1[3], pathNode = node1)
        val pathSegment2 =
            PathSegment(startPosition = values2[0], endPosition = endPosition, pathNode = node2)
        return listOf(pathSegment1, pathSegment2)
    }
}

private fun deCasteljauSplit(
    start: Offset,
    cp1: Offset,
    cp2: Offset,
    end: Offset,
    fraction: Float
): Pair<List<Offset>, List<Offset>> {
    val p01 = lerp(start, cp1, fraction)
    val p12 = lerp(cp1, cp2, fraction)
    val p23 = lerp(cp2, end, fraction)

    val p012 = lerp(p01, p12, fraction)
    val p123 = lerp(p12, p23, fraction)

    val p0123 = lerp(p012, p123, fraction)

    val firstCurve = listOf(start, p01, p012, p0123)
    val secondCurve = listOf(p0123, p123, p23, end)

    return Pair(firstCurve, secondCurve)
}