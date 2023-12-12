package com.baec23.ludwig.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutExpo
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.baec23.ludwig.morpher.component.AnimatedVector
import com.baec23.ludwig.morpher.model.VectorSource
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

    val bookmark =
        "M200-120v-640q0-33 23.5-56.5T280-840h400q33 0 56.5 23.5T760-760v640L480-240 200-120Zm80-122 200-86 200 86v-518H280v518Zm0-518h400-400Z"
    val testString1 =
        "M 100 100 L 150 100 C 200 100, 200 200, 150 200 L 100 200 C 50 200, 50 100, 100 100Z"

    val recommend =
        "M360-240h220q17 0 31.5-8.5T632-272l84-196q2-5 3-10t1-10v-32q0-17-11.5-28.5T680-560H496l24-136q2-10-1-19t-10-16l-29-29-184 200q-8 8-12 18t-4 22v200q0 33 23.5 56.5T360-240ZM480-80q-83 0-156-31.5T197-197q-54-54-85.5-127T80-480q0-83 31.5-156T197-763q54-54 127-85.5T480-880q83 0 156 31.5T763-763q54 54 85.5 127T880-480q0 83-31.5 156T763-197q-54 54-127 85.5T480-80Zm0-80q134 0 227-93t93-227q0-134-93-227t-227-93q-134 0-227 93t-93 227q0 134 93 227t227 93Zm0-320Z"
    val hotelClass =
        "m668-340 152-130 120 10-176 153 52 227-102-62-46-198Zm-94-292-42-98 46-110 92 217-96-9ZM294-247l126-76 126 77-33-144 111-96-146-13-58-136-58 135-146 13 111 97-33 143ZM173-80l65-281L20-550l288-25 112-265 112 265 288 25-218 189 65 281-247-149L173-80Zm247-340Z"
    val settingsHeart =
        "m482-320 140-140q17-17 22-41.5t-5-47.5q-10-23-30-37t-45-14q-25 0-45 15.5T482-552q-18-17-37.5-32.5T400-600q-25 0-45.5 13.5T324-550q-10 23-4.5 47.5T342-460l140 140ZM370-80l-16-128q-13-5-24.5-12T307-235l-119 50L78-375l103-78q-1-7-1-13.5v-27q0-6.5 1-13.5L78-585l110-190 119 50q11-8 23-15t24-12l16-128h220l16 128q13 5 24.5 12t22.5 15l119-50 110 190-103 78q1 7 1 13.5v27q0 6.5-2 13.5l103 78-110 190-118-50q-11 8-23 15t-24 12L590-80H370Zm70-80h79l14-106q31-8 57.5-23.5T639-327l99 41 39-68-86-65q5-14 7-29.5t2-31.5q0-16-2-31.5t-7-29.5l86-65-39-68-99 42q-22-23-48.5-38.5T533-694l-13-106h-79l-14 106q-31 8-57.5 23.5T321-633l-99-41-39 68 86 64q-5 15-7 30t-2 32q0 16 2 31t7 30l-86 65 39 68 99-42q22 23 48.5 38.5T427-266l13 106Zm40-320Z"
    val thumbsUpDown =
        "M80-400q-33 0-56.5-23.5T0-480v-240q0-12 5-23t13-19l198-198 30 30q6 6 10 15.5t4 18.5v8l-28 128h208q17 0 28.5 11.5T480-720v50q0 6-1 11.5t-3 10.5l-90 212q-7 17-22.5 26.5T330-400H80Zm238-80 82-194v-6H134l24-108-78 76v232h238ZM744 0l-30-30q-6-6-10-15.5T700-64v-8l28-128H520q-17 0-28.5-11.5T480-240v-50q0-6 1-11.5t3-10.5l90-212q8-17 23-26.5t33-9.5h250q33 0 56.5 23.5T960-480v240q0 12-4.5 22.5T942-198L744 0ZM642-480l-82 194v6h266l-24 108 78-76v-232H642Zm-562 0v-232 232Zm800 0v232-232Z"
    val partlyCloudy =
        "M440-760v-160h80v160h-80Zm266 110-56-56 113-114 56 57-113 113Zm54 210v-80h160v80H760Zm3 299L650-254l56-56 114 112-57 57ZM254-650 141-763l57-57 112 114-56 56Zm-14 450h180q25 0 42.5-17.5T480-260q0-25-17-42.5T421-320h-51l-20-48q-14-33-44-52.5T240-440q-50 0-85 35t-35 85q0 50 35 85t85 35Zm0 80q-83 0-141.5-58.5T40-320q0-83 58.5-141.5T240-520q60 0 109.5 32.5T423-400q58 0 97.5 43T560-254q-2 57-42.5 95.5T420-120H240Zm320-134q-5-20-10-39t-10-39q45-19 72.5-59t27.5-89q0-66-47-113t-113-47q-60 0-105 39t-53 99q-20-5-41-9t-41-9q14-88 82.5-144T480-720q100 0 170 70t70 170q0 77-44 138.5T560-254Zm-79-226Z"
    val solar =
        "m80-80 80-400h640l80 400H80Zm40-720v-80h120v80H120Zm58 640h262v-80H194l-16 80Zm67-427-57-56 85-85 57 56-85 85Zm-35 267h230v-80H226l-16 80Zm270-360q-83 0-141.5-58.5T280-880h80q0 50 35 85t85 35q50 0 85-35t35-85h80q0 83-58.5 141.5T480-680Zm0-200Zm-40 360v-120h80v120h-80Zm80 360h262l-16-80H520v80Zm0-160h230l-16-80H520v80Zm195-267-84-85 56-56 85 84-57 57Zm5-213v-80h120v80H720Z"
    val face3 =
        "M480-240q134 0 227-93.5T800-560q0-31-5-59.5T779-675q-27 17-57 26t-62 9q-54 0-101.5-24.5T480-734q-31 45-78.5 69.5T300-640q-32 0-62-9t-57-26q-11 27-16 55.5t-5 59.5q0 133 93.5 226.5T480-240ZM360-470q21 0 35.5-14.5T410-520q0-21-14.5-35.5T360-570q-21 0-35.5 14.5T310-520q0 21 14.5 35.5T360-470Zm240 0q21 0 35.5-14.5T650-520q0-21-14.5-35.5T600-570q-21 0-35.5 14.5T550-520q0 21 14.5 35.5T600-470ZM300-720q58 0 99-41t41-99v-18q-68 8-125 43t-95 89q18 12 38 19t42 7Zm360 0q22 0 42-6.5t38-19.5q-38-54-94.5-89T520-878v18q0 58 41 99t99 41ZM88-80q-35 0-59-26T8-167l36-395q8-84 45.5-157t96-126.5q58.5-53.5 134-84T480-960q85 0 160.5 30.5t134 84Q833-792 870.5-719T916-562l36 395q3 35-21 61t-59 26H88Zm392-80q-125 0-225-69.5T110-408L88-160h784l-22-248q-45 109-144.5 178.5T480-160Zm40-718Zm-80 0Zm40 718h392H88h392Z"
    val testHello = "M10 40 C 30 20, 50 60, 70 40 S 110 20, 130 40 S 150 60, 170 40"

    val animationProgress = remember {
        Animatable(0f)
    }
    var sliderPosition by remember { mutableStateOf(0f) }


    LaunchedEffect(Unit) {
        animationProgress.animateTo(
            1f,
            animationSpec = infiniteRepeatable(
                tween(
                    durationMillis = 1000,
                    easing = EaseInOutExpo
                ), RepeatMode.Reverse
            )
        )
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVector(
                modifier = Modifier
                    .size(200.dp)
                    .padding(12.dp),
                startSource = VectorSource(Icons.Outlined.CheckCircle),
                endSource = VectorSource(Icons.Outlined.Settings),
                progress = animationProgress.value,
                strokeWidth = 20f,
                strokeColor = Color.Black
            )

        }
        Slider(
            modifier = Modifier.padding(horizontal = 24.dp),
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
//            valueRange = 0f..startPathSegments.size.toFloat() - 1
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

