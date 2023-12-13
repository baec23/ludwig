package com.baec23.ludwig.morpher.model

data class MorpherPathData(
    val pairedSubpaths: List<PairedSubpath>,
    val unpairedStartSubpaths: List<UnpairedSubpath>,
    val unpairedEndSubpaths: List<UnpairedSubpath>,
)
