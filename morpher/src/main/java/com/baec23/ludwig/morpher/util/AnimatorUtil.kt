package com.baec23.ludwig.morpher.util

import kotlin.math.roundToInt

fun getClampedIndex(fraction: Float, maxIndex: Int): Int {
    var toReturn = (maxIndex * fraction).roundToInt()
    if (toReturn > maxIndex) {
        val extra = toReturn % maxIndex
        toReturn = maxIndex - extra
    } else if (toReturn < 0) {
        val extra = toReturn % maxIndex
        toReturn = -extra
    }
    return toReturn
}