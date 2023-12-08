package com.baec23.ludwig.component.morph

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.graphics.vector.PathParser
import kotlin.math.abs
import kotlin.math.roundToInt

class AnimatedPath(
    startPathString: String,
    endPathString: String,
    private val smoothness: Int = 200
) {
    val startNodes: MutableList<PathNode>
    var startPoints: List<Offset>
    val endNodes: MutableList<PathNode>
    var endPoints: List<Offset>
    var bounds: Rect
    private val pathMemo = HashMap<Int, Path>()

    init {
        val starts =
            PathParser().parsePathString(startPathString).toNodes().convertToAbsoluteCommands()
        startNodes = starts.first.toMutableList()
        startPoints = starts.second

        val ends = PathParser().parsePathString(endPathString).toNodes().convertToAbsoluteCommands()
        endNodes = ends.first.toMutableList()
        endPoints = ends.second

        equalizeNumNodes()
        startPoints = startNodes.convertToAbsoluteCommands().second
        endPoints = endNodes.convertToAbsoluteCommands().second
        val a = PathParser().addPathNodes(startNodes)
        bounds = a.toPath().getBounds()
    }

    fun getInterpolatedPath(fraction: Float): Path {
        val memoKey = (fraction * smoothness).roundToInt()
        pathMemo[memoKey]?.let {
            return it
        }
        val interpolatedPathNodes = mutableListOf<PathNode>()
        startNodes.forEachIndexed { index, pathNode ->
            interpolatedPathNodes.add(pathNode.lerp(endNodes[index], fraction))
        }
        val pathParser = PathParser()
        pathParser.addPathNodes(interpolatedPathNodes)
        val path = pathParser.toPath()
        pathMemo[memoKey] = path
        return path
    }

    fun getStartEndPoints(): List<Offset> {
        var currX = 0f
        var currY = 0f
        val toReturn = mutableListOf<Offset>()
        startNodes.forEach { node ->
            when (node) {
                PathNode.Close -> {}

                is PathNode.MoveTo -> {
                    currX = node.x
                    currY = node.y
                    toReturn.add(Offset(currX, currY))
                }

                is PathNode.LineTo -> {
                    currX = node.x
                    currY = node.y
                    toReturn.add(Offset(currX, currY))
                }

                else -> TODO()
            }
        }
        return toReturn.toList()
    }

    fun getEndEndpoints(): List<Offset> {
        var currX = 0f
        var currY = 0f
        val toReturn = mutableListOf<Offset>()
        endNodes.forEach { node ->
            when (node) {
                PathNode.Close -> {}
                is PathNode.MoveTo -> {
                    currX = node.x
                    currY = node.y
                    toReturn.add(Offset(currX, currY))
                }

                is PathNode.LineTo -> {
                    currX = node.x
                    currY = node.y
                    toReturn.add(Offset(currX, currY))
                }

                else -> TODO()
            }
        }
        return toReturn.toList()
    }

    private fun equalizeNumNodes() {
        val diff = startNodes.size - endNodes.size
        if (diff == 0) {
            return
        }
        if (diff < 0) { //End has more nodes - add more nodes to start
            //find start node with longest length and split it until diff 0
            for (i in 0 until abs(diff)) {
                val longestNodeIndex = findLongestNodeIndex(startNodes.subList(1, startNodes.lastIndex-1)) + 1
                val longestNode = startNodes.toList()[longestNodeIndex]
                val splitNodes = splitNode(longestNode, listOf(0.5f))
                startNodes.removeAt(longestNodeIndex)
                startNodes.addAll(longestNodeIndex, splitNodes.toList())
            }

        } else { //Start has more nodes
            TODO()
        }
    }

    private fun findLongestNodeIndex(nodes: List<PathNode>): Int {
        var maxLength = 0f
        var toReturn = 0
        var currPosition: Offset = Offset.Zero
        nodes.forEachIndexed { index, node ->
            when (node) {
                is PathNode.LineTo -> {
                    val length = node.getLength(currPosition)
                    if (length > maxLength) {
                        maxLength = length
                        toReturn = index
                    }
                    currPosition = Offset(node.x, node.y)
                }

                is PathNode.MoveTo -> {
                    currPosition = Offset(node.x, node.y)
                }

                is PathNode.Close -> {
                    currPosition = Offset.Zero
                }

                else -> TODO()
            }

        }
        return toReturn
    }

    private fun splitNode(node: PathNode, splitFractions: List<Float>): List<PathNode> {
        if (splitFractions.isEmpty()) {
            return emptyList()
        }
        val toReturn = mutableListOf<PathNode>()
        when (node) {
            is PathNode.ArcTo -> TODO()
            is PathNode.CurveTo -> TODO()
            is PathNode.LineTo -> {
                val endX = node.x
                val endY = node.y
                val offsets = splitFractions.map { fraction ->
                    Offset(
                        x = endX * fraction,
                        y = endY * fraction
                    )
                }
                offsets.forEach { offset ->
                    toReturn.add(PathNode.LineTo(offset.x, offset.y))
                }
                toReturn.add(PathNode.LineTo(node.x, node.y))
            }

            is PathNode.QuadTo -> TODO()
            PathNode.Close -> return emptyList()
            else -> TODO()
        }
        return toReturn.toList()
    }
}