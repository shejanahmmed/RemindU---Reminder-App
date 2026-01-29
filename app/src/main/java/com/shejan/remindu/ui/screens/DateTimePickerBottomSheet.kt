package com.shejan.remindu.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.shejan.remindu.ui.theme.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Bottom Sheet for selecting Date and Time.
 * Functionality:
 * - Dynamic Calendar (java.time)
 * - Scrollable Time Wheels (Hours/Minutes)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePickerBottomSheet(
    onDismissRequest: () -> Unit,
    onConfirm: (LocalDateTime) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    
    // State for Date and Time
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedTime by remember { mutableStateOf(LocalTime.of(8, 0)) } // Default 08:00 AM

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectVerticalDragGestures { _, _ -> }
                }
                .padding(horizontal = 24.dp)
                .padding(bottom = 40.dp)
        ) {
            BottomSheetHeader(title = "Select Date & Time")

            MonthHeader(
                currentMonthValues = selectedDate,
                onPreviousMonth = { selectedDate = selectedDate.minusMonths(1) },
                onNextMonth = { selectedDate = selectedDate.plusMonths(1) }
            )

            CalendarGrid(
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it }
            )

            // TimeSelectionSection removed as per request

            Spacer(modifier = Modifier.height(16.dp))

            DigitalTimePickerWheel(
                selectedTime = selectedTime,
                onTimeChange = { selectedTime = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            ConfirmButton(
                onConfirm = { onConfirm(LocalDateTime.of(selectedDate, selectedTime)) }
            )
        }
    }
}

@Composable
private fun BottomSheetHeader(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun MonthHeader(
    currentMonthValues: LocalDate,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousMonth) {
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowLeft,
                contentDescription = "Previous Month",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Text(
            text = currentMonthValues.format(formatter),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        IconButton(onClick = onNextMonth) {
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowRight,
                contentDescription = "Next Month",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun CalendarGrid(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val weekDays = listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa")
    
    // Calculate calendar days
    val yearMonth = YearMonth.from(selectedDate)
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstOfMonth = selectedDate.withDayOfMonth(1)
    
    // Calculate empty header cells
    val emptyCells = if (firstOfMonth.dayOfWeek.value == 7) 0 else firstOfMonth.dayOfWeek.value
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Weekday Header Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            weekDays.forEach { day ->
                Text(
                    text = day,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        // 2. Days Rows
        val totalSlots = emptyCells + daysInMonth
        val rows = (totalSlots + 6) / 7 // ceil(totalSlots / 7)
        
        for (i in 0 until rows) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                for (j in 0 until 7) {
                    val index = (i * 7) + j
                    if (index < emptyCells) {
                        Box(modifier = Modifier.weight(1f).aspectRatio(1f))
                    } else if (index < totalSlots) {
                        val dayNumber = index - emptyCells + 1
                        val date = selectedDate.withDayOfMonth(dayNumber)
                        val isSelected = date == selectedDate
                        
                        Box(modifier = Modifier.weight(1f)) {
                            CalendarDayItem(
                                dayNumber = dayNumber,
                                isSelected = isSelected,
                                onClick = { onDateSelected(date) }
                            )
                        }
                    } else {
                        Box(modifier = Modifier.weight(1f).aspectRatio(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarDayItem(
    dayNumber: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) PrimaryColor.copy(alpha = 0.1f) else Color.Transparent)
            .border(
                width = if (isSelected) 1.dp else 0.dp,
                color = if (isSelected) PrimaryColor.copy(alpha = 0.2f) else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
    ) {
        Text(
            text = dayNumber.toString(),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) PrimaryColor else MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun DigitalTimePickerWheel(
    selectedTime: LocalTime,
    onTimeChange: (LocalTime) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp) // Reduced height to show only 1 prev/next number (total 3 items)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Hour Wheel
        // Use 12-hour format internally for display (1-12)
        // Map 0 -> 12, 1-11 -> 1-11, 12 -> 12, 13-23 -> 1-11
        val currentHour12 = if(selectedTime.hour % 12 == 0) 12 else selectedTime.hour % 12
        
        WheelPicker(
            amount = 12,
            initialIndex = currentHour12 - 1, // 0-indexed, so 1->0, 12->11
            onValueChange = { index ->
                // index is 0..11 corresponding to 1..12
                val newHour12 = index + 1
                // Convert back to 24h based on current AM/PM
                val isAm = selectedTime.hour < 12
                val newHour24 = if (isAm) {
                    if (newHour12 == 12) 0 else newHour12
                } else {
                    if (newHour12 == 12) 12 else newHour12 + 12
                }
                onTimeChange(selectedTime.withHour(newHour24))
            }
        ) { index ->
            // Display value: 1..12
            // Pad start
            (index + 1).toString().padStart(2, '0')
        }

        Text(
            text = " : ",
            color = PrimaryColor,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // Minute Wheel
        WheelPicker(
            amount = 60,
            initialIndex = selectedTime.minute,
            onValueChange = { index ->
                onTimeChange(selectedTime.withMinute(index))
            }
        ) { index ->
            index.toString().padStart(2, '0')
        }

        Spacer(modifier = Modifier.width(32.dp))

        AmPmToggle(
            isAm = selectedTime.hour < 12,
            onToggle = { isAm ->
                val currentHour = selectedTime.hour
                val newHour = if (isAm) {
                    // Switch to AM
                    if (currentHour >= 12) currentHour - 12 else currentHour
                } else {
                    // Switch to PM
                    if (currentHour < 12) currentHour + 12 else currentHour
                }
                onTimeChange(selectedTime.withHour(newHour))
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun WheelPicker(
    amount: Int,
    initialIndex: Int,
    onValueChange: (Int) -> Unit,
    label: (Int) -> String
) {
    // Large number for infinite scroll feel
    val infiniteCount = 1000 * amount 
    val initialPage = infiniteCount / 2 + initialIndex
    
    val pagerState = rememberPagerState(initialPage = initialPage) { infiniteCount }
    
    // Effect to report changes
    LaunchedEffect(pagerState.currentPage) {
        val actualIndex = pagerState.currentPage % amount
        // Avoid potentially redundant updates or verify logic
        // For simplicity, we just trigger. The parent handles state.
        onValueChange(actualIndex)
    }

    // Item height
    val itemHeight = 36.dp
    // Calculate padding to center the item
    // Container height is 110dp. Center is 55dp.
    // Item half height is 18dp. Top should be 55-18=37dp.
    val paddingY = 37.dp

    VerticalPager(
        state = pagerState,
        modifier = Modifier
            .width(50.dp)
            .fillMaxHeight(),
        contentPadding = PaddingValues(vertical = paddingY)
    ) { page ->
        val actualIndex = page % amount
        val isSelected = page == pagerState.currentPage
        
        // Calculate distance for smooth scaling
        // (Using simple isSelected check as requested for clarity, or precise calculation)
        // User asked "make those upcming number more small".
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight)
                .graphicsLayer {
                    val scale = if (isSelected) 1.2f else 0.8f
                    scaleX = scale
                    scaleY = scale
                    alpha = if (isSelected) 1f else 0.3f
                    translationY = if (isSelected) 0f else 0f 
                },
            contentAlignment = Alignment.Center
        ) {
             Text(
                text = label(actualIndex),
                color = if (isSelected) PrimaryColor else MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}

@Composable
private fun AmPmToggle(
    isAm: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(4.dp)
    ) {
        AmPmOption(text = "AM", isSelected = isAm, onClick = { onToggle(true) })
        AmPmOption(text = "PM", isSelected = !isAm, onClick = { onToggle(false) })
    }
}

@Composable
private fun AmPmOption(
    text: String, 
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) PrimaryColor.copy(alpha = 0.1f) else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp) // Adjusted padding (intermediate size)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall, // Adjusted text size
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) PrimaryColor else MaterialTheme.colorScheme.onTertiary
        )
    }
}

@Composable
private fun ConfirmButton(onConfirm: () -> Unit) {
    Button(
        onClick = onConfirm,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(8.dp, CircleShape, spotColor = PrimaryColor.copy(alpha = 0.5f)),
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryColor
        ),
        shape = CircleShape
    ) {
        Text(
            text = "Confirm Selection",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}
