package com.baec23.ludwig.component.morph

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.graphics.vector.PathParser

class UnpairedEndSubpath(private val endPathNodes: List<PathNode>) : AnimatedSubpath {
    private var center: Offset
    private var endPathSegments: MutableList<PathSegment> =
        endPathNodes.toPathSegments().toMutableList()

    init {
        val bounds = PathParser().addPathNodes(endPathNodes).toPath().getBounds()
        center = bounds.center
    }

    override fun getInterpolatedPathNodes(fraction: Float): List<PathNode> {
        val interpolatedPathNodes = mutableListOf<PathNode>()
        endPathSegments.forEach { pathSegment ->
            interpolatedPathNodes.add(
                pathSegment.pathNode.lerp(
                    center, fraction
                )
            )
        }
        return interpolatedPathNodes.toList()
    }
}