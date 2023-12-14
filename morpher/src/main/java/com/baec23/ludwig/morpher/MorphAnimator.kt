package com.baec23.ludwig.morpher

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.PathParser
import com.baec23.ludwig.morpher.model.morpher.MorpherAnimationData
import com.baec23.ludwig.morpher.model.morpher.MorpherPathData
import com.baec23.ludwig.morpher.model.path.PairedSubpath
import com.baec23.ludwig.morpher.model.path.UnpairedSubpath
import com.baec23.ludwig.morpher.model.morpher.VectorSource
import com.baec23.ludwig.morpher.util.calcLength
import com.baec23.ludwig.morpher.util.normalize
import com.baec23.ludwig.morpher.util.splitPaths
import com.baec23.ludwig.morpher.util.toPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MorphAnimator() {
    lateinit var pathData: MorpherPathData
    lateinit var animationData: MorpherAnimationData

    constructor(
        start: VectorSource,
        end: VectorSource,
        width: Float = 0f,
        height: Float = 0f,
        smoothness: Int = 100
    ) : this() {
        pathData = runBlocking { generatePathData(start, end, width, height) }
        val pairedArray = Array<Path?>(smoothness + 1) { null }
        val unpairedStartArray = Array<Path?>(smoothness + 1) { null }
        val unpairedEndArray = Array<Path?>(smoothness + 1) { null }

        pairedArray[0] = pathData.pairedSubpaths.map { pairedSubpath ->
            pairedSubpath.getInterpolatedPathNodes(0f)
        }.flatten().toPath()
        pairedArray[smoothness] = pathData.pairedSubpaths.map { pairedSubpath ->
            pairedSubpath.getInterpolatedPathNodes(1f)
        }.flatten().toPath()

        unpairedStartArray[0] = pathData.unpairedStartSubpaths.map { pairedSubpath ->
            pairedSubpath.getInterpolatedPathNodes(0f)
        }.flatten().toPath()
        unpairedStartArray[smoothness] = pathData.unpairedStartSubpaths.map { pairedSubpath ->
            pairedSubpath.getInterpolatedPathNodes(1f)
        }.flatten().toPath()

        unpairedEndArray[0] = pathData.unpairedEndSubpaths.map { pairedSubpath ->
            pairedSubpath.getInterpolatedPathNodes(0f)
        }.flatten().toPath()
        unpairedEndArray[smoothness] = pathData.unpairedEndSubpaths.map { pairedSubpath ->
            pairedSubpath.getInterpolatedPathNodes(1f)
        }.flatten().toPath()

        animationData = MorpherAnimationData(
            pairedPaths = pairedArray,
            unpairedStartPaths = unpairedStartArray,
            unpairedEndPaths = unpairedEndArray
        )
    }

    constructor(
        precomputedPathData: MorpherPathData,
        precomputedAnimationData: MorpherAnimationData
    ) : this() {
        this.pathData = precomputedPathData
        this.animationData = precomputedAnimationData
    }

    companion object {
        suspend fun precomputeData(
            start: VectorSource,
            end: VectorSource,
            width: Float = 0f,
            height: Float = 0f,
            smoothness: Int = 200
        ): Pair<MorpherPathData, MorpherAnimationData> = withContext(Dispatchers.Default) {
            val pathData = generatePathData(start, end, width, height)
            val animationData = generateAnimationData(pathData, smoothness)
            return@withContext Pair(pathData, animationData)
        }

        private suspend fun generatePathData(
            start: VectorSource,
            end: VectorSource,
            width: Float = 0f,
            height: Float = 0f
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

        private suspend fun generateAnimationData(
            pathData: MorpherPathData,
            smoothness: Int
        ): MorpherAnimationData = withContext(Dispatchers.Default) {
            val paired = async {
                Array<Path?>(smoothness + 1) { index ->
                    pathData.pairedSubpaths.map { subpath ->
                        subpath.getInterpolatedPathNodes(index / smoothness.toFloat())
                    }.flatten().toPath()
                }
            }
            val unpairedStart = async {
                Array<Path?>(smoothness + 1) { index ->
                    pathData.unpairedStartSubpaths.map { subpath ->
                        subpath.getInterpolatedPathNodes(index / smoothness.toFloat())

                    }.flatten().toPath()
                }
            }
            val unpairedEnd = async {
                Array<Path?>(smoothness + 1) { index ->
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
        var cachedPath = animationData.getInterpolatedPairedPath(fraction)
        if (cachedPath == null) {
            Log.d("DEBUG", "Paired path cache miss - ${fraction}")
            val pathNodes = pathData.pairedSubpaths.map { it.getInterpolatedPathNodes(fraction) }
            val pathParser = PathParser()
            pathParser.addPathNodes(pathNodes.flatten())
            cachedPath = pathParser.toPath()
            animationData.setInterpolatedPairedPath(fraction, cachedPath)
        }
        return cachedPath
    }

    fun getInterpolatedUnpairedStartPath(fraction: Float): Path {
        var cachedPath = animationData.getInterpolatedUnpairedStartPath(fraction)
        if (cachedPath == null) {
            Log.d("DEBUG", "Unpaired start path cache miss - ${fraction}")
            val pathNodes =
                pathData.unpairedStartSubpaths.map { it.getInterpolatedPathNodes(fraction) }
            val pathParser = PathParser()
            pathParser.addPathNodes(pathNodes.flatten())
            cachedPath = pathParser.toPath()
            animationData.setInterpolatedUnpairedStartPath(fraction, cachedPath)
        }
        return cachedPath
    }

    fun getInterpolatedUnpairedEndPath(fraction: Float): Path {
        var cachedPath = animationData.getInterpolatedUnpairedEndPath(fraction)
        if (cachedPath == null) {
            Log.d("DEBUG", "Unpaired end path cache miss - ${fraction}")
            val pathNodes =
                pathData.unpairedEndSubpaths.map { it.getInterpolatedPathNodes(fraction) }
            val pathParser = PathParser()
            pathParser.addPathNodes(pathNodes.flatten())
            cachedPath = pathParser.toPath()
            animationData.setInterpolatedEndPath(fraction, cachedPath)
        }
        return cachedPath
    }
}