package com.baec23.ludwig.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.baec23.ludwig.component.morph.DrawSegment
import com.baec23.ludwig.component.morph.SvgViewBox
import com.baec23.ludwig.component.morph.convertToAbsoluteCommands
import com.baec23.ludwig.component.morph.splitIntoClosedPaths
import com.baec23.ludwig.component.morph.splitIntoPaths
import com.baec23.ludwig.component.morph.toCustomString
import com.baec23.ludwig.component.morph.toDrawSegments
import com.baec23.ludwig.component.morph.toScaledPath
import com.baec23.ludwig.component.section.DisplaySection
import com.baec23.ludwig.component.section.ExpandableDisplaySection

@Composable
fun PathTestScreen() {
    val viewBox = SvgViewBox(0, -960, 960, 960)

    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    val arrowForwardString =
        "M647-440H160v-80h487L423-744l57-56 320 320-320 320-57-56 224-224Z"
    val checkPathString = "M382-240 154-468l57-57 171 171 367-367 57 57-424 424Z"
    val favoritePathString =
        "m480-120-58-52q-101-91-167-157T150-447.5Q111-500 95.5-544T80-634q0-94 63-157t157-63q52 0 99 22t81 62q34-40 81-62t99-22q94 0 157 63t63 157q0 46-15.5 90T810-447.5Q771-395 705-329T538-172l-58 52Zm0-108q96-86 158-147.5t98-107q36-45.5 50-81t14-70.5q0-60-40-100t-100-40q-47 0-87 26.5T518-680h-76q-15-41-55-67.5T300-774q-60 0-100 40t-40 100q0 35 14 70.5t50 81q36 45.5 98 107T480-228Zm0-273Z"
    val testPathString =
        "M10,10 L50,60 A10,20 45 0,1 80,70 Q100,90 120,100 T140,110 L160,120 A5,10 30 0,0 180,130 Q200,150 220,160 T240,170 L260,180 A15,25 60 1,1 280,190 Q300,210 320,220 T340,230 Z"
    val addString = "M440-440H200v-80h240v-240h80v240h240v80H520v240h-80v-240Z"
    val closeString = "m256-200-56-56 224-224-224-224 56-56 224 224 224-224 56 56-224 224 224 224-56 56-224-224-224 224Z"

    val pathString = addString

    val path = PathParser().parsePathString(pathString)
    val pathDataNodes = androidx.core.graphics.PathParser.createNodesFromPathData(pathString)
    val pathNodes = path.toNodes().convertToAbsoluteCommands().first
    val drawSegments = path.toNodes().toDrawSegments()
    val bounds = path.toPath().getBounds()
    val aspectRatio = bounds.width / bounds.height
    val splitPaths = pathNodes.splitIntoPaths()
    val scaledSplitPaths = if (canvasSize.width > 0) {
        splitPaths.map { splitPath ->
            splitPath.toScaledPath(
                bounds,
                canvasSize.width,
                canvasSize.height
            )
        }
    } else {
        listOf()
    }
    val splitPathNodes = pathNodes.splitIntoClosedPaths()
    var sliderPosition by remember { mutableStateOf(0f) }

    val animationProgress = remember {
        Animatable(0f)
    }

    LaunchedEffect(Unit) {
        animationProgress.animateTo(
            1f,
            animationSpec = infiniteRepeatable(
                tween(
                    durationMillis = 2000,
//                    easing = EaseInOutExpo
                ), RepeatMode.Reverse
            )
        )
    }



    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Canvas(modifier = Modifier
                .background(Color.LightGray)
                .padding(24.dp)
                .aspectRatio(aspectRatio)
                .onGloballyPositioned { coordinates ->
                    canvasSize = coordinates.size
//                    Log.d(
//                        "what", """
//                        scaleFactor: ${scaleFactor}
//                        canvasWidth: ${canvasSize.width}
//                        canvasHeight: ${canvasSize.height}
//                        viewboxWidth: ${viewBox.width}
//                        viewboxHeight: ${viewBox.height}
//                        """.trimIndent()
//                    )
                },
                onDraw = {
//                    drawPath(
//                        pathToDraw,
//                        color = Color.Black,
//                        style = Stroke(width = 10f, cap = StrokeCap.Round)
//                    )

                    scaledSplitPaths.forEachIndexed { index, subpath ->

                        if (index < sliderPosition) {
                            val pathMeasure = PathMeasure()
                            pathMeasure.setPath(subpath, false)
                            val pathToDraw = Path()
                            pathMeasure.getSegment(
                                0f,
                                animationProgress.value * pathMeasure.length,
                                pathToDraw,
                                startWithMoveTo = true
                            )

                            drawPath(pathToDraw, color = Color.Black, style = Stroke(width = 10f))
                        }
                    }
                })

        }
        Slider(
            modifier = Modifier.padding(horizontal = 24.dp),
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            valueRange = 0f..scaledSplitPaths.size.toFloat(),
        )
        DrawSegmentsSection(modifier = Modifier.weight(1f), drawSegments = drawSegments)
    }
}


@Composable
fun DrawSegmentsSection(
    drawSegments: List<DrawSegment>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        drawSegments.forEachIndexed { index, drawSegment ->
            var isExpanded by remember { mutableStateOf(false) }

            ExpandableDisplaySection(
                isExpanded = isExpanded,
                onExpand = { isExpanded = !isExpanded },
                headerText = "Segment $index"
            ) {
                DisplaySection(headerText = "Start") {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("x = ${drawSegment.start.x}")
                        Text("y = ${drawSegment.start.y}")
                    }
                }
                DisplaySection(headerText = "End") {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("x = ${drawSegment.end.x}")
                        Text("y = ${drawSegment.end.y}")
                    }
                }
                DisplaySection(headerText = "Distance") {
                    Text("distance = ${drawSegment.distance}")
                }
                DisplaySection(headerText = "Path") {
                    Text(drawSegment.pathNodes.toCustomString())
                }
            }
        }
    }
}

@Composable
fun PathNodesSection(
    splitPathNodes: List<List<PathNode>>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        splitPathNodes.forEachIndexed { index, sublist ->
            var isExpanded by remember { mutableStateOf(false) }
            Card(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    isExpanded = !isExpanded
                }) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text("Sublist $index")
                    if (isExpanded) {
                        Text(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            text = sublist.toCustomString()
                        )
                    }
                }
            }
        }
    }
}