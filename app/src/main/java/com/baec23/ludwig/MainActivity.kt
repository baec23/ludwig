package com.baec23.ludwig

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.baec23.ludwig.ui.MorpherSample
import com.baec23.ludwig.ui.TestScreen2
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
                    MorpherSample()
//                    TestScreen2()
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp)
//                            .verticalScroll(state = rememberScrollState())
//                    ) {
//                        var input1 by remember { mutableStateOf("") }
//                        var input2 by remember { mutableStateOf("") }
//                        var isExpanded by remember { mutableStateOf(false) }
//                        var isExpanded2 by remember { mutableStateOf(false) }
//                        ExpandableDisplaySection(
//                            isExpanded = isExpanded2,
//                            onExpand = { isExpanded2 = !isExpanded2 },
//                            headerText = "Morpher Test"
//                        ) {
//                            Morpher()
//                        }
//                        ExpandableDisplaySection(
//                            isExpanded = isExpanded,
//                            onExpand = { isExpanded = !isExpanded },
//                            headerText = "Expandable Display Section",
//                            headerIcon = Icons.Default.AccountBox,
//                            headerSubtext = "Hello this is some subtext"
//                        ) {
//                            InputField(
//                                value = input1,
//                                onValueChange = { input1 = it },
//                                label = "Username"
//                            )
//                            PasswordInputField(
//                                value = input2,
//                                onValueChange = { input2 = it },
//                                label = "Password",
//                                placeholder = "Password can only contain letters, numbers, and symbols"
//                            )
//                            StatefulButton(text = "Hey") {
//                                Toast.makeText(
//                                    applicationContext,
//                                    "Password is : $input2",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//
//                        }
//                        DisplaySection(
//                            headerText = "Date Picker / Time Spinner",
//                            headerIcon = Icons.Default.Place,
//                            headerSubtext = "This is a section to test date picker and time spinner"
//                        ) {
//                            DatePicker(onCancelled = { }) { }
//                            TimePicker(onTimeChanged = {})
//                        }
//                    }
                }
            }
        }
    }
}
