package com.baec23.ludwig

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.baec23.ludwig.ui.AnimatedTextTestScreen
import com.baec23.ludwig.ui.TestScreen
import com.baec23.ludwig.ui.theme.LudwigTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LudwigTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AnimatedTextTestScreen()
//                    TestScreen()
                }
            }
        }
    }
}
