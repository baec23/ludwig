package com.baec23.ludwig.morpher.model.path

import androidx.compose.ui.graphics.vector.PathNode

internal interface AnimatedSubpath {
    fun getInterpolatedPathNodes(fraction:Float): List<PathNode>
}