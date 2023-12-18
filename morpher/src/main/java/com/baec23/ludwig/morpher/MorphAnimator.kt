package com.baec23.ludwig.morpher

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.graphics.vector.toPath
import com.baec23.ludwig.morpher.model.morpher.MorpherAnimationData
//import com.baec23.ludwig.morpher.model.morpher.MorpherPathData
import com.baec23.ludwig.morpher.model.morpher.MorpherPathData
import com.baec23.ludwig.morpher.model.morpher.VectorSource
import com.baec23.ludwig.morpher.model.path.PairedSubpath
import com.baec23.ludwig.morpher.model.path.UnpairedSubpath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.math.abs

class MorphAnimator() {
    lateinit var pathData: MorpherPathData
    lateinit var animationData: MorpherAnimationData

    private val pathParser = PathParser()
    private val pairedPath = Path()
    private val unpairedStartPath = Path()
    private val unpairedEndPath = Path()

    constructor(
        start: VectorSource,
        end: VectorSource,
        smoothness: Int = 100
    ) : this() {
        pathData = runBlocking { generatePathData(start, end) }
//        animationData = runBlocking { generateAnimationData(pathData, smoothness) }
    }

    companion object {
        //        suspend fun precomputeData(
//            start: VectorSource,
//            end: VectorSource,
//            width: Float = 0f,
//            height: Float = 0f,
//            smoothness: Int = 200
//        ): Pair<MorpherPathData, MorpherAnimationData> = withContext(Dispatchers.Default) {
//            val pathData = generatePathData(start, end, width, height)
//            val animationData = generateAnimationData(pathData, smoothness)
//            return@withContext Pair(pathData, animationData)
//        }
        private suspend fun generatePathData(
            start: VectorSource,
            end: VectorSource,
        ): MorpherPathData = withContext(Dispatchers.Default) {
            val startSubpaths =
                start.ludwigPath.subpaths.toMutableList()
            val endSubpaths =
                end.ludwigPath.subpaths.toMutableList()

            //Pair closedSubpaths
            val startClosedSubpaths = startSubpaths.filter { it.isClosed }
                .sortedByDescending { abs(it.area) }
            val endClosedSubpaths =
                endSubpaths.filter { it.isClosed }
                    .sortedByDescending { abs(it.area) }
            val closedPairedSubpaths = startClosedSubpaths.zip(endClosedSubpaths) { start, end ->
                startSubpaths.remove(start)
                endSubpaths.remove(end)
                PairedSubpath(start, end)
            }

            //Pair openSubpaths
            val startOpenSubpaths = startSubpaths.filter { !it.isClosed }
                .sortedByDescending { it.length }
            val endOpenSubpaths =
                endSubpaths.filter { !it.isClosed }.sortedByDescending { it.length }
            val openPairedSubpaths = startOpenSubpaths.zip(endOpenSubpaths) { start, end ->
                startSubpaths.remove(start)
                endSubpaths.remove(end)
                PairedSubpath(start, end)
            }

//            startSubpaths.removeAll(closedPairedSubpaths.map { pairedSubpath -> pairedSubpath.startSubpath })
//            endSubpaths.removeAll(closedPairedSubpaths.map { pairedSubpath -> pairedSubpath.endSubpath })
//            startSubpaths.removeAll(openPairedSubpaths.map { pairedSubpath -> pairedSubpath.startSubpath })
//            endSubpaths.removeAll(openPairedSubpaths.map { pairedSubpath -> pairedSubpath.endSubpath })
            //Pair remaining
            val startRemainingSubpaths = startSubpaths.sortedByDescending { maxOf(it.area, it.length) }
            val endRemainingSubpaths = endSubpaths.sortedByDescending { maxOf(it.area, it.length) }
            val remainingPairedSubpaths = startRemainingSubpaths.zip(endRemainingSubpaths) { start, end ->
                startSubpaths.remove(start)
                endSubpaths.remove(end)
                PairedSubpath(start, end)
            }
//            startSubpaths.removeAll(remainingPairedSubpaths.map { pairedSubpath -> pairedSubpath.startSubpath })
//            endSubpaths.removeAll(remainingPairedSubpaths.map { pairedSubpath -> pairedSubpath.endSubpath })

            val pairedSubpaths = closedPairedSubpaths + openPairedSubpaths + remainingPairedSubpaths
            val unpairedStartSubpaths = startSubpaths.map { subpath -> UnpairedSubpath(subpath) }
            val unpairedEndSubpaths = endSubpaths.map { subpath -> UnpairedSubpath(subpath) }

            return@withContext MorpherPathData(
                pairedSubpaths = pairedSubpaths,
                unpairedStartSubpaths = unpairedStartSubpaths,
                unpairedEndSubpaths = unpairedEndSubpaths
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
        pathParser.clear()
        val pathNodes = pathData.pairedSubpaths.map { it.getInterpolatedPathNodes(fraction) }
        pathParser.addPathNodes(pathNodes.flatten())
        return pathParser.toPath(pairedPath)
//
//        var cachedPath = animationData.getInterpolatedPairedPath(fraction)
//        if (cachedPath == null) {
////            Log.d("DEBUG", "Paired path cache miss - ${fraction}")
//            val pathNodes = pathData.pairedSubpaths.map { it.getInterpolatedPathNodes(fraction) }
//            val pathParser = PathParser()
//            pathParser.addPathNodes(pathNodes.flatten())
//            cachedPath = pathParser.toPath()
//            animationData.setInterpolatedPairedPath(fraction, cachedPath)
//        }
//        return cachedPath
    }

    fun getInterpolatedUnpairedStartPath(fraction: Float): Path {
        pathParser.clear()
        val pathNodes = pathData.unpairedStartSubpaths.map { it.getInterpolatedPathNodes(fraction) }
        pathParser.addPathNodes(pathNodes.flatten())
        return pathParser.toPath(unpairedStartPath)

//        var cachedPath = animationData.getInterpolatedUnpairedStartPath(fraction)
//        if (cachedPath == null) {
////            Log.d("DEBUG", "Unpaired start path cache miss - ${fraction}")
//            val pathNodes =
//                pathData.unpairedStartSubpaths.map { it.getInterpolatedPathNodes(fraction) }
//            val pathParser = PathParser()
//            pathParser.addPathNodes(pathNodes.flatten())
//            cachedPath = pathParser.toPath()
//            animationData.setInterpolatedUnpairedStartPath(fraction, cachedPath)
//        }
//        return cachedPath
    }

    fun getInterpolatedUnpairedEndPath(fraction: Float): Path {
        pathParser.clear()
        val pathNodes = pathData.unpairedEndSubpaths.map { it.getInterpolatedPathNodes(fraction) }
        pathParser.addPathNodes(pathNodes.flatten())
        return pathParser.toPath(unpairedEndPath)

//        var cachedPath = animationData.getInterpolatedUnpairedEndPath(fraction)
//        if (cachedPath == null) {
////            Log.d("DEBUG", "Unpaired end path cache miss - ${fraction}")
//            val pathNodes =
//                pathData.unpairedEndSubpaths.map { it.getInterpolatedPathNodes(fraction) }
//            val pathParser = PathParser()
//            pathParser.addPathNodes(pathNodes.flatten())
//            cachedPath = pathParser.toPath()
//            animationData.setInterpolatedEndPath(fraction, cachedPath)
//        }
//        return cachedPath
    }
}