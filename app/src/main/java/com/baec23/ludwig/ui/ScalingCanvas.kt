package com.baec23.ludwig.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalDensity

@Composable
fun PathImage(
    modifier: Modifier = Modifier,
    path: Path,
    strokeWidth: Float = 4f,
    strokeColor: Color = Color.Black
) {
    BoxWithConstraints(modifier) {
        val pathBounds = path.getBounds()

        val pathWidth = pathBounds.width
        val pathHeight = pathBounds.height
        path.translate(Offset(-pathBounds.left, -pathBounds.top))

        val mWidth =
            pathWidth.coerceIn(constraints.minWidth.toFloat(), constraints.maxWidth.toFloat())
        val mHeight =
            pathHeight.coerceIn(constraints.minHeight.toFloat(), constraints.maxHeight.toFloat())

        val mWidthDp = with(LocalDensity.current) { mWidth.toDp() }
        val mHeightDp = with(LocalDensity.current) { mHeight.toDp() }

        val scaleFactorX = mWidth / pathWidth
        val scaleFactorY = mHeight / pathHeight
        val minScaleFactor = minOf(scaleFactorX, scaleFactorY)

        val dx = (mWidth - pathWidth * minScaleFactor) / 2f
        val dy = (mHeight - pathHeight * minScaleFactor) / 2f
        Canvas(
            modifier = Modifier
                .size(width = mWidthDp, height = mHeightDp)
        ) {
            withTransform({
                translate(left = dx, top = dy)
                scale(scale = minScaleFactor, pivot = Offset.Zero)
            }) {

                drawPath(
                    path,
                    color = strokeColor,
                    style = Stroke(
                        width = strokeWidth / minScaleFactor,
                        join = StrokeJoin.Round,
                        cap = StrokeCap.Round
                    )
                )
            }
        }
    }
}