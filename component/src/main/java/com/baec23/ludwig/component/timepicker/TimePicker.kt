package com.baec23.ludwig.component.timepicker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.baec23.ludwig.core.spinner.BoundedSpinner
import com.baec23.ludwig.core.spinner.WrappingSpinner
import java.time.LocalTime

enum class TimePickerType {
    HoursMinutesSeconds12Hour,
    HoursMinutes12Hour,
    HoursMinutesSeconds24Hour,
    HoursMinutes24Hour,
}

@Composable
fun TimePicker(
    modifier: Modifier = Modifier,
    type: TimePickerType = TimePickerType.HoursMinutes12Hour,
    numRows: Int = 5,
    textStyle: TextStyle = MaterialTheme.typography.displaySmall,
    unselectedTextColor: Color = Color.Unspecified,
    selectedTextColor: Color = Color.Unspecified,
    initialTime: LocalTime = LocalTime.now(),
    offsetScaleMultiplier: Float = 0.25f,
    offsetAlphaMultiplier: Float = 0.25f,
    offsetRotationMultiplier: Float = 25f,
    onTimeChanged: (LocalTime) -> Unit,
) {
    when (type) {
        TimePickerType.HoursMinutesSeconds12Hour -> TimePickerHoursMinutesSeconds12Hour(
            modifier = modifier,
            numRows = numRows,
            textStyle = textStyle,
            unselectedTextColor = unselectedTextColor,
            selectedTextColor = selectedTextColor,
            initialTime = initialTime,
            offsetScaleMultiplier = offsetScaleMultiplier,
            offsetAlphaMultiplier = offsetAlphaMultiplier,
            offsetRotationMultiplier = offsetRotationMultiplier,
            onTimeChanged = onTimeChanged
        )

        TimePickerType.HoursMinutes12Hour -> TimePickerHoursMinutes12Hour(
            modifier = modifier,
            numRows = numRows,
            textStyle = textStyle,
            unselectedTextColor = unselectedTextColor,
            selectedTextColor = selectedTextColor,
            initialTime = initialTime,
            offsetScaleMultiplier = offsetScaleMultiplier,
            offsetAlphaMultiplier = offsetAlphaMultiplier,
            offsetRotationMultiplier = offsetRotationMultiplier,
            onTimeChanged = onTimeChanged
        )

        TimePickerType.HoursMinutesSeconds24Hour -> TimePickerHoursMinutesSeconds24Hour(
            modifier = modifier,
            numRows = numRows,
            textStyle = textStyle,
            unselectedTextColor = unselectedTextColor,
            selectedTextColor = selectedTextColor,
            initialTime = initialTime,
            offsetScaleMultiplier = offsetScaleMultiplier,
            offsetAlphaMultiplier = offsetAlphaMultiplier,
            offsetRotationMultiplier = offsetRotationMultiplier,
            onTimeChanged = onTimeChanged
        )

        TimePickerType.HoursMinutes24Hour -> TimePickerHoursMinutes24Hour(
            modifier = modifier,
            numRows = numRows,
            textStyle = textStyle,
            unselectedTextColor = unselectedTextColor,
            selectedTextColor = selectedTextColor,
            initialTime = initialTime,
            offsetScaleMultiplier = offsetScaleMultiplier,
            offsetAlphaMultiplier = offsetAlphaMultiplier,
            offsetRotationMultiplier = offsetRotationMultiplier,
            onTimeChanged = onTimeChanged
        )
    }
}

@Composable
private fun TimePickerHoursMinutesSeconds12Hour(
    modifier: Modifier = Modifier,
    numRows: Int = 5,
    textStyle: TextStyle = MaterialTheme.typography.displaySmall,
    unselectedTextColor: Color = Color.Unspecified,
    selectedTextColor: Color = Color.Unspecified,
    initialTime: LocalTime = LocalTime.now(),
    offsetScaleMultiplier: Float = 0.25f,
    offsetAlphaMultiplier: Float = 0.25f,
    offsetRotationMultiplier: Float = 25f,
    onTimeChanged: (LocalTime) -> Unit,
) {
    val validHours = mutableListOf("12")
    val validMinutes = mutableListOf<String>()
    val validSeconds = mutableListOf<String>()
    val amPm = listOf("AM", "PM")
    for (i in 1..11) {
        validHours.add("$i")
    }
    for (i in 0..59) {
        validMinutes.add("$i".padStart(2, '0'))
        validSeconds.add("$i".padStart(2, '0'))
    }

    var selectedTime by remember { mutableStateOf(initialTime) }
    var selectedHourIndex by remember { mutableStateOf(if (initialTime.hour in 1..11) initialTime.hour else initialTime.hour % 12) }
    var selectedMinuteIndex by remember { mutableStateOf(initialTime.minute) }
    var selectedSecondsIndex by remember { mutableStateOf(initialTime.second) }
    var selectedAmPmIndex by remember { mutableStateOf(if (initialTime.hour < 12) 0 else 1) }
    val hapticFeedback = LocalHapticFeedback.current

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            WrappingSpinner(
                items = validHours.toList(),
                numRows = numRows,
                textStyle = textStyle,
                unselectedTextColor = unselectedTextColor,
                selectedTextColor = selectedTextColor,
                horizontalAlignment = Alignment.End,
                initialSelectedIndex = selectedHourIndex,
                offsetScaleMultiplier = offsetScaleMultiplier,
                offsetAlphaMultiplier = offsetAlphaMultiplier,
                offsetRotationMultiplier = offsetRotationMultiplier
            ) {
                selectedHourIndex = it
                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                selectedTime = if (selectedAmPmIndex == 0)  //AM
                    selectedTime.withHour(selectedHourIndex)
                else    //PM
                    selectedTime.withHour(selectedHourIndex + 12)
                onTimeChanged(selectedTime)
            }
            Spacer(modifier = Modifier.width(15.dp))
            Text(text = ":", style = textStyle, color = selectedTextColor)
            Spacer(modifier = Modifier.width(15.dp))
            WrappingSpinner(
                items = validMinutes.toList(),
                numRows = numRows,
                textStyle = textStyle,
                unselectedTextColor = unselectedTextColor,
                selectedTextColor = selectedTextColor,
                horizontalAlignment = Alignment.CenterHorizontally,
                initialSelectedIndex = selectedMinuteIndex,
                offsetScaleMultiplier = offsetScaleMultiplier,
                offsetAlphaMultiplier = offsetAlphaMultiplier,
                offsetRotationMultiplier = offsetRotationMultiplier
            ) {
                selectedMinuteIndex = it
                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                selectedTime = selectedTime.withMinute(selectedMinuteIndex)
                onTimeChanged(selectedTime)
            }
            Spacer(modifier = Modifier.width(15.dp))
            Text(text = ":", style = textStyle, color = selectedTextColor)
            Spacer(modifier = Modifier.width(15.dp))
            WrappingSpinner(
                items = validSeconds.toList(),
                numRows = numRows,
                textStyle = textStyle,
                unselectedTextColor = unselectedTextColor,
                selectedTextColor = selectedTextColor,
                horizontalAlignment = Alignment.Start,
                initialSelectedIndex = selectedSecondsIndex,
                offsetScaleMultiplier = offsetScaleMultiplier,
                offsetAlphaMultiplier = offsetAlphaMultiplier,
                offsetRotationMultiplier = offsetRotationMultiplier
            ) {
                selectedSecondsIndex = it
                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                selectedTime = selectedTime.withSecond(selectedSecondsIndex)
                onTimeChanged(selectedTime)
            }
        }
        Spacer(modifier = Modifier.width(30.dp))
        BoundedSpinner(
            items = amPm.toList(),
            numRows = numRows,
            textStyle = textStyle,
            unselectedTextColor = unselectedTextColor,
            selectedTextColor = selectedTextColor,
            horizontalAlignment = Alignment.CenterHorizontally,
            initialSelectedIndex = selectedAmPmIndex,
            offsetScaleMultiplier = offsetScaleMultiplier,
            offsetAlphaMultiplier = offsetAlphaMultiplier,
            offsetRotationMultiplier = offsetRotationMultiplier
        ) {
            selectedAmPmIndex = it
            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            selectedTime = if (selectedAmPmIndex == 0)
                selectedTime.withHour(selectedHourIndex)
            else
                selectedTime.withHour(selectedHourIndex + 12)
            onTimeChanged(selectedTime)
        }
    }
}

@Composable
private fun TimePickerHoursMinutes12Hour(
    modifier: Modifier = Modifier,
    numRows: Int = 5,
    textStyle: TextStyle = MaterialTheme.typography.displaySmall,
    unselectedTextColor: Color = Color.Unspecified,
    selectedTextColor: Color = Color.Unspecified,
    initialTime: LocalTime = LocalTime.now(),
    offsetScaleMultiplier: Float = 0.25f,
    offsetAlphaMultiplier: Float = 0.25f,
    offsetRotationMultiplier: Float = 25f,
    onTimeChanged: (LocalTime) -> Unit,
) {
    val validHours = mutableListOf("12")
    val validMinutes = mutableListOf<String>()
    val amPm = listOf("AM", "PM")
    for (i in 1..11) {
        validHours.add("$i")
    }
    for (i in 0..59) {
        validMinutes.add("$i".padStart(2, '0'))
    }

    var selectedTime by remember { mutableStateOf(initialTime) }
    var selectedHourIndex by remember { mutableStateOf(if (initialTime.hour in 1..11) initialTime.hour else initialTime.hour % 12) }
    var selectedMinuteIndex by remember { mutableStateOf(initialTime.minute) }
    var selectedAmPmIndex by remember { mutableStateOf(if (initialTime.hour < 12) 0 else 1) }
    val hapticFeedback = LocalHapticFeedback.current

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            WrappingSpinner(
                items = validHours.toList(),
                numRows = numRows,
                textStyle = textStyle,
                unselectedTextColor = unselectedTextColor,
                selectedTextColor = selectedTextColor,
                horizontalAlignment = Alignment.End,
                initialSelectedIndex = selectedHourIndex,
                offsetScaleMultiplier = offsetScaleMultiplier,
                offsetAlphaMultiplier = offsetAlphaMultiplier,
                offsetRotationMultiplier = offsetRotationMultiplier
            ) {
                selectedHourIndex = it
                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                selectedTime = if (selectedAmPmIndex == 0)
                    selectedTime.withHour(selectedHourIndex)
                else
                    selectedTime.withHour(selectedHourIndex + 12)
                onTimeChanged(selectedTime)
            }
            Spacer(modifier = Modifier.width(15.dp))
            Text(text = ":", style = textStyle, color = selectedTextColor)
            Spacer(modifier = Modifier.width(15.dp))
            WrappingSpinner(
                items = validMinutes.toList(),
                numRows = numRows,
                textStyle = textStyle,
                unselectedTextColor = unselectedTextColor,
                selectedTextColor = selectedTextColor,
                horizontalAlignment = Alignment.Start,
                initialSelectedIndex = selectedMinuteIndex,
                offsetScaleMultiplier = offsetScaleMultiplier,
                offsetAlphaMultiplier = offsetAlphaMultiplier,
                offsetRotationMultiplier = offsetRotationMultiplier
            ) {
                selectedMinuteIndex = it
                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                selectedTime = selectedTime.withMinute(selectedMinuteIndex)
                onTimeChanged(selectedTime)
            }
        }
        Spacer(modifier = Modifier.width(30.dp))
        BoundedSpinner(
            items = amPm.toList(),
            numRows = numRows,
            textStyle = textStyle,
            unselectedTextColor = unselectedTextColor,
            selectedTextColor = selectedTextColor,
            horizontalAlignment = Alignment.CenterHorizontally,
            initialSelectedIndex = selectedAmPmIndex,
            offsetScaleMultiplier = offsetScaleMultiplier,
            offsetAlphaMultiplier = offsetAlphaMultiplier,
            offsetRotationMultiplier = offsetRotationMultiplier
        ) {
            selectedAmPmIndex = it
            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            selectedTime = if (selectedAmPmIndex == 0)
                selectedTime.withHour(selectedHourIndex)
            else
                selectedTime.withHour(selectedHourIndex + 12)
            onTimeChanged(selectedTime)
        }
    }
}

@Composable
private fun TimePickerHoursMinutesSeconds24Hour(
    modifier: Modifier = Modifier,
    numRows: Int = 5,
    textStyle: TextStyle = MaterialTheme.typography.displaySmall,
    unselectedTextColor: Color = Color.Unspecified,
    selectedTextColor: Color = Color.Unspecified,
    initialTime: LocalTime = LocalTime.now(),
    offsetScaleMultiplier: Float = 0.25f,
    offsetAlphaMultiplier: Float = 0.25f,
    offsetRotationMultiplier: Float = 25f,
    onTimeChanged: (LocalTime) -> Unit,
) {
    val validHours = mutableListOf<String>()
    val validMinutes = mutableListOf<String>()
    val validSeconds = mutableListOf<String>()
    for (i in 0..23) {
        validHours.add("$i")
    }
    for (i in 0..59) {
        validMinutes.add("$i".padStart(2, '0'))
        validSeconds.add("$i".padStart(2, '0'))
    }

    var selectedTime by remember { mutableStateOf(initialTime) }
    var selectedHourIndex by remember { mutableStateOf(initialTime.hour) }
    var selectedMinuteIndex by remember { mutableStateOf(initialTime.minute) }
    var selectedSecondsIndex by remember { mutableStateOf(initialTime.second) }
    val hapticFeedback = LocalHapticFeedback.current

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            WrappingSpinner(
                items = validHours.toList(),
                numRows = numRows,
                textStyle = textStyle,
                unselectedTextColor = unselectedTextColor,
                selectedTextColor = selectedTextColor,
                horizontalAlignment = Alignment.End,
                initialSelectedIndex = selectedHourIndex,
                offsetScaleMultiplier = offsetScaleMultiplier,
                offsetAlphaMultiplier = offsetAlphaMultiplier,
                offsetRotationMultiplier = offsetRotationMultiplier
            ) {
                selectedHourIndex = it
                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                selectedTime = selectedTime.withHour(selectedHourIndex)
                onTimeChanged(selectedTime)
            }
            Spacer(modifier = Modifier.width(15.dp))
            Text(text = ":", style = textStyle, color = selectedTextColor)
            Spacer(modifier = Modifier.width(15.dp))
            WrappingSpinner(
                items = validMinutes.toList(),
                numRows = numRows,
                textStyle = textStyle,
                unselectedTextColor = unselectedTextColor,
                selectedTextColor = selectedTextColor,
                horizontalAlignment = Alignment.CenterHorizontally,
                initialSelectedIndex = selectedMinuteIndex,
                offsetScaleMultiplier = offsetScaleMultiplier,
                offsetAlphaMultiplier = offsetAlphaMultiplier,
                offsetRotationMultiplier = offsetRotationMultiplier
            ) {
                selectedMinuteIndex = it
                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                selectedTime = selectedTime.withMinute(selectedMinuteIndex)
                onTimeChanged(selectedTime)
            }
            Spacer(modifier = Modifier.width(15.dp))
            Text(text = ":", style = textStyle, color = selectedTextColor)
            Spacer(modifier = Modifier.width(15.dp))
            WrappingSpinner(
                items = validSeconds.toList(),
                numRows = numRows,
                textStyle = textStyle,
                unselectedTextColor = unselectedTextColor,
                selectedTextColor = selectedTextColor,
                horizontalAlignment = Alignment.Start,
                initialSelectedIndex = selectedSecondsIndex,
                offsetScaleMultiplier = offsetScaleMultiplier,
                offsetAlphaMultiplier = offsetAlphaMultiplier,
                offsetRotationMultiplier = offsetRotationMultiplier
            ) {
                selectedSecondsIndex = it
                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                selectedTime = selectedTime.withSecond(selectedSecondsIndex)
                onTimeChanged(selectedTime)
            }
        }
    }
}

@Composable
private fun TimePickerHoursMinutes24Hour(
    modifier: Modifier = Modifier,
    numRows: Int = 5,
    textStyle: TextStyle = MaterialTheme.typography.displaySmall,
    unselectedTextColor: Color = Color.Unspecified,
    selectedTextColor: Color = Color.Unspecified,
    initialTime: LocalTime = LocalTime.now(),
    offsetScaleMultiplier: Float = 0.25f,
    offsetAlphaMultiplier: Float = 0.25f,
    offsetRotationMultiplier: Float = 25f,
    onTimeChanged: (LocalTime) -> Unit,
) {
    val validHours = mutableListOf<String>()
    val validMinutes = mutableListOf<String>()
    for (i in 0..23) {
        validHours.add("$i")
    }
    for (i in 0..59) {
        validMinutes.add("$i".padStart(2, '0'))
    }

    var selectedTime by remember { mutableStateOf(initialTime) }
    var selectedHourIndex by remember { mutableStateOf(initialTime.hour) }
    var selectedMinuteIndex by remember { mutableStateOf(initialTime.minute) }
    val hapticFeedback = LocalHapticFeedback.current

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            WrappingSpinner(
                items = validHours.toList(),
                numRows = numRows,
                textStyle = textStyle,
                unselectedTextColor = unselectedTextColor,
                selectedTextColor = selectedTextColor,
                horizontalAlignment = Alignment.End,
                initialSelectedIndex = selectedHourIndex,
                offsetScaleMultiplier = offsetScaleMultiplier,
                offsetAlphaMultiplier = offsetAlphaMultiplier,
                offsetRotationMultiplier = offsetRotationMultiplier
            ) {
                selectedHourIndex = it
                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                selectedTime = selectedTime.withHour(selectedHourIndex)
                onTimeChanged(selectedTime)
            }
            Spacer(modifier = Modifier.width(15.dp))
            Text(text = ":", style = textStyle, color = selectedTextColor)
            Spacer(modifier = Modifier.width(15.dp))
            WrappingSpinner(
                items = validMinutes.toList(),
                numRows = numRows,
                textStyle = textStyle,
                unselectedTextColor = unselectedTextColor,
                selectedTextColor = selectedTextColor,
                horizontalAlignment = Alignment.Start,
                initialSelectedIndex = selectedMinuteIndex,
                offsetScaleMultiplier = offsetScaleMultiplier,
                offsetAlphaMultiplier = offsetAlphaMultiplier,
                offsetRotationMultiplier = offsetRotationMultiplier
            ) {
                selectedMinuteIndex = it
                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                selectedTime = selectedTime.withMinute(selectedMinuteIndex)
                onTimeChanged(selectedTime)
            }
        }
    }
}
