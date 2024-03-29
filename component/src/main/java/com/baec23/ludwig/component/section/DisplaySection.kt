package com.baec23.ludwig.component.section

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DisplaySection(
    modifier: Modifier = Modifier,
    headerText: String,
    headerSubtext: String? = null,
    headerIcon: ImageVector? = null,
    headerTextStyle: TextStyle = MaterialTheme.typography.titleMedium,
    headerSubtextStyle: TextStyle = MaterialTheme.typography.titleSmall,
    headerIconColor: Color = MaterialTheme.colorScheme.primary,
    dividerColor: Color = MaterialTheme.colorScheme.primary,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    contentSpacing: Dp = 12.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    val textHeightDp = with(LocalDensity.current) { headerTextStyle.lineHeight.toDp() }
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.Top) {
            headerIcon?.let {
                Icon(
                    modifier = Modifier.size(textHeightDp),
                    imageVector = headerIcon,
                    contentDescription = headerText,
                    tint = headerIconColor
                )
                Spacer(modifier = Modifier.width(4.dp))
            }

            Column {
                Text(
                    text = headerText,
                    style = headerTextStyle,
                    textAlign = TextAlign.Start,
                )
                headerSubtext?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = headerSubtext,
                        style = headerSubtextStyle,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(2.dp))
        Divider(color = dividerColor, thickness = 2.dp)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(contentSpacing),
            horizontalAlignment = horizontalAlignment
        ) {
            content()
        }
    }
}

@Composable
fun ExpandableDisplaySection(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    onExpand: () -> Unit,
    headerText: String,
    headerSubtext: String? = null,
    headerIcon: ImageVector? = null,
    headerTextStyle: TextStyle = MaterialTheme.typography.titleMedium,
    headerSubtextStyle: TextStyle = MaterialTheme.typography.titleSmall,
    headerIconColor: Color = MaterialTheme.colorScheme.primary,
    dividerColor: Color = MaterialTheme.colorScheme.primary,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    contentSpacing: Dp = 12.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    val arrowRotation by animateFloatAsState(
        targetValue = if (isExpanded) 90f else 0f, label = "arrowRotation",
    )
    val textHeightDp = with(LocalDensity.current) { headerTextStyle.lineHeight.toDp() }
    Column(modifier = modifier) {
        //Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                    onExpand()
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column(modifier = Modifier.weight(1f, fill = true)) {
                Row(verticalAlignment = Alignment.Top) {
                    headerIcon?.let {
                        Icon(
                            modifier = Modifier.size(textHeightDp),
                            imageVector = headerIcon,
                            contentDescription = headerText,
                            tint = headerIconColor
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Column {
                        Text(
                            text = headerText,
                            style = headerTextStyle,
                            textAlign = TextAlign.Start,
                        )
                        headerSubtext?.let {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = headerSubtext,
                                style = headerSubtextStyle,
                                textAlign = TextAlign.Start
                            )
                        }
                    }
                }
            }
            Column(modifier = Modifier.padding(start = 20.dp)) {
                Icon(
                    modifier = Modifier.rotate(arrowRotation),
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "expand"
                )
            }
        }

        //Divider
        Spacer(modifier = Modifier.height(2.dp))
        Divider(color = dividerColor, thickness = 2.dp)
        //Content
        AnimatedVisibility(visible = isExpanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
                verticalArrangement = Arrangement.spacedBy(contentSpacing),
                horizontalAlignment = horizontalAlignment
            ) {
                content()
            }
        }
    }
}