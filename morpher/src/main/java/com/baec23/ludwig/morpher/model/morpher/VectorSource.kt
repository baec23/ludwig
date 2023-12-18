package com.baec23.ludwig.morpher.model.morpher

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.graphics.vector.VectorPath
import androidx.compose.ui.graphics.vector.toPath
import com.baec23.ludwig.morpher.model.path.LudwigPath


data class VectorSource(
    val ludwigPath: LudwigPath,
    val viewportSize: Size,
    val viewportOffset: Offset,
    val vectorSize: Size,
    val vectorOffset: Offset
) {
    companion object {
        /**
         * The 'd' attribute within a <path> element
         * If viewportSize and/or viewportOffset are null, will use bounds
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
                LudwigPath.fromPath(pathData.toPath(), 1000f, 1000f),
                Size(viewportWidth, viewportHeight),
                Offset(viewportOffsetX, viewportOffsetY),
                Size(1000f, 1000f),
                Offset(0f, 0f)
            )
        }

        fun fromImageVector(imageVector: ImageVector): VectorSource {
            val pathData = imageVector.root.filterIsInstance<VectorPath>().flatMap { it.pathData }
            return VectorSource(
                LudwigPath.fromPath(pathData.toPath(), 1000f, 1000f),
                Size(imageVector.viewportWidth, imageVector.viewportHeight),
                Offset(0f, 0f),
                Size(1000f, 1000f),
                Offset(0f, 0f)
            )
        }
    }
}