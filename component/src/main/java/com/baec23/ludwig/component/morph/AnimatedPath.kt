package com.baec23.ludwig.component.morph

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.graphics.vector.VectorPath

class AnimatedPath(
    startPathString: String,
    endPathString: String,
    private val smoothness: Int = 500,
    width: Float = 0f,
    height: Float = 0f,
) {
    val pairedSubpaths: MutableList<PairedSubpath> = mutableListOf()
    val unpairedStartSubpaths: MutableList<UnpairedStartSubpath> = mutableListOf()
    val unpairedEndSubpaths: MutableList<UnpairedEndSubpath> = mutableListOf()

    private val pathMemo = HashMap<Int, Path>()

    init {
        val lockPath = Icons.Outlined.Lock.root[0] as VectorPath
        val checkCircle = Icons.Outlined.CheckCircle.root[0] as VectorPath
        val startBounds = lockPath.pathData.toPath().getBounds()
//        val startBounds = PathParser().parsePathString(startPathString).toPath().getBounds()
        val endBounds = checkCircle.pathData.toPath().getBounds()
//        val endBounds = PathParser().parsePathString(endPathString).toPath().getBounds()
        val targetWidth = if (width >= 0f) width else maxOf(startBounds.width, endBounds.width)
        val targetHeight = if (height >= 0f) height else maxOf(startBounds.height, endBounds.height)
        val startScaleFactorX = targetWidth / startBounds.width
        val startScaleFactorY = targetHeight / startBounds.height
        val endScaleFactorX = targetWidth / endBounds.width
        val endScaleFactorY = targetHeight / endBounds.height
        val a = Icons.Outlined.Lock.root
//
        val normalizedStartPathNodes =
            lockPath.pathData.normalize(
                Offset(startBounds.left, startBounds.top),
                startScaleFactorX,
                startScaleFactorY
            )


//        val normalizedStartPathNodes =
//            PathParser().parsePathString(startPathString).toNodes().normalize(
//                Offset(startBounds.left, startBounds.top),
//                startScaleFactorX,
//                startScaleFactorY
//            )

//        val normalizedEndPathNodes =
//            PathParser().parsePathString(endPathString).toNodes().normalize(
//                Offset(endBounds.left, endBounds.top),
//                endScaleFactorX,
//                endScaleFactorY
//            )

        val normalizedEndPathNodes =
            checkCircle.pathData.normalize(
                Offset(endBounds.left, endBounds.top),
                endScaleFactorX,
                endScaleFactorY
            )

        val startSubpathNodes =
            normalizedStartPathNodes.splitIntoClosedPaths().sortedByDescending { it.getLength() }

        val endSubpathNodes =
            normalizedEndPathNodes.splitIntoClosedPaths().sortedByDescending { it.getLength() }


        val numStartSubpaths = startSubpathNodes.size
        val numEndSubpaths = endSubpathNodes.size
        val numAnimatedSubpaths = minOf(numStartSubpaths, numEndSubpaths)
        for (i in 0 until numAnimatedSubpaths) {
            pairedSubpaths.add(PairedSubpath(startSubpathNodes[i], endSubpathNodes[i]))
        }
        for (i in numAnimatedSubpaths until numStartSubpaths) {
            unpairedStartSubpaths.add(
                UnpairedStartSubpath(
                    startSubpathNodes[i]
                )
            )
        }
        for (i in numAnimatedSubpaths until numEndSubpaths) {
            unpairedEndSubpaths.add(UnpairedEndSubpath(endSubpathNodes[i]))
        }
    }

    fun getInterpolatedPath(fraction: Float): Path {
        val memoKey = (fraction * smoothness).toInt()
        if (pathMemo.containsKey(memoKey)) {
            return pathMemo[memoKey]!!
        }
        val pathNodes = pairedSubpaths.map {
            it.getInterpolatedPathNodes(fraction)
        }.toMutableList()

        val pathParser = PathParser()
        pathParser.addPathNodes(pathNodes.flatten())
        val path = pathParser.toPath()
        pathMemo[memoKey] = path
        return path
    }

    fun getUnpairedStartPath(fraction: Float): Path {
        val pathNodes = unpairedStartSubpaths.map { it.getInterpolatedPathNodes(fraction) }
        val pathParser = PathParser()
        pathParser.addPathNodes(pathNodes.flatten())
        return pathParser.toPath()
    }

    fun getUnpairedEndPath(fraction: Float): Path {
        val pathNodes = unpairedEndSubpaths.map { it.getInterpolatedPathNodes(fraction) }
        val pathParser = PathParser()
        pathParser.addPathNodes(pathNodes.flatten())
        return pathParser.toPath()
    }
}