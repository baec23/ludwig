package com.baec23.ludwig.morpher

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.PathParser
import com.baec23.ludwig.morpher.model.MorpherAnimationData
import com.baec23.ludwig.morpher.model.MorpherPathData
import com.baec23.ludwig.morpher.model.PairedSubpath
import com.baec23.ludwig.morpher.model.UnpairedSubpath
import com.baec23.ludwig.morpher.model.VectorSource
import com.baec23.ludwig.morpher.util.calcLength
import com.baec23.ludwig.morpher.util.normalize
import com.baec23.ludwig.morpher.util.splitPaths
import com.baec23.ludwig.morpher.util.toPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MorphAnimator() {
    lateinit var pathData: MorpherPathData
    private var precomputedAnimationData: MorpherAnimationData? = null

    constructor(start: VectorSource, end: VectorSource, width: Float, height: Float) : this() {
        pathData = runBlocking { generatePathData(start, end, width, height) }
    }

    constructor(
        pathData: MorpherPathData,
        precomputedAnimationData: MorpherAnimationData
    ) : this() {
        this.pathData = pathData
        this.precomputedAnimationData = precomputedAnimationData
    }

    companion object {
        suspend fun generatePathData(
            start: VectorSource, end: VectorSource, width: Float, height: Float
        ): MorpherPathData = withContext(Dispatchers.Default) {
            //Calc offset / scale
            val startBounds = start.bounds
            val endBounds = end.bounds
            val targetWidth = if (width >= 0f) width else maxOf(startBounds.width, endBounds.width)
            val targetHeight =
                if (height >= 0f) height else maxOf(startBounds.height, endBounds.height)
            val startScaleFactorX = targetWidth / startBounds.width
            val startScaleFactorY = targetHeight / startBounds.height
            val endScaleFactorX = targetWidth / endBounds.width
            val endScaleFactorY = targetHeight / endBounds.height

            //Normalize (offset + scale paths)
            val normalizedStartNodes = start.pathData.normalize(
                Offset(startBounds.left, startBounds.top), startScaleFactorX, startScaleFactorY
            )
            val normalizedEndNodes = end.pathData.normalize(
                Offset(endBounds.left, endBounds.top), endScaleFactorX, endScaleFactorY
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
            val _pairedSubpaths = mutableListOf<PairedSubpath>()
            val _unpairedStartSubpaths = mutableListOf<UnpairedSubpath>()
            val _unpairedEndSubpaths = mutableListOf<UnpairedSubpath>()

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
            return@withContext MorpherPathData(
                pairedSubpaths = _pairedSubpaths.toList(),
                unpairedStartSubpaths = _unpairedStartSubpaths.toList(),
                unpairedEndSubpaths = _unpairedEndSubpaths.toList()
            )
        }

        suspend fun generateAnimationData(
            pathData: MorpherPathData,
            smoothness: Int
        ): MorpherAnimationData = withContext(Dispatchers.Default) {
            val paired = async {
                Array(smoothness + 1) { index ->
                    pathData.pairedSubpaths.map { subpath ->
                        subpath.getInterpolatedPathNodes(index / smoothness.toFloat())
                    }.flatten().toPath()
                }
            }
            val unpairedStart = async {
                Array(smoothness + 1) { index ->
                    pathData.unpairedStartSubpaths.map { subpath ->
                        subpath.getInterpolatedPathNodes(index / smoothness.toFloat())

                    }.flatten().toPath()
                }
            }
            val unpairedEnd = async {
                Array(smoothness + 1) { index ->
                    pathData.unpairedEndSubpaths.map { subpath ->
                        subpath.getInterpolatedPathNodes(index / smoothness.toFloat())

                    }.flatten().toPath()
                }
            }
            val pairedPaths = paired.await()
            val unpairedStartPaths = unpairedStart.await()
            val unpairedEndPaths = unpairedEnd.await()
            return@withContext MorpherAnimationData(
                pairedPaths = pairedPaths,
                unpairedStartPaths = unpairedStartPaths,
                unpairedEndPaths = unpairedEndPaths
            )
        }
    }

    fun getInterpolatedPairedPath(fraction: Float): Path {
        precomputedAnimationData?.let {
            return it.getInterpolatedPairedPath(fraction)
        }
        val pathNodes = pathData.pairedSubpaths.map { it.getInterpolatedPathNodes(fraction) }
        val pathParser = PathParser()
        pathParser.addPathNodes(pathNodes.flatten())
        return pathParser.toPath()
    }

    fun getInterpolatedUnpairedStartPath(fraction: Float): Path {
        precomputedAnimationData?.let {
            return it.getInterpolatedUnpairedStartPath(fraction)
        }
        val pathNodes = pathData.unpairedStartSubpaths.map { it.getInterpolatedPathNodes(fraction) }
        val pathParser = PathParser()
        pathParser.addPathNodes(pathNodes.flatten())
        return pathParser.toPath()
    }

    fun getInterpolatedUnpairedEndPath(fraction: Float): Path {
        precomputedAnimationData?.let {
            return it.getInterpolatedUnpairedEndPath(fraction)
        }
        val pathNodes = pathData.unpairedEndSubpaths.map { it.getInterpolatedPathNodes(fraction) }
        val pathParser = PathParser()
        pathParser.addPathNodes(pathNodes.flatten())
        return pathParser.toPath()
    }

    suspend fun precomputeAnimationData(smoothness: Int) = coroutineScope {
        precomputedAnimationData =
            generateAnimationData(pathData = pathData, smoothness = smoothness)
    }
}