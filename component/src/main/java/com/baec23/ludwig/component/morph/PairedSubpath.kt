package com.baec23.ludwig.component.morph

import androidx.compose.ui.graphics.vector.PathNode
import kotlin.math.abs

class PairedSubpath(
    startPathNodes: List<PathNode>,
    endPathNodes: List<PathNode>,
) : AnimatedSubpath {
    private var startPathSegments: MutableList<PathSegment> =
        startPathNodes.toPathSegments().toMutableList()
    private var endPathSegments: MutableList<PathSegment> =
        endPathNodes.toPathSegments().toMutableList()

    init {
        equalizeSegments()
        val startFirst =
            startPathSegments.first { it.pathNode !is PathNode.MoveTo && it.pathNode != PathNode.Close }
        endPathSegments.removeAt(0) //Remove MoveTo
        endPathSegments.removeLast() //Remove Close
        var shortestDistance = Float.MAX_VALUE
        var closestEndIndex = -1
        for (i in endPathSegments.indices) {
            val distance = startFirst.distanceTo(endPathSegments[i])
            if (distance < shortestDistance) {
                shortestDistance = distance
                closestEndIndex = i
            }
        }
        val closestEndSegment = endPathSegments[closestEndIndex]
        endPathSegments = endPathSegments.rotateToIndex(closestEndIndex).toMutableList()
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
        endPathSegments.add(
            PathSegment(
                startPosition = closestEndSegment.startPosition,
                endPosition = closestEndSegment.startPosition,
                pathNode = PathNode.Close
            )
        )
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
}