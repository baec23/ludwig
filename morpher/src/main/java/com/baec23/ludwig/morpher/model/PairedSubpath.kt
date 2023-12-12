package com.baec23.ludwig.morpher.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.PathNode
import com.baec23.ludwig.morpher.util.lerp
import com.baec23.ludwig.morpher.util.rotateToIndex
import com.baec23.ludwig.morpher.util.toPathSegments
import kotlin.math.abs

class PairedSubpath(
    startPathNodes: List<PathNode>,
    endPathNodes: List<PathNode>,
) : AnimatedSubpath {
    private var startPathSegments: MutableList<PathSegment> =
        startPathNodes.toPathSegments().toMutableList()
    private var endPathSegments: MutableList<PathSegment> =
        endPathNodes.toPathSegments().toMutableList()

    private val startIsClosed: Boolean = startPathNodes.find { it == PathNode.Close } != null
    private val endIsClosed: Boolean = endPathNodes.find { it == PathNode.Close } != null

    init {
        if (startIsClosed && endIsClosed) {
            val filtered =
                endPathSegments.filterNot { it.pathNode is PathNode.MoveTo || it.pathNode is PathNode.RelativeMoveTo }
            val closestEndIndex =
                filtered.findShortestDistanceIndex(startPathSegments.first { it.pathNode !is PathNode.MoveTo }.startPosition)
            val closestEndSegment = filtered[closestEndIndex]
            endPathSegments = filtered.rotateToIndex(closestEndIndex).toMutableList()
            endPathSegments.add(
                0, PathSegment(
                    startPosition = closestEndSegment.startPosition,
                    endPosition = closestEndSegment.startPosition,
                    pathNode = PathNode.MoveTo(
                        closestEndSegment.startPosition.x,
                        closestEndSegment.startPosition.y
                    )
                )
            )
        }

        equalizeSegments()
    }

    override fun getInterpolatedPathNodes(fraction: Float): List<PathNode> {
        val interpolatedPathNodes = mutableListOf<PathNode>()
        startPathSegments.forEachIndexed { index, pathSegment ->
            interpolatedPathNodes.add(
                pathSegment.pathNode.lerp(
                    endPathSegments[index].pathNode, fraction
                )
            )
        }
        if (startIsClosed && endIsClosed) {
            interpolatedPathNodes.add(PathNode.Close)
        } else {
            if (startIsClosed && fraction < 0.01f) {
                interpolatedPathNodes.add(PathNode.Close)
            } else if (endIsClosed && fraction > 0.99f) {
                interpolatedPathNodes.add(PathNode.Close)
            }
        }
        return interpolatedPathNodes.toList()
    }

    private fun equalizeSegments() {
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