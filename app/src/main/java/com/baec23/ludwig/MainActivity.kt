package com.baec23.ludwig

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baec23.ludwig.component.section.DisplaySection
import com.baec23.ludwig.component.timepicker.TimePicker
import com.baec23.ludwig.component.timepicker.TimePickerType
import com.baec23.ludwig.ui.theme.LudwigTheme
import java.time.LocalTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LudwigTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(state = rememberScrollState())
                    ) {
                        DisplaySection(headerText = "HH:MM:SS 12 Hour Spinner") {
                            var time by remember { mutableStateOf(LocalTime.now()) }
                            Text("Selected Time = $time")
                            TimePicker(
                                initialTime = LocalTime.now().minusHours(1),
                                type = TimePickerType.HoursMinutesSeconds12Hour,
                                selectedTextColor = MaterialTheme.colorScheme.primary
                            ) {
                                time = it
                            }
                        }
                        DisplaySection(headerText = "HH:MM 12 Hour Spinner") {
                            var time by remember { mutableStateOf(LocalTime.now()) }
                            Text("Selected Time = $time")
                            TimePicker(initialTime = LocalTime.now().minusHours(1), type = TimePickerType.HoursMinutes12Hour) {
                                time = it
                            }
                        }
                        DisplaySection(headerText = "HH:MM:SS 24 Hour Spinner") {
                            var time by remember { mutableStateOf(LocalTime.now()) }
                            Text("Selected Time = $time")
                            TimePicker(initialTime = LocalTime.now().minusHours(1), type = TimePickerType.HoursMinutesSeconds24Hour) {
                                time = it
                            }
                        }
                        DisplaySection(headerText = "HH:MM 24 Hour Spinner") {
                            var time by remember { mutableStateOf(LocalTime.now()) }
                            Text("Selected Time = $time")
                            TimePicker(initialTime = LocalTime.now().minusHours(1), type = TimePickerType.HoursMinutes24Hour) {
                                time = it
                            }
                        }
                    }
                }
            }
        }
    }
}
