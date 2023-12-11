package com.baec23.ludwig.component.morph

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.graphics.vector.PathParser

class UnpairedStartSubpath(startPathNodes: List<PathNode>) : AnimatedSubpath {
    private var center: Offset
    private var startPathSegments: MutableList<PathSegment> =
        startPathNodes.toPathSegments().toMutableList()

    init {
        val bounds = PathParser().addPathNodes(startPathNodes).toPath().getBounds()
        center = bounds.center
    }

    override fun getInterpolatedPathNodes(fraction: Float): List<PathNode> {
        val interpolatedPathNodes = mutableListOf<PathNode>()
        startPathSegments.forEach { pathSegment ->
            interpolatedPathNodes.add(
                pathSegment.pathNode.lerp(
                    center, fraction
                )
            )
        }
        return interpolatedPathNodes.toList()
    }
}