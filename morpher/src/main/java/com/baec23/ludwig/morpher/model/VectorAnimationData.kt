package com.baec23.ludwig.morpher.model

import androidx.compose.ui.graphics.Path

data class VectorAnimationData(
    val pairedSubpaths: List<PairedSubpath>,
    val unpairedStartSubpaths: List<UnpairedSubpath>,
    val unpairedEndSubpaths: List<UnpairedSubpath>,
    val pairedInterpolatedPaths: Array<Path>,
    val unpairedStartInterpolatedPaths: Array<Path>,
    val unpairedEndInterpolatedPaths: Array<Path>,
    val width: Float,
    val height: Float
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VectorAnimationData

        if (pairedSubpaths != other.pairedSubpaths) return false
        if (unpairedStartSubpaths != other.unpairedStartSubpaths) return false
        if (unpairedEndSubpaths != other.unpairedEndSubpaths) return false
        if (!pairedInterpolatedPaths.contentEquals(other.pairedInterpolatedPaths)) return false
        if (!unpairedStartInterpolatedPaths.contentEquals(other.unpairedStartInterpolatedPaths)) return false
        if (!unpairedEndInterpolatedPaths.contentEquals(other.unpairedEndInterpolatedPaths)) return false
        if (width != other.width) return false
        if (height != other.height) return false

        return true
    }

    override fun hashCode(): Int {
        var result = pairedSubpaths.hashCode()
        result = 31 * result + unpairedStartSubpaths.hashCode()
        result = 31 * result + unpairedEndSubpaths.hashCode()
        result = 31 * result + pairedInterpolatedPaths.contentHashCode()
        result = 31 * result + unpairedStartInterpolatedPaths.contentHashCode()
        result = 31 * result + unpairedEndInterpolatedPaths.contentHashCode()
        result = 31 * result + width.hashCode()
        result = 31 * result + height.hashCode()
        return result
    }
}
