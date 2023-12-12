package com.baec23.ludwig.morpher.model

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.graphics.vector.VectorPath

class VectorSource() {
    var pathData: List<PathNode> = listOf()
    var bounds: Rect = Rect.Zero

    constructor(pathString: String) : this() {
        val pathParser = PathParser().parsePathString(pathString)
        bounds = pathParser.toPath().getBounds()
        pathData = pathParser.toNodes()
    }

    constructor(imageVector: ImageVector) : this() {
        pathData = imageVector.root.filterIsInstance<VectorPath>().flatMap { it.pathData }
        val pathParser = PathParser()
        pathParser.addPathNodes(pathData)
        bounds = pathParser.toPath().getBounds()

    }

    constructor(vectorPath: VectorPath) : this() {
        pathData = vectorPath.pathData
        val pathParser = PathParser()
        pathParser.addPathNodes(pathData)
        bounds = pathParser.toPath().getBounds()
    }
}
