package com.baec23.ludwig.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.baec23.ludwig.R
import com.baec23.ludwig.morpher.component.AnimatedVector
import com.baec23.ludwig.morpher.model.morpher.VectorSource

@Composable
fun MorpherSample() {
    val vectorSource1 = VectorSource.fromImageVector(Icons.Outlined.Star)
    val vectorSource2 =
        VectorSource.fromImageVector(ImageVector.vectorResource(R.drawable.android))
//    val vectorSource2 =
//        VectorSource.fromPathString("m256-200-56-56 224-224-224-224 56-56 224 224 224-224 56 56-224 224 224 224-56 56-224-224-224 224Z")
    var selectedVectorSource by remember { mutableStateOf(vectorSource1) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        AnimatedVector(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            vectorSource = selectedVectorSource,
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = { selectedVectorSource = vectorSource1 }) {
                Text(text = "Source 1")
            }
            Button(
                modifier = Modifier.weight(1f),
                onClick = { selectedVectorSource = vectorSource2 }) {
                Text(text = "Source 2")
            }
        }
    }
}