package com.baec23.ludwig.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutExpo
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.baec23.ludwig.R
import com.baec23.ludwig.core.fadingLazy.FadingLazyVerticalGrid
import com.baec23.ludwig.morpher.component.AnimatedMorphVector
import com.baec23.ludwig.morpher.model.morpher.VectorSource
import kotlinx.coroutines.launch


@Composable
fun TestScreen2() {
    val androidVectorSource =
        VectorSource.fromImageVector(ImageVector.vectorResource(R.drawable.androidlogo))
    val appleVectorSource =
        VectorSource.fromImageVector(ImageVector.vectorResource(R.drawable.applelogo))
    val redditVectorSource =
        VectorSource.fromImageVector(ImageVector.vectorResource(R.drawable.redditlogo))
    val chromeVectorSource =
        VectorSource.fromImageVector(ImageVector.vectorResource(R.drawable.chrome))
    val firefoxVectorSource =
        VectorSource.fromImageVector(ImageVector.vectorResource(R.drawable.firefox))
    val instagramVectorSource =
        VectorSource.fromImageVector(ImageVector.vectorResource(R.drawable.instagram))
    val pinterestVectorSource =
        VectorSource.fromImageVector(ImageVector.vectorResource(R.drawable.pinterest))
    val snapchatVectorSource =
        VectorSource.fromImageVector(ImageVector.vectorResource(R.drawable.snapchat))
    val paypalVectorSource =
        VectorSource.fromImageVector(ImageVector.vectorResource(R.drawable.paypal))
    val youtubeVectorSource =
        VectorSource.fromImageVector(ImageVector.vectorResource(R.drawable.youtube))
    val skypeVectorSource =
        VectorSource.fromImageVector(ImageVector.vectorResource(R.drawable.skype))
    val slackVectorSource =
        VectorSource.fromImageVector(ImageVector.vectorResource(R.drawable.slack))
    val twitterVectorSource =
        VectorSource.fromImageVector(ImageVector.vectorResource(R.drawable.twitter))
    val flowerVectorSource =
        VectorSource.fromImageVector(ImageVector.vectorResource(R.drawable.flower))
    val refreshVectorSource = VectorSource.fromImageVector(Icons.Outlined.Refresh)
    val faceVectorSource = VectorSource.fromImageVector(Icons.Outlined.Face)
    val shoppingCartVectorSource = VectorSource.fromImageVector(Icons.Outlined.ShoppingCart)
    val createVectorSource = VectorSource.fromImageVector(Icons.Outlined.Create)
    val settingsVectorSource = VectorSource.fromImageVector(Icons.Default.Settings)
    val yinYangVectorSource = VectorSource.fromImageVector(ImageVector.vectorResource(R.drawable.yinyang))
    val cloverVectorSource = VectorSource.fromImageVector(ImageVector.vectorResource(R.drawable.clover))
    val worldLoveVectorSource = VectorSource.fromImageVector(ImageVector.vectorResource(R.drawable.worldlove))
    val fireVectorSource = VectorSource.fromImageVector(ImageVector.vectorResource(R.drawable.fire))
    val diamondVectorSource = VectorSource.fromImageVector(ImageVector.vectorResource(R.drawable.diamond))
    val helloBubbleVectorSource = VectorSource.fromImageVector(ImageVector.vectorResource(R.drawable.hellobubble))


    val targetVectors: List<VectorSource> = listOf(
        androidVectorSource,
        appleVectorSource,
        firefoxVectorSource,
        youtubeVectorSource,
        instagramVectorSource,
        paypalVectorSource,
        skypeVectorSource,
        slackVectorSource,
        pinterestVectorSource,
        snapchatVectorSource,
        twitterVectorSource,
        fireVectorSource,
        diamondVectorSource,
        flowerVectorSource,
        refreshVectorSource,
        faceVectorSource,
        shoppingCartVectorSource,
        createVectorSource,
        settingsVectorSource,
        cloverVectorSource,
        yinYangVectorSource,
        worldLoveVectorSource,
    )

    val animationProgress = remember {
        Animatable(0f)
    }

//    LaunchedEffect(Unit) {
//        animationProgress.animateTo(
//            1f,
//            animationSpec = infiniteRepeatable(
//                tween(
//                    durationMillis = 1000,
//                    easing = EaseInOutExpo
//                ), RepeatMode.Reverse
//            )
//        )
//    }

    var currStartSource by remember { mutableStateOf(androidVectorSource) }
    var currEndSource by remember { mutableStateOf(flowerVectorSource) }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedMorphVector(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(36.dp),
                startSource = currStartSource,
                endSource = currEndSource,
                progress = animationProgress.value,
                strokeWidth = 20f,
                strokeColor = Color.Black,
                animationSmoothness = 200
            )
        }
        FadingLazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 75.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(targetVectors) {
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .background(color = Color.LightGray)
                        .clickable(enabled = !animationProgress.isRunning) {
                            if(animationProgress.value == 0f){
                                currEndSource = it
                                coroutineScope.launch {
                                    animationProgress.animateTo(targetValue = 1f, animationSpec = tween(durationMillis = 1000, easing = EaseInOutExpo))
                                }
                            }else{
                                currStartSource = it
                                coroutineScope.launch {
                                    animationProgress.animateTo(targetValue = 0f, animationSpec = tween(durationMillis = 1000, easing = EaseInOutExpo))
                                }
                            }
                        }
                        .padding(12.dp)
                )
                {
                    VectorImage(
                        modifier = Modifier
                            .aspectRatio(1f),
                        source = it,
                        style = Stroke(width = 5f)
                    )
                }
            }
        }
    }
}