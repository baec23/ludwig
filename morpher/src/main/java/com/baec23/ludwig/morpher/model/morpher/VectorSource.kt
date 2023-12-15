package com.baec23.ludwig.morpher.model.morpher

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.graphics.vector.VectorPath
import com.baec23.ludwig.morpher.util.normalize


data class VectorSource(
    val pathData: List<PathNode>,
    val bounds: Rect,
    val viewportSize: Size,
    val viewportOffset: Offset,
) {
    private val memo = HashMap<Offset, List<PathNode>>()

    companion object {
        /**
         * The 'd' attribute within a <path> element
         * If viewportWidth <= 0 or viewportHeight <=0, will just use path bounds
         */
        fun fromPathString(
            pathString: String,
            viewportSize: Size? = null,
            viewportOffset: Offset? = null,
        ): VectorSource {
            val pathParser = PathParser().parsePathString(pathString)
            val bounds = pathParser.toPath().getBounds()
            val pathData = pathParser.toNodes()
            val viewportWidth = viewportSize?.width ?: bounds.width
            val viewportHeight = viewportSize?.height ?: bounds.height
            val viewportOffsetX = viewportOffset?.x ?: bounds.left
            val viewportOffsetY = viewportOffset?.y ?: bounds.top
            return VectorSource(
                pathData,
                bounds,
                Size(viewportWidth, viewportHeight),
                Offset(viewportOffsetX, viewportOffsetY)
            )
        }

        fun fromImageVector(imageVector: ImageVector): VectorSource {
            val pathData = imageVector.root.filterIsInstance<VectorPath>().flatMap { it.pathData }
            val pathParser = PathParser()
            pathParser.addPathNodes(pathData)
            val bounds = pathParser.toPath().getBounds()
            return VectorSource(
                pathData,
                bounds,
                Size(imageVector.viewportWidth, imageVector.viewportHeight),
                Offset(0f, 0f)
            )
        }
    }

    fun getNormalizedPathData(width: Float, height: Float): List<PathNode> {
        if (memo.containsKey(Offset(width, height))) {
            return memo[Offset(width, height)]!!
        }
        //Calc offset / scale
        val targetWidth = if (width > 0f) width else bounds.width
        val targetHeight =
            if (height > 0f) height else bounds.height
        val scaleFactorX = targetWidth / bounds.width
        val scaleFactorY = targetHeight / bounds.height

        //Normalize (offset + scale paths)
        val normalized =
            pathData.normalize(Offset(bounds.left, bounds.top), scaleFactorX, scaleFactorY)
        memo[Offset(width, height)] = normalized
        return normalized
    }
}