package com.baec23.ludwig.core.autoResizingText

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp


@Composable
fun AutoResizingText(
    modifier: Modifier = Modifier,
    text: String,
    mode: AutoResizingTextMode = AutoResizingTextMode.Height,
    minFontSize: TextUnit = 10.sp,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    style: TextStyle = LocalTextStyle.current
) {
    var scaledTextStyle by remember { mutableStateOf(style) }
    var readyToDraw by remember { mutableStateOf(false) }
    Text(
        modifier = modifier.drawWithContent { if (readyToDraw) drawContent() },
        text = text,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = { textLayoutResult ->
            if (readyToDraw) return@Text
            if (scaledTextStyle.fontSize < minFontSize) {
                scaledTextStyle = scaledTextStyle.copy(fontSize = minFontSize)
                readyToDraw = true
                return@Text
            }
            when (mode) {
                AutoResizingTextMode.Width -> {
                    if (textLayoutResult.didOverflowWidth)
                        scaledTextStyle =
                            scaledTextStyle.copy(fontSize = scaledTextStyle.fontSize * 0.9f)
                    else
                        readyToDraw = true
                }

                AutoResizingTextMode.Height -> {
                    if (textLayoutResult.didOverflowHeight)
                        scaledTextStyle =
                            scaledTextStyle.copy(fontSize = scaledTextStyle.fontSize * 0.9f)
                    else
                        readyToDraw = true
                }

                AutoResizingTextMode.WidthOrHeight -> {
                    if (textLayoutResult.hasVisualOverflow)
                        scaledTextStyle =
                            scaledTextStyle.copy(fontSize = scaledTextStyle.fontSize * 0.9f)
                    else
                        readyToDraw = true
                }
            }
        },
        style = scaledTextStyle,
    )
}

enum class AutoResizingTextMode {
    Width,
    Height,
    WidthOrHeight
}