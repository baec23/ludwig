package com.baec23.ludwig.morpher

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.PathParser
import com.baec23.ludwig.morpher.model.PairedSubpath
import com.baec23.ludwig.morpher.model.UnpairedSubpath
import com.baec23.ludwig.morpher.model.VectorSource
import com.baec23.ludwig.morpher.util.calcLength
import com.baec23.ludwig.morpher.util.normalize
import com.baec23.ludwig.morpher.util.splitPaths

class VectorAnimator(
    start: VectorSource,
    end: VectorSource,
    private val smoothness: Int = 500,
    width: Float = 0f,
    height: Float = 0f,
) {
    private val _pairedSubpaths: MutableList<PairedSubpath> = mutableListOf()
    val pairedSubpaths = _pairedSubpaths.toList()
    private val _unpairedStartSubpaths: MutableList<UnpairedSubpath> = mutableListOf()
    val unpairedStartSubpaths = _unpairedStartSubpaths.toList()
    private val _unpairedEndSubpaths: MutableList<UnpairedSubpath> = mutableListOf()
    val unpairedEndSubpaths = _unpairedEndSubpaths.toList()

    private val pathMemo = HashMap<Int, Path>()

    init {
        //Calc offset / scale
        val startBounds = start.bounds
        val endBounds = end.bounds
        val targetWidth = if (width >= 0f) width else maxOf(startBounds.width, endBounds.width)
        val targetHeight = if (height >= 0f) height else maxOf(startBounds.height, endBounds.height)
        val startScaleFactorX = targetWidth / startBounds.width
        val startScaleFactorY = targetHeight / startBounds.height
        val endScaleFactorX = targetWidth / endBounds.width
        val endScaleFactorY = targetHeight / endBounds.height

        //Normalize (offset + scale paths)
        val normalizedStartNodes =
            start.pathData.normalize(
                Offset(startBounds.left, startBounds.top),
                startScaleFactorX,
                startScaleFactorY
            )
        val normalizedEndNodes =
            end.pathData.normalize(
                Offset(endBounds.left, endBounds.top),
                endScaleFactorX,
                endScaleFactorY
            )

        //Split into subpaths
        val startSubpathNodes =
            normalizedStartNodes.splitPaths().sortedByDescending { it.calcLength() }

        val endSubpathNodes =
            normalizedEndNodes.splitPaths().sortedByDescending { it.calcLength() }

        //Arrange into paired (morphing) and unpaired (no morphing)
        val numStartSubpaths = startSubpathNodes.size
        val numEndSubpaths = endSubpathNodes.size
        val numAnimatedSubpaths = minOf(numStartSubpaths, numEndSubpaths)
        for (i in 0 until numAnimatedSubpaths) {
            _pairedSubpaths.add(PairedSubpath(startSubpathNodes[i], endSubpathNodes[i]))
        }
        for (i in numAnimatedSubpaths until numStartSubpaths) {
            _unpairedStartSubpaths.add(
                UnpairedSubpath(
                    startSubpathNodes[i]
                )
            )
        }
        for (i in numAnimatedSubpaths until numEndSubpaths) {
            _unpairedEndSubpaths.add(UnpairedSubpath(endSubpathNodes[i]))
        }
    }

    fun getInterpolatedPairedPath(fraction: Float): Path {
        val memoKey = (fraction * smoothness).toInt()
        if (pathMemo.containsKey(memoKey)) {
            return pathMemo[memoKey]!!
        }
        val pathNodes = _pairedSubpaths.map {
            it.getInterpolatedPathNodes(fraction)
        }

        val pathParser = PathParser()
        pathParser.addPathNodes(pathNodes.flatten())
        val path = pathParser.toPath()
        pathMemo[memoKey] = path
        return path
    }

    fun getInterpolatedUnpairedStartPath(fraction: Float): Path {
        val pathNodes = _unpairedStartSubpaths.map { it.getInterpolatedPathNodes(fraction) }
        val pathParser = PathParser()
        pathParser.addPathNodes(pathNodes.flatten())
        return pathParser.toPath()
    }

    fun getInterpolatedUnpairedEndPath(fraction: Float): Path {
        val pathNodes = _unpairedEndSubpaths.map { it.getInterpolatedPathNodes(fraction) }
        val pathParser = PathParser()
        pathParser.addPathNodes(pathNodes.flatten())
        return pathParser.toPath()
    }
}