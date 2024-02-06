package com.baec23.ludwig.morpher.model.path

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.PathNode
import com.baec23.ludwig.morpher.util.lerp

data class UnpairedSubpath(val subpath: LudwigSubpath) : AnimatedSubpath {
    private val center: Offset = subpath.toComposePath().getBounds().center

    override fun getInterpolatedPathNodes(fraction: Float): List<PathNode> {
        val pathSegments = subpath.pathSegments
        val interpolatedPathNodes = mutableListOf<PathNode>()
        val startX = lerp(
            pathSegments.first().startPosition.x,
            center.x,
            fraction
        )
        val startY = lerp(
            pathSegments.first().startPosition.y,
            center.y,
            fraction
        )
        interpolatedPathNodes.add(PathNode.MoveTo(startX, startY))

        pathSegments.forEach { pathSegment ->
            interpolatedPathNodes.add(
                pathSegment.pathNode.lerp(
                    center, fraction
                )
            )
        }
        if (subpath.isClosed) {
            interpolatedPathNodes.add(PathNode.Close)
        }
        return interpolatedPathNodes.toList()
    }
}
