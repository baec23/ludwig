package com.baec23.ludwig.ui

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.baec23.ludwig.component.morph.AnimatedPath
import com.baec23.ludwig.component.morph.TAG
import kotlin.random.Random


@Composable
fun TestScreen2() {
    val arrowForwardString =
        "M647-440H160v-80h487L423-744l57-56 320 320-320 320-57-56 224-224Z"
    val checkPathString = "M382-240 154-468l57-57 171 171 367-367 57 57-424 424Z"
    val favoritePathString =
        "m480-120-58-52q-101-91-167-157T150-447.5Q111-500 95.5-544T80-634q0-94 63-157t157-63q52 0 99 22t81 62q34-40 81-62t99-22q94 0 157 63t63 157q0 46-15.5 90T810-447.5Q771-395 705-329T538-172l-58 52Zm0-108q96-86 158-147.5t98-107q36-45.5 50-81t14-70.5q0-60-40-100t-100-40q-47 0-87 26.5T518-680h-76q-15-41-55-67.5T300-774q-60 0-100 40t-40 100q0 35 14 70.5t50 81q36 45.5 98 107T480-228Zm0-273Z"
    val testPathString =
        "M10,10 L50,60 A10,20 45 0,1 80,70 Q100,90 120,100 T140,110 L160,120 A5,10 30 0,0 180,130 Q200,150 220,160 T240,170 L260,180 A15,25 60 1,1 280,190 Q300,210 320,220 T340,230 Z"
    val addString = "M440-440H200v-80h240v-240h80v240h240v80H520v240h-80v-240Z"
    val closeString =
        "m256-200-56-56 224-224-224-224 56-56 224 224 224-224 56 56-224 224 224 224-56 56-224-224-224 224Z"

    val animatedPath = remember { AnimatedPath(checkPathString, addString) }
    val animationProgress = remember {
        Animatable(0f)
    }

//    LaunchedEffect(Unit) {
//        animationProgress.animateTo(
//            1f,
//            animationSpec = infiniteRepeatable(
//                tween(
//                    durationMillis = 2000,
//                    easing = EaseInOutElastic
//                ), RepeatMode.Reverse
//            )
//        )
//    }

    /*

    UI Stuff

     */

    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    var sliderPosition by remember { mutableStateOf(0f) }

//    val scaleFactor = canvasSize.width / animatedPath.bounds.width
//    Log.d(
//        TAG,
//        "canvasSize = ${canvasSize} | bounds = ${animatedPath.bounds} | scaleFactor = ${scaleFactor}"
//    )


    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Canvas(modifier = Modifier
                .aspectRatio(1f)
                .background(Color.LightGray)
                .padding(24.dp)
                .onGloballyPositioned { coordinates ->
                    canvasSize = coordinates.size
                },
                onDraw = {
                    Log.d(TAG, "TestScreen2: ${size}")
                    val scaleFactor =  size.width / animatedPath.bounds.width
                    Log.d(TAG, "TestScreen2: ${animatedPath.bounds}")
                    withTransform({
                        scale(scaleFactor)
                        translate(animatedPath.bounds.left, (-animatedPath.bounds.top) * scaleFactor)
                    }){
                        val interpolatedPath =
                            animatedPath.getInterpolatedPath(sliderPosition)
//                    val bounds = interpolatedPath.getBounds()
//                    val interpolatedScaledPath =
//                        interpolatedPath.toScaledPath(bounds, canvasSize.width, canvasSize.height)
                        drawPath(
                            interpolatedPath,
                            color = Color.Black,
                            style = Stroke(width = 20f)
                        )
                        val startPoints = animatedPath.startPoints
                        drawPoints(
                            points = startPoints,
                            color = Color.Green,
                            pointMode = PointMode.Points,
                            strokeWidth = 20f
                        )
                        val endPoints = animatedPath.endPoints
                        drawPoints(
                            points = endPoints,
                            color = Color.Red,
                            pointMode = PointMode.Points,
                            strokeWidth = 20f
                        )

                    }

                })

        }
        Slider(
            modifier = Modifier.padding(horizontal = 24.dp),
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
        )
    }
}


fun generateRandomColors(count: Int): List<Color> {
    return List(count) {
        // Generate random RGB values
        val red = Random.nextInt(256)
        val green = Random.nextInt(256)
        val blue = Random.nextInt(256)

        // Combine them to create a color integer
        Color(red, green, blue)
    }
}

