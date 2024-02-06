package com.baec23.ludwig.morpher.component.text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.baec23.ludwig.morpher.component.AnimatedVector
import com.baec23.ludwig.morpher.model.morpher.VectorSource

@Composable
fun AnimatedChar(
    modifier: Modifier = Modifier,
    char: Char,
    enterAnimation: CharAnimation = CharAnimation.Default,
    exitAnimation: CharAnimation = CharAnimation.Default
) {
    val vectorSource by remember(key1 = char) {
        mutableStateOf<VectorSource>(
            VectorSource.fromText(
                char.toString()
            )
        )
    }
    AnimatedVector(modifier = modifier, vectorSource = vectorSource, strokeWidth = 80f)
}

//hide
//show

sealed class CharAnimation {
    data object Default : CharAnimation()

}