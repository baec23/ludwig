package com.baec23.ludwig.component.morph

import android.graphics.Path
import android.graphics.PathMeasure
import androidx.compose.ui.graphics.asComposePath
import androidx.core.graphics.PathParser

class PathSegment(
    val path: Path,
) {
    var length: Float
    private var pathMeasurer: PathMeasure = PathMeasure()

    /**
     * Splits PathSegment into subsegments
     *
     * @param splitPositions array of positions (from 0f to 1f) of where to split the segment.
     * For example, to split the segment in half: [0.5f].
     * To split the segment into thirds: [0.33f, 0.66f].
     * @return Array of PathSegment with size equal to splitPositions.size + 1
     */
    fun splitSegment(splitPositions: FloatArray): Array<PathSegment> {
        if (splitPositions.isEmpty()) {
            return arrayOf(this)
        }
        val toReturn = Array<PathSegment>(splitPositions.size + 1) { PathSegment(Path()) }
        val splitDistances = splitPositions.map { position -> this.length * position }
        var currStartDistance = 0f
        for (i in splitDistances.indices) {
            val subPath = Path()
            pathMeasurer.getSegment(currStartDistance, splitDistances[i], subPath, true)
            toReturn[i] = PathSegment(subPath)
            currStartDistance = splitDistances[i]
        }
        return toReturn
    }

    fun hmm(){

    }
    init {
        pathMeasurer.setPath(path, false)
        length = pathMeasurer.length
    }

}
