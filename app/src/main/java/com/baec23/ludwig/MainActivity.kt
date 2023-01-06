package com.baec23.ludwig

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.baec23.ludwig.component.section.DisplaySection
import com.baec23.ludwig.core.togglable.ToggleableIcon
import com.baec23.ludwig.ui.theme.LudwigTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LudwigTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    color = MaterialTheme.colorScheme.background
                ) {

                    var selectedIconIndex by remember { mutableStateOf(0) }

                    DisplaySection(headerText = "ToggleableIcon test") {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
//                                .height(50.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            ToggleableIcon(
                                isToggled = selectedIconIndex == 0,
                                imageVector = Icons.Default.AccountBox,
                                label = "Accountasdfsadfsdfasdfsadf",
                                iconSize = DpSize(100.dp, 100.dp)
                            ) {
                                selectedIconIndex = 0
                            }
                            ToggleableIcon(
                                isToggled = selectedIconIndex == 1,
                                imageVector = Icons.Default.Home,
                                label = "Home"
                            ) {
                                selectedIconIndex = 1
                            }
                            ToggleableIcon(
                                isToggled = selectedIconIndex == 2,
                                imageVector = Icons.Default.ShoppingCart,
                                label = "Cart"
                            ) {
                                selectedIconIndex = 2
                            }
                        }
                    }
                }
            }
        }
    }
}