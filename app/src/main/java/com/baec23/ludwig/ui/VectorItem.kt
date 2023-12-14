package com.baec23.ludwig.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.toPath
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.baec23.ludwig.morpher.model.morpher.VectorSource


@Composable
fun VectorItem(modifier: Modifier = Modifier, vectorSource: VectorSource) {
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    val pathData =
        vectorSource.getNormalizedPathData(canvasSize.width.toFloat(), canvasSize.height.toFloat())
    val path = pathData.toPath()
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(Color.LightGray)
            .padding(24.dp),
        contentAlignment = Alignment.Center

    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned { coordinates ->
                    canvasSize = coordinates.size
                },
        ) {
            drawPath(
                path,
                color = Color.Black,
                style = Stroke(
                    width = 10f,
                    join = StrokeJoin.Round,
                    cap = StrokeCap.Round
                )
            )
        }
    }
}
