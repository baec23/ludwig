package com.baec23.ludwig.morpher.model.morpher

import androidx.compose.ui.graphics.Path
import com.baec23.ludwig.morpher.util.getClampedIndex

data class MorpherAnimationData(
    val pairedPaths: Array<Path?>,
    val unpairedStartPaths: Array<Path?>,
    val unpairedEndPaths: Array<Path?>,

) {
    private val smoothness = pairedPaths.size - 1

    fun getInterpolatedPairedPath(fraction: Float): Path? {
        val index = getClampedIndex(fraction, smoothness)
        return pairedPaths[index]
    }

    fun getInterpolatedUnpairedStartPath(fraction: Float): Path? {
        val index = getClampedIndex(fraction, smoothness)
        return unpairedStartPaths[index]
    }

    fun getInterpolatedUnpairedEndPath(fraction: Float): Path? {
        val index = getClampedIndex(fraction, smoothness)
        return unpairedEndPaths[index]
    }

    fun setInterpolatedPairedPath(fraction: Float, path: Path) {
        val index = getClampedIndex(fraction, smoothness)
        pairedPaths[index] = path
    }

    fun setInterpolatedUnpairedStartPath(fraction: Float, path: Path) {
        val index = getClampedIndex(fraction, smoothness)
        unpairedStartPaths[index] = path
    }

    fun setInterpolatedEndPath(fraction: Float, path: Path) {
        val index = getClampedIndex(fraction, smoothness)
        unpairedEndPaths[index] = path
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MorpherAnimationData

        if (!pairedPaths.contentEquals(other.pairedPaths)) return false
        if (!unpairedStartPaths.contentEquals(other.unpairedStartPaths)) return false
        if (!unpairedEndPaths.contentEquals(other.unpairedEndPaths)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = pairedPaths.contentHashCode()
        result = 31 * result + unpairedStartPaths.contentHashCode()
        result = 31 * result + unpairedEndPaths.contentHashCode()
        return result
    }
}
