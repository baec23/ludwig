package com.baec23.ludwig.composable_components.togglable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.baec23.ludwig.composable_components.FadingLazyColumn
import com.baec23.ludwig.composable_components.FadingLazyHorizontalGrid
import com.baec23.ludwig.composable_components.FadingLazyRow
import com.baec23.ludwig.composable_components.FadingLazyVerticalGrid

@Composable
fun ToggleableIconRow(
    modifier: Modifier = Modifier,
    toggleableIconListItems: List<ToggleableIconListItem>,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    toggledOnColor: Color = MaterialTheme.colorScheme.primary,
    toggledOffColor: Color = Color.LightGray,
    toggledOnScale: Float = 1f,
    toggledOffScale: Float = 0.75f,
    onItemToggle: (Int) -> Unit,
) {
    FadingLazyRow(
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {
        items(toggleableIconListItems.count()) { itemIndex ->
            val item = toggleableIconListItems[itemIndex]
            ToggleableIcon(
                isToggled = item.isToggled,
                imageVector = item.iconImageVector,
                label = item.label,
                toggledOnColor = toggledOnColor,
                toggledOffColor = toggledOffColor,
                toggledOnScale = toggledOnScale,
                toggledOffScale = toggledOffScale
            ) {
                onItemToggle(itemIndex)
            }
        }
    }
}

@Composable
fun ToggleableIconColumn(
    modifier: Modifier = Modifier,
    toggleableIconListItems: List<ToggleableIconListItem>,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    toggledOnColor: Color = MaterialTheme.colorScheme.primary,
    toggledOffColor: Color = Color.LightGray,
    toggledOnScale: Float = 1f,
    toggledOffScale: Float = 0.75f,
    onItemToggle: (Int) -> Unit,
) {
    FadingLazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = verticalArrangement
    ) {
        items(toggleableIconListItems.count()) { itemIndex ->
            val item = toggleableIconListItems[itemIndex]
            ToggleableIcon(
                isToggled = item.isToggled,
                imageVector = item.iconImageVector,
                label = item.label,
                toggledOnColor = toggledOnColor,
                toggledOffColor = toggledOffColor,
                toggledOnScale = toggledOnScale,
                toggledOffScale = toggledOffScale
            ) {
                onItemToggle(itemIndex)
            }
        }
    }
}

@Composable
fun ToggleableIconVerticalGrid(
    modifier: Modifier = Modifier,
    columns: GridCells,
    toggleableIconListItems: List<ToggleableIconListItem>,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    toggledOnColor: Color = MaterialTheme.colorScheme.primary,
    toggledOffColor: Color = Color.LightGray,
    toggledOnScale: Float = 1f,
    toggledOffScale: Float = 0.75f,
    onItemToggle: (Int) -> Unit,
) {
    FadingLazyVerticalGrid(
        modifier = modifier,
        columns = columns,
        contentPadding = contentPadding,
        verticalArrangement = verticalArrangement,
        horizontalArrangement = horizontalArrangement
    ) {
        items(toggleableIconListItems.count()) { itemIndex ->
            val item = toggleableIconListItems[itemIndex]
            ToggleableIcon(
                isToggled = item.isToggled,
                imageVector = item.iconImageVector,
                label = item.label,
                toggledOnColor = toggledOnColor,
                toggledOffColor = toggledOffColor,
                toggledOnScale = toggledOnScale,
                toggledOffScale = toggledOffScale
            ) {
                onItemToggle(itemIndex)
            }
        }
    }
}

@Composable
fun ToggleableIconHorizontalGrid(
    modifier: Modifier = Modifier,
    rows: GridCells,
    toggleableIconListItems: List<ToggleableIconListItem>,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    toggledOnColor: Color = MaterialTheme.colorScheme.primary,
    toggledOffColor: Color = Color.LightGray,
    toggledOnScale: Float = 1f,
    toggledOffScale: Float = 0.75f,
    onItemToggle: (Int) -> Unit,
) {
    FadingLazyHorizontalGrid(
        modifier = modifier,
        rows = rows,
        contentPadding = contentPadding,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement
    ) {
        items(toggleableIconListItems.count()) { itemIndex ->
            val item = toggleableIconListItems[itemIndex]
            ToggleableIcon(
                isToggled = item.isToggled,
                imageVector = item.iconImageVector,
                label = item.label,
                toggledOnColor = toggledOnColor,
                toggledOffColor = toggledOffColor,
                toggledOnScale = toggledOnScale,
                toggledOffScale = toggledOffScale
            ) {
                onItemToggle(itemIndex)
            }
        }
    }
}