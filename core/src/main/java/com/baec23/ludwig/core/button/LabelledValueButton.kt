package com.baec23.ludwig.component.button

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import java.util.concurrent.CancellationException
import kotlin.math.roundToInt

@Composable
fun LabelledValueButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: @Composable BoxScope.() -> Unit,
    contentPadding: PaddingValues = PaddingValues(20.dp),
    containerShape: Shape = RoundedCornerShape(8.dp),
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    iconColor: Color = Color.LightGray,
    pressedContainerColor: Color = Color.LightGray,
    pressedIconColor: Color = Color.DarkGray,
    painter: Painter = rememberVectorPainter(image = Icons.Default.ArrowForward),
    value: @Composable BoxScope.() -> Unit,
) {
    var buttonAnimationState by remember { mutableStateOf(ButtonAnimationState.Idle) }

    val isPressedTransition =
        updateTransition(targetState = buttonAnimationState, label = "isPressedTransition")
    val animatedIconScale by isPressedTransition.animateFloat(
        label = "iconScaleAnimation",
        transitionSpec = {
            if (buttonAnimationState == ButtonAnimationState.Pressed)
                tween(50)
            else
                spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessMedium
                )
        }) {
        if (it == ButtonAnimationState.Pressed) 1.5f else 1f
    }
    val animatedIconColor by isPressedTransition.animateColor(label = "iconColorAnimation",
        transitionSpec = {
            if (buttonAnimationState == ButtonAnimationState.Pressed)
                tween(50)
            else
                spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessMedium
                )
        }) {
        if (it == ButtonAnimationState.Pressed) pressedIconColor else iconColor
    }
    val animatedContainerColor by isPressedTransition.animateColor(label = "containerColorAnimation",
        transitionSpec = {
            if (buttonAnimationState == ButtonAnimationState.Pressed)
                tween(50)
            else
                spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessMedium
                )
        }) {
        if (it == ButtonAnimationState.Pressed) pressedContainerColor else containerColor
    }
    val animatedYOffset by isPressedTransition.animateDp(label = "offsetAnimation",
        transitionSpec = {
            if (buttonAnimationState == ButtonAnimationState.Pressed)
                tween(50)
            else
                spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessMedium
                )
        }) {
        if (it == ButtonAnimationState.Pressed) 2.dp else 0.dp
    }
    val animatedContainerScale by isPressedTransition.animateFloat(label = "containerScaleAnimation",
        transitionSpec = {
            if (buttonAnimationState == ButtonAnimationState.Pressed)
                tween(50)
            else
                spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessMedium
                )
        }) {
        if (it == ButtonAnimationState.Pressed) 0.95f else 0.98f
    }

    Card(
        modifier = modifier
            .offset(
                x = 0.dp,
                y = animatedYOffset
            )
            .scale(animatedContainerScale)
            .run {
                this.pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            buttonAnimationState = ButtonAnimationState.Pressed
                            val isReleased = try {
                                tryAwaitRelease()
                            } catch (e: CancellationException) {
                                false
                            }
                            buttonAnimationState = ButtonAnimationState.Idle
                            if (isReleased) {
                                buttonAnimationState = ButtonAnimationState.Idle
                                onClick()
                            }
                        },
                    )
                }
            },
        shape = containerShape,
        colors = CardDefaults.cardColors(containerColor = animatedContainerColor),
    ) {
        Row(
            modifier = Modifier.padding(contentPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                label()
            }
            Spacer(modifier = Modifier.width(10.dp))
            Box(modifier = Modifier.maxWidth(0.65f)) {
                value()
            }
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                modifier = Modifier
                    .requiredWidthIn(min = 20.dp, max = 30.dp)
                    .scale(animatedIconScale),
                painter = painter,
                contentDescription = null,
                tint = animatedIconColor
            )
        }
    }
}

@Composable
fun LabelledValueButton(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    labelStyle: TextStyle = MaterialTheme.typography.labelMedium,
    valueStyle: TextStyle = MaterialTheme.typography.titleLarge,
    contentPadding: PaddingValues = PaddingValues(20.dp),
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    containerShape: Shape = RoundedCornerShape(8.dp),
    pressedContainerColor: Color = Color.LightGray,
    pressedIconColor: Color = Color.DarkGray,
    iconColor: Color = Color.DarkGray,
    iconImageVector: ImageVector,
    onClick: () -> Unit,
) {
    LabelledValueButton(
        modifier = modifier,
        label = label,
        value = value,
        labelStyle = labelStyle,
        valueStyle = valueStyle,
        contentPadding = contentPadding,
        containerColor = containerColor,
        containerShape = containerShape,
        iconColor = iconColor,
        painter = rememberVectorPainter(iconImageVector),
        pressedContainerColor = pressedContainerColor,
        pressedIconColor = pressedIconColor,
        onClick = onClick,
    )
}

@Composable
fun LabelledValueButton(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    labelStyle: TextStyle = MaterialTheme.typography.labelMedium,
    valueStyle: TextStyle = MaterialTheme.typography.titleLarge,
    contentPadding: PaddingValues = PaddingValues(20.dp),
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    containerShape: Shape = RoundedCornerShape(8.dp),
    iconColor: Color = Color.DarkGray,
    pressedContainerColor: Color = Color.LightGray,
    pressedIconColor: Color = Color.DarkGray,
    painter: Painter = rememberVectorPainter(image = Icons.Default.ArrowForward),
    onClick: () -> Unit,
) {
    LabelledValueButton(
        modifier = modifier,
        label = {
            Text(
                text = label,
                style = labelStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        value = {
            Text(
                text = value,
                style = valueStyle,
                textAlign = TextAlign.End,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        contentPadding = contentPadding,
        containerColor = containerColor,
        containerShape = containerShape,
        iconColor = iconColor,
        painter = painter,
        pressedContainerColor = pressedContainerColor,
        pressedIconColor = pressedIconColor,
        onClick = onClick
    )
}

@Composable
fun LabelledValueButton(
    modifier: Modifier = Modifier,
    label: AnnotatedString,
    value: AnnotatedString,
    labelStyle: TextStyle = MaterialTheme.typography.labelMedium,
    valueStyle: TextStyle = MaterialTheme.typography.titleLarge,
    contentPadding: PaddingValues = PaddingValues(20.dp),
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    containerShape: Shape = RoundedCornerShape(8.dp),
    pressedContainerColor: Color = Color.LightGray,
    pressedIconColor: Color = Color.DarkGray,
    iconColor: Color = Color.DarkGray,
    painter: Painter = rememberVectorPainter(image = Icons.Default.ArrowForward),
    onClick: () -> Unit,
) {
    LabelledValueButton(
        modifier = modifier,
        label = {
            Text(
                text = label,
                style = labelStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        value = {
            Text(
                text = value,
                style = valueStyle,
                textAlign = TextAlign.End,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        contentPadding = contentPadding,
        containerColor = containerColor,
        containerShape = containerShape,
        pressedContainerColor = pressedContainerColor,
        pressedIconColor = pressedIconColor,
        iconColor = iconColor,
        painter = painter,
        onClick = onClick
    )
}

@Composable
fun LabelledValueButton(
    modifier: Modifier = Modifier,
    label: String,
    value: AnnotatedString,
    labelStyle: TextStyle = MaterialTheme.typography.labelMedium,
    valueStyle: TextStyle = MaterialTheme.typography.titleLarge,
    contentPadding: PaddingValues = PaddingValues(20.dp),
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    containerShape: Shape = RoundedCornerShape(8.dp),
    pressedContainerColor: Color = Color.LightGray,
    pressedIconColor: Color = Color.DarkGray,
    iconColor: Color = Color.DarkGray,
    painter: Painter = rememberVectorPainter(image = Icons.Default.ArrowForward),
    onClick: () -> Unit,
) {
    LabelledValueButton(
        modifier = modifier,
        label = {
            Text(
                text = label,
                style = labelStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        value = {
            Text(
                text = value,
                style = valueStyle,
                textAlign = TextAlign.End,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        contentPadding = contentPadding,
        containerColor = containerColor,
        containerShape = containerShape,
        pressedIconColor = pressedIconColor,
        pressedContainerColor = pressedContainerColor,
        iconColor = iconColor,
        painter = painter,
        onClick = onClick
    )
}

@Composable
fun LabelledValueButton(
    modifier: Modifier = Modifier,
    label: AnnotatedString,
    value: String,
    labelStyle: TextStyle = MaterialTheme.typography.labelMedium,
    valueStyle: TextStyle = MaterialTheme.typography.titleLarge,
    contentPadding: PaddingValues = PaddingValues(20.dp),
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    containerShape: Shape = RoundedCornerShape(8.dp),
    pressedContainerColor: Color = Color.LightGray,
    pressedIconColor: Color = Color.DarkGray,
    iconColor: Color = Color.DarkGray,
    painter: Painter = rememberVectorPainter(image = Icons.Default.ArrowForward),
    onClick: () -> Unit,
) {
    LabelledValueButton(
        modifier = modifier,
        label = {
            Text(
                text = label,
                style = labelStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        value = {
            Text(
                text = value,
                style = valueStyle,
                textAlign = TextAlign.End,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        contentPadding = contentPadding,
        containerColor = containerColor,
        containerShape = containerShape,
        pressedContainerColor = pressedContainerColor,
        pressedIconColor = pressedIconColor,
        iconColor = iconColor,
        painter = painter,
        onClick = onClick
    )
}

fun Modifier.maxWidth(
    fraction: Float = 1f,
) = layout { measurable, constraints ->
    val maxWidth = (constraints.maxWidth * fraction).roundToInt()
    val width = measurable.maxIntrinsicWidth(constraints.maxHeight).coerceAtMost(maxWidth)

    val placeable = measurable.measure(
        Constraints(
            constraints.minWidth,
            width,
            constraints.minHeight,
            constraints.maxHeight
        )
    )
    layout(width, placeable.height) {
        placeable.placeRelative(0, 0)
    }
}