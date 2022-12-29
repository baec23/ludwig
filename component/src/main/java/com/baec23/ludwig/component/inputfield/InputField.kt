package com.baec23.ludwig.component.inputfield

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    label: String? = null,
    placeholder: String? = null,
    unfocusedAlpha: Float = 0.5f,
    underlineBarColor1: Color = MaterialTheme.colorScheme.primary,
    underlineBarColor2: Color = Color.LightGray,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    labelStyle: TextStyle = MaterialTheme.typography.labelSmall,
    placeholderStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.LightGray),
    errorStyle: TextStyle = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.error),
    hasError: Boolean = false,
    errorMessage: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    singleLine: Boolean = minLines == 1 && maxLines == 1,
    inputValidator: InputValidator = if (singleLine) InputValidator.TextNoSpaces else InputValidator.TextWithSpacesAndNewLine
) {

    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val isFocusedTransition =
        updateTransition(targetState = isFocused, label = "isFocusedTransition")

    val underlineBarWidth by isFocusedTransition.animateDp(
        label = "underlineBarWidth",
        transitionSpec = {
            when (isFocused) {
                true -> tween(100)
                false -> tween(500)
            }
        }) { if (it) 2.dp else 1.dp }

    val focusAlpha by isFocusedTransition.animateFloat(label = "focusAlpha", transitionSpec = {
        when (isFocused) {
            true -> tween(100)
            false -> tween(500)
        }
    }) {
        if (it) 1f else unfocusedAlpha
    }

    val currScreenWidth =
        LocalDensity.current.run { Dp(LocalConfiguration.current.screenWidthDp.toFloat()).toPx() }
    val underlineBarGradientX by isFocusedTransition.animateFloat(
        label = "underlineBarGradientX",
        transitionSpec = {
            when (isFocused) {
                true -> tween(300)
                false -> tween(500)
            }
        }) { if (it) currScreenWidth else 0f }

    Column(modifier = modifier) {

        label?.let {
            Text(
                modifier = Modifier.alpha(focusAlpha),
                text = label,
                style = labelStyle,
                maxLines = 1,
                overflow = TextOverflow.Clip
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        BasicTextField(
            modifier = modifier
                .onFocusEvent { focusState -> isFocused = focusState.isFocused }
                .onKeyEvent {
                    handleKeyEvent(
                        keyEvent = it,
                        focusManager = focusManager,
                        isSingleLine = singleLine
                    )
                },
            value = value,
            onValueChange = {
                onValueChange(inputValidator.filter(it))
            },
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            minLines = minLines,
            maxLines = maxLines,
            decorationBox = { innerTextField ->
                Column {
                    Box(contentAlignment = Alignment.CenterStart) {
                        placeholder?.let {
                            androidx.compose.animation.AnimatedVisibility(
                                visible = (value.isEmpty()),
                                enter = fadeIn(animationSpec = tween(200)),
                                exit = fadeOut(animationSpec = tween(100))
                            ) {
                                Text(
                                    text = placeholder,
                                    style = placeholderStyle,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                        innerTextField()
                    }
                    val underlineBarColors = listOf(underlineBarColor1, underlineBarColor2)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp), contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(underlineBarWidth)
                                .alpha(focusAlpha)
                                .run {
                                    if (hasError) this.background(MaterialTheme.colorScheme.error)
                                    else this.background(
                                        brush = Brush.horizontalGradient(
                                            colors = underlineBarColors,
                                            startX = underlineBarGradientX,
                                            endX = underlineBarGradientX + 1
                                        )
                                    )
                                }
                        )
                    }
                }
            }
        )
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.End),
            visible = hasError && errorMessage != null
        ) {
            errorMessage?.let {
                Text(
                    text = errorMessage,
                    style = errorStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
private fun handleKeyEvent(
    keyEvent: KeyEvent,
    focusManager: FocusManager,
    isSingleLine: Boolean
): Boolean {
    when (keyEvent.key) {
        Key.Enter -> {
            if (isSingleLine) {
                val result = focusManager.moveFocus(FocusDirection.Next)
                if (!result) {
                    focusManager.clearFocus()
                }
            }
        }

        Key.Tab -> {
            val result = focusManager.moveFocus(FocusDirection.Next)
            if (!result) {
                focusManager.clearFocus()
            }
        }
    }
    return true
}
