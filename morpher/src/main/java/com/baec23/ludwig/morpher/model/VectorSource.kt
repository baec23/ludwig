package com.baec23.ludwig.morpher.model

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.graphics.vector.VectorPath


data class VectorSource(
    val pathData: List<PathNode>,
    val bounds: Rect
) {
    companion object {
        fun fromPathString(pathString: String): VectorSource {
            val pathParser = PathParser().parsePathString(pathString)
            val bounds = pathParser.toPath().getBounds()
            val pathData = pathParser.toNodes()
            return VectorSource(pathData, bounds)
        }

        fun fromImageVector(imageVector: ImageVector): VectorSource {
            val pathData = imageVector.root.filterIsInstance<VectorPath>().flatMap { it.pathData }
            val pathParser = PathParser()
            pathParser.addPathNodes(pathData)
            val bounds = pathParser.toPath().getBounds()
            return VectorSource(pathData, bounds)
        }

        fun fromVectorPath(vectorPath: VectorPath): VectorSource {
            val pathData = vectorPath.pathData
            val pathParser = PathParser()
            pathParser.addPathNodes(pathData)
            val bounds = pathParser.toPath().getBounds()
            return VectorSource(pathData, bounds)
        }
    }
}