package com.baec23.ludwig.morpher.model.morpher

import com.baec23.ludwig.morpher.model.path.PairedSubpath
import com.baec23.ludwig.morpher.model.path.UnpairedSubpath

data class MorpherPathData(
    val pairedSubpaths: List<PairedSubpath>,
    val unpairedStartSubpaths: List<UnpairedSubpath>,
    val unpairedEndSubpaths: List<UnpairedSubpath>,
)
