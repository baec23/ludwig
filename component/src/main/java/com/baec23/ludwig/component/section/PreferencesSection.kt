package com.baec23.ludwig.component.section

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PreferencesSection(
    modifier: Modifier = Modifier,
    headerTitle: String,
    headerIcon: ImageVector? = null,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            if (headerIcon != null) {
                Icon(
                    imageVector = headerIcon,
                    contentDescription = headerTitle,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(headerTitle)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Divider()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            content()
        }
    }
}

@Preview
@Composable
fun PreferencesSectionPreview() {
    PreferencesSection(
        headerTitle = "My Section Title",
        headerIcon = Icons.Rounded.Favorite
    ) {
        Text("Content")
    }
}