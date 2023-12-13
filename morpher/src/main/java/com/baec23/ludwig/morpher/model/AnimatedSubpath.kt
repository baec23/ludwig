package com.baec23.ludwig.morpher.model

import androidx.compose.ui.graphics.vector.PathNode

interface AnimatedSubpath {
    fun getInterpolatedPathNodes(fraction:Float): List<PathNode>
}