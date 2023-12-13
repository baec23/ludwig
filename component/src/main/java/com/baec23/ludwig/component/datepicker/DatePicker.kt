package com.baec23.ludwig.component.datepicker

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.TextStyle

data class DatePickerScope(
    val todayDate: LocalDate,
    val selectedDate: LocalDate,
    val viewingDate: LocalDate,
    val setSelectedDate: (selectedDate: LocalDate) -> Unit,
    val setViewingDate: (viewingDate: LocalDate) -> Unit,
    val finalizeSelection: () -> Unit,
    val cancelSelection: () -> Unit,
)

@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    initialDate: LocalDate? = null,
    containerShape: Shape = RoundedCornerShape(
        topStart = 16.dp, topEnd = 16.dp, bottomStart = 12.dp, bottomEnd = 12.dp
    ),
    containerColors: CardColors = CardDefaults.cardColors(),
    containerElevation: CardElevation = CardDefaults.cardElevation(),
    containerBorder: BorderStroke? = null,
    onSelectedDateChanged: ((LocalDate) -> Unit)? = null,
    onViewingDateChanged: ((LocalDate) -> Unit)? = null,
    shouldFinalizeOnSelect: Boolean = false,
    header: (@Composable DatePickerScope.() -> Unit)? = { DefaultHeader() },
    footer: (@Composable DatePickerScope.() -> Unit)? = { DefaultFooter() },
    content: @Composable DatePickerScope.() -> Unit = { DefaultContent() },
    onCancelled: () -> Unit,
    onDateSelectionFinalized: (LocalDate) -> Unit,
) {
    val todayDate = LocalDate.now()
    var startDate = todayDate
    initialDate?.let { startDate = initialDate }
    var selectedDate by rememberSaveable { mutableStateOf(startDate) }
    var viewingDate by rememberSaveable { mutableStateOf(startDate) }

    val scope = DatePickerScope(
        todayDate = todayDate,
        selectedDate = selectedDate,
        viewingDate = viewingDate,
        setSelectedDate = { date ->
            selectedDate = date
            onSelectedDateChanged?.let { it(date) }
            if (shouldFinalizeOnSelect)
                onDateSelectionFinalized(selectedDate)
        },
        setViewingDate = { selectedViewingDate ->
            viewingDate = selectedViewingDate
            onViewingDateChanged?.let { it(selectedViewingDate) }
        },
        finalizeSelection = {
            onDateSelectionFinalized(selectedDate)
        },
        cancelSelection = onCancelled
    )

    Card(
        modifier = modifier,
        shape = containerShape,
        colors = containerColors,
        elevation = containerElevation,
        border = containerBorder
    ) {
        Column(modifier = modifier) {
            header?.let { header(scope) }
            content(scope)
            footer?.let { footer(scope) }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun DatePickerScope.DefaultHeader(
) {
    val locale = LocalContext.current.resources.configuration.locales[0]

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Select date", style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.height(36.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            AnimatedContent(targetState = selectedDate, label = "datePicker") { sDate ->
                val dayOfWeek = sDate.dayOfWeek.getDisplayName(TextStyle.SHORT, locale)
                val month = sDate.month.getDisplayName(TextStyle.SHORT, locale)
                val dayOfMonth = sDate.dayOfMonth
                Text(
                    text = "$dayOfWeek, $month $dayOfMonth",
                    style = MaterialTheme.typography.headlineLarge
                )
            }
//            IconButton(onClick = {}) {
//                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Date")
//            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Divider()
    }
}

@Composable
private fun DatePickerScope.DefaultFooter() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp), horizontalArrangement = Arrangement.End
    ) {
        Row {
            TextButton(onClick = cancelSelection) {
                Text("Cancel")
            }
            Spacer(modifier = Modifier.width(16.dp))
            TextButton(onClick = finalizeSelection) {
                Text("OK")
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun DatePickerScope.DefaultContent() {
    var isYearSelectionActive by rememberSaveable { mutableStateOf(false) }
    val selectableYears = remember {
        mutableListOf(1900).apply {
            for (i in 1901..2100) {
                this.add(i)
            }
        }
    }
    val gridState = rememberLazyGridState()

    LaunchedEffect(true) {
        val currentYearIndex = selectableYears.indexOf(todayDate.year)
        gridState.animateScrollToItem(currentYearIndex)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        DatePickerCalendarControls(viewingDate = viewingDate,
            isYearSelectionActive = isYearSelectionActive,
            onYearSelectionClick = { isYearSelectionActive = !isYearSelectionActive },
            onPrevMonthClick = {
                setViewingDate(viewingDate.minusMonths(1))
            },
            onNextMonthClick = {
                setViewingDate(viewingDate.plusMonths(1))
            })

        Column(
            modifier = Modifier
                .height(274.dp)
                .padding(horizontal = 12.dp)
        ) {
            AnimatedVisibility(
                visible = isYearSelectionActive,
            ) {
                DatePickerYearSelector(gridState = gridState,
                    selectableYears = selectableYears.toList(),
                    selectedYear = viewingDate.year,
                    onYearSelect = {
                        setViewingDate(viewingDate.withYear(it))
                        isYearSelectionActive = false
                    })
            }
            AnimatedVisibility(
                visible = !isYearSelectionActive
            ) {
                AnimatedContent(targetState = viewingDate, transitionSpec = {
                    if (targetState > initialState) {
                        slideInHorizontally { fullWidth -> fullWidth } with slideOutHorizontally { fullWidth -> -fullWidth } + fadeOut()
                    } else {
                        slideInHorizontally { fullWidth -> -fullWidth } with slideOutHorizontally { fullWidth -> fullWidth } + fadeOut()
                    }
                }, label = "yearSelection") { vDate ->
                    DatePickerCalendar(selectedDate = selectedDate, viewingDate = vDate) {
                        setSelectedDate(it)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun DatePickerCalendarControls(
    viewingDate: LocalDate,
    isYearSelectionActive: Boolean,
    onYearSelectionClick: () -> Unit,
    onPrevMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit,
) {
    val locale = LocalContext.current.resources.configuration.locales[0]


    val arrowIconRotation by animateFloatAsState(targetValue = if (isYearSelectionActive) 180f else 360f,
        label = "arrowIconRotation"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.wrapContentSize(Alignment.CenterStart)
        ) {
            Row(
                modifier = Modifier.clickable { onYearSelectionClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(12.dp))
                AnimatedContent(targetState = viewingDate, transitionSpec = {
                    if (targetState > initialState) {
                        slideInHorizontally { fullWidth -> fullWidth } + fadeIn() with slideOutHorizontally { fullWidth -> -fullWidth } + fadeOut()
                    } else {
                        slideInHorizontally { fullWidth -> -fullWidth } + fadeIn() with slideOutHorizontally { fullWidth -> fullWidth } + fadeOut()
                    }
                }, label = "datePickerControls") { vDate ->
                    val month = vDate.month.getDisplayName(TextStyle.FULL, locale)
                    val year = vDate.year
                    Text(text = "$month $year", style = MaterialTheme.typography.labelLarge)
                }
                Spacer(modifier = Modifier.width(5.dp))
                Icon(
                    modifier = Modifier
                        .rotate(arrowIconRotation)
                        .size(24.dp),
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Select Month"
                )
            }
        }
        AnimatedVisibility(
            visible = !isYearSelectionActive, enter = fadeIn(), exit = fadeOut()
        ) {
            Row {
                IconButton(
                    modifier = Modifier
                        .height(24.dp)
                        .width(40.dp),
                    onClick = { onPrevMonthClick() }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Previous Month"
                    )
                }
                IconButton(
                    modifier = Modifier
                        .height(24.dp)
                        .width(40.dp),
                    onClick = { onNextMonthClick() }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Next Month"
                    )
                }
            }
        }
    }
}

@Composable
private fun DatePickerCalendar(
    selectedDate: LocalDate,
    viewingDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
) {
    val currentMonth = viewingDate.month
    val firstDayOfMonth = viewingDate.withDayOfMonth(1).dayOfWeek
    val numDaysInMonth = currentMonth.length(isLeapYear(viewingDate))
    val displayOffset = if (firstDayOfMonth.value == 7) 0 else firstDayOfMonth.value

    val headerLabels = listOf("Su", "M", "T", "W", "Th", "F", "S")

    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        columns = GridCells.Fixed(7),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        items(7 + displayOffset + numDaysInMonth) {
            Box(modifier = Modifier.size(40.dp), contentAlignment = Alignment.Center) {
                when {
                    //Header Labels
                    it < headerLabels.size -> {
                        Text(
                            text = headerLabels[it],
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    //Prev Month Days
                    it < headerLabels.size + displayOffset -> {
                        Box(modifier = Modifier.size(1.dp)) {}
                    }

                    //Curr Month Days
                    else -> {
                        val day = it - headerLabels.size - displayOffset + 1
                        DatePickerCalendarButton(
                            day = day,
                            isSelected = day == selectedDate.dayOfMonth && viewingDate.month == selectedDate.month && viewingDate.year == selectedDate.year,
                            isToday = day == LocalDate.now().dayOfMonth && viewingDate.month == LocalDate.now().month && viewingDate.year == LocalDate.now().year
                        ) {
                            onDateSelected(viewingDate.withDayOfMonth(day))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DatePickerYearSelector(
    gridState: LazyGridState,
    selectableYears: List<Int>,
    selectedYear: Int,
    onYearSelect: (Int) -> Unit,
) {
    val presentYear = LocalDate.now().year

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        state = gridState,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(selectableYears.size) {
            val currItemYear = selectableYears[it]
            DatePickerSelectableYear(
                year = currItemYear,
                isSelected = currItemYear == selectedYear,
                isPresentYear = presentYear == currItemYear
            ) {
                onYearSelect(currItemYear)
            }
        }
    }
}

@Composable
private fun DatePickerSelectableYear(
    year: Int,
    isSelected: Boolean,
    isPresentYear: Boolean,
    onYearSelect: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(modifier = Modifier
            .width(72.dp)
            .height(36.dp)
            .clip(CircleShape)
            .run {
                when {
                    isSelected -> this.background(MaterialTheme.colorScheme.primary)
                    isPresentYear -> this.border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )

                    else -> this
                }
            }
            .clickable { onYearSelect() }) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = year.toString(),
                textAlign = TextAlign.Center,
                color = when {
                    isSelected -> MaterialTheme.colorScheme.onPrimary
                    isPresentYear -> MaterialTheme.colorScheme.primary
                    else -> Color.Unspecified
                },
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun DatePickerCalendarButton(
    day: Int,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: () -> Unit,
) {
    Box(modifier = Modifier
        .aspectRatio(1f)
        .clip(CircleShape)
        .run {
            if (isSelected) {
                this.background(MaterialTheme.colorScheme.primary)
            } else if (isToday) {
                this.border(
                    width = 1.dp, color = MaterialTheme.colorScheme.primary, shape = CircleShape
                )
            } else {
                this
            }
        }
        .clickable {
            onClick()
        }) {
        Text(
            modifier = Modifier.align(Alignment.Center), text = day.toString(), color = when {
                isSelected -> MaterialTheme.colorScheme.onPrimary
                isToday -> MaterialTheme.colorScheme.primary
                else -> Color.Unspecified
            }, style = MaterialTheme.typography.bodySmall
        )
    }
}


private fun isLeapYear(date: LocalDate): Boolean {
    val year = date.year
    return year % 400 == 0 || (year % 100 != 0 && year % 4 == 0)
}