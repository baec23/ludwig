package com.baec23.ludwig.component.navbar

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.baec23.ludwig.component.toggleable.ToggleableIcon

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    items: List<BottomNavigationItem>,
    currNavScreenRoute: String?,
    selectedItemScale: Float = 1f,
    unselectedItemScale: Float = 0.75f,
    selectedItemColor: Color = MaterialTheme.colorScheme.primary,
    unselectedItemColor: Color = Color.LightGray,
    backgroundColor: Color = NavigationBarDefaults.containerColor,
    onBottomNavigationItemPressed: (BottomNavigationItem) -> Unit,
) {
    NavigationBar(modifier = modifier, containerColor = backgroundColor) {
        items.forEach { item ->
            ToggleableIcon(
                modifier = Modifier.weight(1f),
                isToggled = item.route == currNavScreenRoute,
                imageVector = item.iconImageVector,
                contentDescription = item.label,
                label = item.label,
                toggledOnColor = selectedItemColor,
                toggledOffColor = unselectedItemColor,
                toggledOnScale = selectedItemScale,
                toggledOffScale = unselectedItemScale
            ) {
                onBottomNavigationItemPressed(item)
            }
        }
    }
}

data class BottomNavigationItem(
    val iconImageVector: ImageVector,
    val route: String,
    val label: String? = null,
)