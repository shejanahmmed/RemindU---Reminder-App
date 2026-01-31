package com.shejan.remindu.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.animation.core.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shejan.remindu.ui.theme.*

import androidx.lifecycle.viewmodel.compose.viewModel
import com.shejan.remindu.ui.viewmodels.RemindUViewModel
import java.time.format.DateTimeFormatter
import java.time.LocalDate

import java.time.DayOfWeek
import java.time.temporal.TemporalAdjusters
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HomeScreen(
    onFabClick: () -> Unit,
    viewModel: RemindUViewModel = viewModel()
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        // Removed bottomBar from Scaffold to avoid solid background area
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // State for selected date
            var selectedDate by remember { mutableStateOf(LocalDate.now()) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                ProfileHeader()
                Spacer(modifier = Modifier.height(32.dp))
                CircularProgressSection()
                Spacer(modifier = Modifier.height(32.dp))
                CalendarSection(
                    selectedDate = selectedDate,
                    onDateSelected = { selectedDate = it }
                )
                Spacer(modifier = Modifier.height(32.dp))
                TimelineSection(viewModel, selectedDate)
                Spacer(modifier = Modifier.height(100.dp)) // Extra space for scrolling above FAB
            }
        }
    }
}

@Composable
fun ProfileHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Hello, Alex \uD83D\uDC4B",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiary
            )
            Text(
                text = "Let's be productive!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Transparent)
            ) {
                BadgedBox(
                    badge = {
                        Badge(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(8.dp)
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Notifications,
                        contentDescription = "Notifications",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            // Profile Image Placeholder
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp,
                border = androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.surface)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                         imageVector = Icons.Rounded.Person,
                         contentDescription = "Profile",
                         tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun CircularProgressSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(200.dp)
        ) {
            Canvas(modifier = Modifier.size(160.dp)) {
                // Background Circle
                drawCircle(
                    color = Color.LightGray.copy(alpha = 0.2f),
                    style = Stroke(width = 30f)
                )
                // Progress Arc (75%)
                drawArc(
                    color = PrimaryColor,
                    startAngle = -90f,
                    sweepAngle = 270f, // 75% of 360
                    useCenter = false,
                    style = Stroke(width = 30f, cap = StrokeCap.Round)
                )
            }
            
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "75%",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "DONE",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiary,
                    letterSpacing = 1.sp
                )
            }

            // Floating "On Track" Badge
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = 10.dp, y = (-10).dp)
                    .background(SuccessColor.copy(alpha = 0.3f), RoundedCornerShape(50))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "On Track",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF2E7D32), // Darker green for text
                    fontWeight = FontWeight.Bold
                )
            }
            
             // Floating Trophy Icon
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 0.dp, y = 10.dp)
                    .background(TertiaryColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .padding(8.dp)
            ) {
                Icon(
                     imageVector = Icons.Rounded.Star,
                     contentDescription = "Trophy",
                     tint = PrimaryColor,
                     modifier = Modifier.size(20.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "3 of 4 tasks completed today",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onTertiary
        )
    }
}

@Composable
fun CalendarSection(selectedDate: LocalDate, onDateSelected: (LocalDate) -> Unit) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            val monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
            Text(
                text = selectedDate.format(monthFormatter),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Today",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onDateSelected(LocalDate.now()) }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        
        // Generate current week or a rolling window
        val startOfWindow = LocalDate.now().minusDays(1) // Start from yesterday? Or Monday? Let's show Today - 2 to Today + 4
        val dates = (0..6).map { startOfWindow.plusDays(it.toLong()) }
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
             contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(dates) { date ->
                val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                val dayNumber = date.dayOfMonth.toString()
                val isSelected = date == selectedDate
                
                CalendarDayItem(
                    day = dayName, 
                    date = dayNumber, 
                    isSelected = isSelected,
                    onClick = { onDateSelected(date) }
                )
            }
        }
    }
}

@Composable
fun CalendarDayItem(day: String, date: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onBackground
    val subTextColor = if (isSelected) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onTertiary
    
    Column(
        modifier = Modifier
            .width(56.dp)
            .height(80.dp)
            .shadow(
                elevation = if(isSelected) 8.dp else 1.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = if(isSelected) MaterialTheme.colorScheme.primary else Color.Black
            )
            .background(backgroundColor, RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = if (isSelected) Color.Transparent else Color.LightGray.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = day,
            style = MaterialTheme.typography.labelSmall,
            color = subTextColor
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = date,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = contentColor
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Dot indicator
        if (date != "18") { // Simulate some have dots
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) Color.White else Color.Green) // Green dot for others
            )
        }
    }
}

@Composable
fun TimelineSection(viewModel: RemindUViewModel, selectedDate: LocalDate) {
    val reminders = viewModel.reminders
    val selectedReminders = reminders.filter { it.dateTime.toLocalDate() == selectedDate }
    
    Column {
        Text(
            text = if (selectedDate == LocalDate.now()) "Today's Timeline" else "Timeline for ${selectedDate.format(DateTimeFormatter.ofPattern("MMM d"))}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        if (selectedReminders.isEmpty()) {
             Box(
                 modifier = Modifier.fillMaxWidth().padding(32.dp),
                 contentAlignment = Alignment.Center
             ) {
                 Text(
                     text = "No tasks for today ✨",
                     style = MaterialTheme.typography.bodyLarge,
                     color = MaterialTheme.colorScheme.onTertiary
                 )
             }
        } else {
            Box {
                // Vertical Line
                Box(
                     modifier = Modifier
                        .matchParentSize() 
                ) {
                    Box(
                        modifier = Modifier
                            .padding(start = 19.dp)
                            .width(2.dp)
                            .fillMaxHeight()
                            .background(Color.LightGray.copy(alpha = 0.5f))
                    )
                }
                
                Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")
                    
                    selectedReminders.forEachIndexed { index, reminder ->
                         TimelineItem(
                            title = reminder.title,
                            subtitle = reminder.description.ifBlank { reminder.category?.name ?: "Reminder" },
                            time = reminder.dateTime.format(timeFormatter),
                            icon = reminder.category?.icon ?: Icons.Rounded.Task,
                            iconBgColor = reminder.category?.color?.copy(alpha=0.1f) ?: Color(0xFFE8F5E9),
                            iconTint = reminder.category?.color ?: Color(0xFF2E7D32),
                            isCompleted = reminder.isCompleted,
                            isHighLight = false // Keep standard for now
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun TimelineItem(
    title: String,
    subtitle: String,
    time: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconBgColor: Color,
    iconTint: Color,
    isCompleted: Boolean = false,
    isHighLight: Boolean = false,
    avatars: Boolean = false
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        // Timeline Node
        Box(
            modifier = Modifier
                .width(40.dp), // Container for dot
            contentAlignment = Alignment.Center
        ) {
            if (isHighLight) {
                 Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(PrimaryColor)
                        .border(4.dp, PrimaryColor.copy(alpha = 0.3f), CircleShape)
                ) {
                    Box(modifier = Modifier.size(8.dp).background(Color.White, CircleShape).align(Alignment.Center))
                }
            } else if (isCompleted) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.background)
                        .border(2.dp, Color.Green, CircleShape)
                ) {
                     Box(modifier = Modifier.size(10.dp).background(Color.Green, CircleShape).align(Alignment.Center))
                }
            } else {
                 Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.background)
                        .border(2.dp, Color.LightGray, CircleShape)
                )
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        // Card content
        val cardBg = if (isHighLight) 
            androidx.compose.ui.graphics.Brush.linearGradient(listOf(PrimaryColor, Color(0xFF757ce8)))
            else androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.surface) // Keep surface for readability, but check if we should remove shadow
            
        // Actually, let's make non-highlighted transparent as suspected
        val actualCardBg = if (isHighLight) 
             androidx.compose.ui.graphics.Brush.linearGradient(listOf(PrimaryColor, Color(0xFF757ce8)))
             else androidx.compose.ui.graphics.SolidColor(Color.White)
             
        val textColor = if (isHighLight) Color.White else MaterialTheme.colorScheme.onBackground
        val subTextColor = if (isHighLight) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onTertiary
        
        Card(
             modifier = Modifier
                 .fillMaxWidth()
                 .shadow(
                     elevation = if(isHighLight) 10.dp else 0.dp, // No shadow for others
                     shape = RoundedCornerShape(24.dp),
                     spotColor = if(isHighLight) PrimaryColor else Color.Transparent
                 ),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent) 
        ) {
            Box(
                modifier = Modifier
                    .background(actualCardBg) // Use the BG
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(iconBgColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = icon, contentDescription = null, tint = iconTint)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = textColor,
                                textDecoration = if (isCompleted && !isHighLight) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                            )
                            Text(
                                text = subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = subTextColor
                            )
                        }
                    }
                    
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = time,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isHighLight) Color.White else if (isCompleted) Color.Green else MaterialTheme.colorScheme.onTertiary
                        )
                        if (avatars) {
                             Spacer(modifier = Modifier.height(8.dp))
                             // Placeholder for avatars
                             Row(horizontalArrangement = Arrangement.spacedBy((-8).dp)) {
                                 Surface(modifier = Modifier.size(24.dp), shape = CircleShape, border = androidx.compose.foundation.BorderStroke(1.dp, Color.White), color = Color.Gray) {}
                                 Surface(modifier = Modifier.size(24.dp), shape = CircleShape, border = androidx.compose.foundation.BorderStroke(1.dp, Color.White), color = Color.DarkGray) {}
                                 Box(modifier = Modifier.size(24.dp).background(Color.White.copy(alpha = 0.3f), CircleShape).border(1.dp, Color.White, CircleShape), contentAlignment = Alignment.Center) {
                                     Text("+2", style = MaterialTheme.typography.labelSmall, fontSize = 8.sp, color = Color.White)
                                 }
                             }
                        } else if (isCompleted) {
                            Icon(imageVector = Icons.Rounded.CheckCircle, contentDescription = "Completed", tint = Color.Green, modifier = Modifier.padding(top = 4.dp).size(20.dp))
                        }
                    }
                }
            }
        }
    }
}





@Composable
fun BottomNavigationBar(selectedItem: String, onItemSelected: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 24.dp)
            .height(100.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Background Pill (Surface)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .shadow(10.dp, RoundedCornerShape(50), spotColor = Color.Black.copy(alpha = 0.1f)),
            shape = RoundedCornerShape(50),
            color = WarmOffWhite.copy(alpha = 0.9f)
        ) {
            // Empty content, just for background visuals
        }

        // Content Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(64.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home Item
            NavItem(
                label = "Home",
                icon = Icons.Rounded.Home,
                isSelected = selectedItem == "Home",
                onClick = { onItemSelected("Home") }
            )
            
            // History Item
            NavItem(
                label = "History",
                icon = Icons.Rounded.Refresh,
                isSelected = selectedItem == "History",
                onClick = { onItemSelected("History") }
            )
            
            // Add Item
            NavItem(
                label = "Add",
                icon = Icons.Rounded.Add,
                isSelected = selectedItem == "Add",
                onClick = { onItemSelected("Add") }
            )
            
            // Dashboard Item
            NavItem(
                label = "Dashboard",
                icon = Icons.Rounded.Menu,
                isSelected = selectedItem == "Dashboard",
                onClick = { onItemSelected("Dashboard") }
            )
            
            // Settings Item
            NavItem(
                label = "Settings",
                icon = Icons.Rounded.Settings,
                isSelected = selectedItem == "Settings",
                onClick = { onItemSelected("Settings") }
            )
        }
    }
}

@Composable
fun NavItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // Animation states
    val offsetY by animateDpAsState(
        targetValue = if (isSelected) (-20).dp else 0.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )
    
    val size by animateDpAsState(
        targetValue = if (isSelected) 56.dp else 24.dp,
        animationSpec = tween(300)
    )
    
    val containerColor = if (isSelected) MatteTerracotta else Color.Transparent
    val iconColor = if (isSelected) Color.White else CharcoalBrown
    val shadowElevation = if (isSelected) 8.dp else 0.dp

    Box(
        modifier = Modifier
            .graphicsLayer {
                translationY = offsetY.toPx()
            }
            .size(size) // Animate size from icon size to FAB size
            .shadow(shadowElevation, CircleShape, spotColor = MatteTerracotta.copy(alpha = 0.5f))
            .clip(CircleShape)
            .background(containerColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
         // If selected, we want a larger container but standard icon size inside
         // If not selected, it's just the icon
         
         Icon(
             imageVector = icon, 
             contentDescription = label, 
             tint = iconColor,
             modifier = Modifier.size(24.dp)
         )
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    RemindUTheme {
        HomeScreen(onFabClick = {})
    }
}
