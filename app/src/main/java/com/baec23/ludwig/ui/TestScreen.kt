package com.baec23.ludwig.ui

import androidx.compose.animation.core.EaseInOutExpo
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.baec23.ludwig.R
import com.baec23.ludwig.component.section.DisplaySection
import com.baec23.ludwig.component.fadinglazy.FadingLazyVerticalGrid
import com.baec23.ludwig.morpher.component.AnimatedVector
import com.baec23.ludwig.morpher.component.VectorImage
import com.baec23.ludwig.morpher.model.morpher.VectorSource


@Composable
fun TestScreen() {
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
    val yinYangVectorSource =
        VectorSource.fromImageVector(ImageVector.vectorResource(R.drawable.yinyang))
    val cloverVectorSource =
        VectorSource.fromImageVector(ImageVector.vectorResource(R.drawable.clover))
    val worldLoveVectorSource =
        VectorSource.fromImageVector(ImageVector.vectorResource(R.drawable.worldlove))
    val fireVectorSource = VectorSource.fromImageVector(ImageVector.vectorResource(R.drawable.fire))
    val diamondVectorSource =
        VectorSource.fromImageVector(ImageVector.vectorResource(R.drawable.diamond))
    val helloBubbleVectorSource =
        VectorSource.fromImageVector(ImageVector.vectorResource(R.drawable.hellobubble))

    val addVectorSource =
        VectorSource.fromImageVector(Icons.Outlined.Add)
    val closeVectorSource =
        VectorSource.fromImageVector(Icons.Outlined.Close)
    val checkVectorSource =
        VectorSource.fromImageVector(Icons.Outlined.Check)
    val checkCircleVectorSource =
        VectorSource.fromImageVector(Icons.Outlined.CheckCircle)

    val testStringVectorSource = VectorSource.fromText("H")
    val testStringVectorSource2 = VectorSource.fromText("E")
    val testStringVectorSource3 = VectorSource.fromText("L")
    val testStringVectorSource4 = VectorSource.fromText("L")
    val testStringVectorSource5 = VectorSource.fromText("O")

    val targetVectors: List<VectorSource> = listOf(
//        redditVectorSource,
//        androidVectorSource,
//        chromeVectorSource,
//        firefoxVectorSource,
//        skypeVectorSource,
//        refreshVectorSource,
//        shoppingCartVectorSource,
//        createVectorSource,
//        fireVectorSource,
//        diamondVectorSource,
//        helloBubbleVectorSource,
//        paypalVectorSource,
//        pinterestVectorSource,
//        appleVectorSource,
//        slackVectorSource,
//        youtubeVectorSource,
//        instagramVectorSource,
//        snapchatVectorSource,
//        twitterVectorSource,
//        flowerVectorSource,
//        faceVectorSource,
//        yinYangVectorSource,
//        settingsVectorSource,
//        cloverVectorSource,
//        worldLoveVectorSource,
//        addVectorSource,
//        closeVectorSource,
//        checkVectorSource,
//        checkCircleVectorSource,
        testStringVectorSource,
        testStringVectorSource2,
        testStringVectorSource3,
        testStringVectorSource4,
        testStringVectorSource5,
    )

    var currSelectedSource by remember { mutableStateOf(appleVectorSource) }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVector(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(36.dp),
                vectorSource = currSelectedSource,
                strokeWidth = 30f,
//                animationSpec = tween(durationMillis = 1000, easing = EaseInOutExpo)
                animationSpec = tween(durationMillis = 800, easing = EaseInOutExpo)
            )
        }
        DisplaySection(
            modifier = Modifier.padding(horizontal = 8.dp),
            headerText = "Controls",
            contentPadding = PaddingValues(16.dp),
            headerIcon = Icons.Default.Star
        ) {
            FadingLazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 100.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                gradientHeightPercent = 0.2f
            ) {
                items(targetVectors) {
                    Box(modifier = Modifier
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .alpha(if (currSelectedSource == it) 1f else 0.5f)
                        .border(
                            width = if (currSelectedSource == it) 6.dp else 2.dp,
                            color = Color.Black,
                            shape = CircleShape
                        )
                        .clickable() {
                            currSelectedSource = it
                        }
                        .padding(24.dp)) {
                        VectorImage(
                            modifier = Modifier.aspectRatio(1f),
                            source = it,
                            style = Stroke(width = 5f)
                        )
                    }
                }
            }
        }
    }
}