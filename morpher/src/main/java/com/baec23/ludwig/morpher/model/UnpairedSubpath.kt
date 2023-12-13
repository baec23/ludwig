package com.baec23.ludwig.morpher.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.graphics.vector.PathParser
import com.baec23.ludwig.morpher.util.lerp
import com.baec23.ludwig.morpher.util.toPathSegments

class UnpairedSubpath(pathNodes: List<PathNode>) : AnimatedSubpath {
    private val isClosed: Boolean = pathNodes.find { it == PathNode.Close } != null
    private var center: Offset
    private var pathSegments: MutableList<PathSegment> =
        pathNodes.toPathSegments().toMutableList()

    init {
        val bounds = PathParser().addPathNodes(pathNodes).toPath().getBounds()
        center = bounds.center
    }

    override fun getInterpolatedPathNodes(fraction: Float): List<PathNode> {
        val interpolatedPathNodes = mutableListOf<PathNode>()
        pathSegments.forEach { pathSegment ->
            interpolatedPathNodes.add(
                pathSegment.pathNode.lerp(
                    center, fraction
                )
            )
        }
        if (isClosed) {
            interpolatedPathNodes.add(PathNode.Close)
        }
        return interpolatedPathNodes.toList()
    }
}