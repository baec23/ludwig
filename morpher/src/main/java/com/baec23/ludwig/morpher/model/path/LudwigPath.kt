package com.baec23.ludwig.morpher.model.path

import android.graphics.RectF
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.vector.PathParser
import com.baec23.ludwig.morpher.util.toSubpaths

data class LudwigPath(
    val subpaths: List<LudwigSubpath>,
    val width: Float,
    val height: Float
) {
    companion object {
        /**
         * Compose Path
         */
        fun fromPath(
            path: androidx.compose.ui.graphics.Path,
            targetWidth: Float,
            targetHeight: Float
        ): LudwigPath {
            return fromPath(path.asAndroidPath(), targetWidth, targetHeight)
        }

        /**
         * Android Path
         */
        fun fromPath(
            path: android.graphics.Path,
            targetWidth: Float,
            targetHeight: Float
        ): LudwigPath {
            val bounds = RectF()
            path.computeBounds(bounds, true)
            val subpaths = androidx.graphics.path.PathIterator(path).asSequence().toList()
                .toSubpaths(bounds, targetWidth, targetHeight)
            return LudwigPath(subpaths, targetWidth, targetHeight)
        }
    }

    fun toComposePath(): Path {
        return PathParser().addPathNodes(subpaths.map { it.toPathNodes() }
            .flatten()).toPath()
    }
}
