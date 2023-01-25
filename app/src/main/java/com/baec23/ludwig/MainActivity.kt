package com.baec23.ludwig

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baec23.ludwig.component.button.StatefulButton
import com.baec23.ludwig.component.datepicker.DatePicker
import com.baec23.ludwig.component.inputfield.InputField
import com.baec23.ludwig.component.inputfield.PasswordInputField
import com.baec23.ludwig.component.section.DisplaySection
import com.baec23.ludwig.component.section.ExpandableDisplaySection
import com.baec23.ludwig.component.timepicker.TimePicker
import com.baec23.ludwig.ui.theme.LudwigTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LudwigTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .verticalScroll(state = rememberScrollState())
                    ) {
                        var input1 by remember { mutableStateOf("") }
                        var input2 by remember { mutableStateOf("") }
                        var isExpanded by remember { mutableStateOf(false) }
                        ExpandableDisplaySection(
                            isExpanded = isExpanded,
                            onExpand = { isExpanded = !isExpanded },
                            headerText = "Expandable Display Section",
                            headerIcon = Icons.Default.AccountBox,
                            headerSubtext = "Hello this is some subtext"
                        ) {
                            InputField(
                                value = input1,
                                onValueChange = { input1 = it },
                                label = "Username"
                            )
                            PasswordInputField(
                                value = input2,
                                onValueChange = { input2 = it },
                                label = "Password",
                                placeholder = "Password can only contain letters, numbers, and symbols"
                            )
                            StatefulButton(text = "Hey") {
                                Toast.makeText(
                                    applicationContext,
                                    "Password is : $input2",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }
                        DisplaySection(headerText = "Date Picker / Time Spinner", headerIcon = Icons.Default.Place, headerSubtext = "This is a section to test date picker and time spinner") {
                            DatePicker(onCancelled = { }) { }
                            TimePicker(onTimeChanged = {})
                        }
                    }
                }
            }
        }
    }
}
