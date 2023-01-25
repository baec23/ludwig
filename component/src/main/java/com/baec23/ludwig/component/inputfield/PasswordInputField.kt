package com.baec23.ludwig.component.inputfield

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun PasswordInputField(
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
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        capitalization = KeyboardCapitalization.None,
        autoCorrect = false,
        keyboardType = KeyboardType.Password,
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    minLines: Int = 1,
    maxLines: Int = 1,
    singleLine: Boolean = minLines == 1 && maxLines == 1,
    inputValidator: InputValidator = InputValidator.Password
) {

    InputField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = readOnly,
        label = label,
        placeholder = placeholder,
        unfocusedAlpha = unfocusedAlpha,
        underlineBarColor1 = underlineBarColor1,
        underlineBarColor2 = underlineBarColor2,
        textStyle = textStyle,
        labelStyle = labelStyle,
        placeholderStyle = placeholderStyle,
        hasError = hasError,
        errorMessage = errorMessage,
        errorStyle = errorStyle,
        minLines = 1,
        maxLines = 1,
        singleLine = singleLine,
        inputValidator = inputValidator,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = PasswordVisualTransformation()
    )
}