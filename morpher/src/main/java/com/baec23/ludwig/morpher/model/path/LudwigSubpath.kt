package com.baec23.ludwig.morpher.model.path

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.graphics.vector.PathParser
import java.util.LinkedList

data class LudwigSubpath(
    val pathSegments: List<PathSegment>,
    val bounds: Rect,
    val isClosed: Boolean,
    val area: Float = 0f,
){
    val length: Float = pathSegments.map{it.length}.sum()
    fun reverse(): LudwigSubpath {
        val reversedPathSegments = LinkedList<PathSegment>()
        pathSegments.forEach { segment ->
            val node = segment.pathNode
            if (node !is PathNode.MoveTo) {
                node as PathNode.CurveTo
                val newNode = PathNode.CurveTo(
                    node.x2,
                    node.y2,
                    node.x1,
                    node.y1,
                    segment.startPosition.x,
                    segment.startPosition.y
                )
                reversedPathSegments.add(
                    0, PathSegment(
                        startPosition = Offset(node.x3, node.y3), endPosition = Offset(
                            segment.startPosition.x, segment.startPosition.y
                        ), pathNode = newNode
                    )
                )
            }
        }
        if(isClosed){
            return this.copy(pathSegments = reversedPathSegments.toList(), area = -this.area!!)
        }
        return this.copy(pathSegments = reversedPathSegments.toList())
    }
    fun rotateToIndex(index: Int): LudwigSubpath {
        return this.copy(
            pathSegments = pathSegments.subList(
                index, pathSegments.size
            ) + pathSegments.subList(0, index)
        )
    }
    fun toPathNodes(): List<PathNode> {
        val pathNodes = mutableListOf<PathNode>()
        pathNodes.add(
            PathNode.MoveTo(
                this.pathSegments.first().startPosition.x, this.pathSegments.first().startPosition.y
            )
        )
        pathNodes.addAll(this.pathSegments.map { it.pathNode })
        if (isClosed) {
            pathNodes.add(PathNode.Close)
        }
        return pathNodes.toList()
    }

    fun toComposePath(): Path{
        return PathParser().addPathNodes(toPathNodes()).toPath()
    }
}