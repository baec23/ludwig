package com.baec23.ludwig.morpher.model.path

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.PathNode
import com.baec23.ludwig.morpher.util.distanceTo
import com.baec23.ludwig.morpher.util.lerp
import kotlin.math.abs

class PairedSubpath(
    var startSubpath: LudwigSubpath,
    var endSubpath: LudwigSubpath,
) : AnimatedSubpath {

    init {
        if (startSubpath.isClosed && endSubpath.isClosed) {
            if (startSubpath.area > 0 && endSubpath.area < 0 || startSubpath.area < 0 && endSubpath.area > 0) {
                endSubpath = endSubpath.reverse()
            }
            val closestEndIndex =
                endSubpath.pathSegments.findShortestDistanceIndex(startSubpath.pathSegments.first().startPosition)
            endSubpath = endSubpath.rotateToIndex(closestEndIndex)
        }
        equalizeSegments()
    }

    override fun getInterpolatedPathNodes(fraction: Float): List<PathNode> {
        val startPathSegments = startSubpath.pathSegments
        val endPathSegments = endSubpath.pathSegments
        val interpolatedPathNodes = mutableListOf<PathNode>()
        val startX = lerp(
            startPathSegments.first().startPosition.x,
            endPathSegments.first().startPosition.x,
            fraction
        )
        val startY = lerp(
            startPathSegments.first().startPosition.y,
            endPathSegments.first().startPosition.y,
            fraction
        )
        interpolatedPathNodes.add(PathNode.MoveTo(startX, startY))

        startPathSegments.forEachIndexed { index, pathSegment ->
            interpolatedPathNodes.add(
                pathSegment.pathNode.lerp(
                    endPathSegments[index].pathNode, fraction
                )
            )
        }
        if (startSubpath.isClosed && endSubpath.isClosed) {
            interpolatedPathNodes.add(PathNode.Close)
        } else {
            if (startSubpath.isClosed && fraction == 0f) {
                interpolatedPathNodes.add(PathNode.Close)
            } else if (endSubpath.isClosed && fraction == 1f) {
                interpolatedPathNodes.add(PathNode.Close)
            }
        }
        return interpolatedPathNodes.toList()
    }

    private fun equalizeSegments() {
        val startPathSegments = startSubpath.pathSegments.toMutableList()
        val endPathSegments = endSubpath.pathSegments.toMutableList()
        val diff = startPathSegments.size - endPathSegments.size
        if (diff == 0) {
            return
        }
        if (diff < 0) { //End has more nodes - add more nodes to start
            //find start node with longest length and split it until diff 0
            for (i in 0 until abs(diff)) {
                val longestSegment =
                    startPathSegments.maxByOrNull { pathSegment -> pathSegment.length } ?: return
                val splitSegments = longestSegment.split(0.5f)
                val index = startPathSegments.indexOf(longestSegment)
                startPathSegments.removeAt(index)
                startPathSegments.addAll(index, splitSegments)
            }

        } else { //Start has more nodes
            for (i in 0 until abs(diff)) {
                val longestSegment =
                    endPathSegments.maxByOrNull { pathSegment -> pathSegment.length } ?: return
                val splitSegments = longestSegment.split(0.5f)
                val index = endPathSegments.indexOf(longestSegment)
                endPathSegments.removeAt(index)
                endPathSegments.addAll(index, splitSegments)
            }
        }
        startSubpath = startSubpath.copy(pathSegments = startPathSegments.toList())
        endSubpath = endSubpath.copy(pathSegments = endPathSegments.toList())
    }

    private fun List<PathSegment>.findShortestDistanceIndex(target: Offset): Int {
        var shortestDistance = Float.MAX_VALUE
        var closestEndIndex = -1
        for (i in this.indices) {
            val distance = target.distanceTo(this[i].startPosition)
            if (distance < shortestDistance) {
                shortestDistance = distance
                closestEndIndex = i
            }
        }
        return closestEndIndex
    }
}
