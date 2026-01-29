package com.shejan.remindu.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shejan.remindu.ui.theme.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun CreateReminderScreen(onBackClick: () -> Unit) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedDateTime by remember { mutableStateOf<LocalDateTime?>(null) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            CreateHeader()
            Spacer(modifier = Modifier.height(24.dp))

            TitleSection()
            Spacer(modifier = Modifier.height(32.dp))
            InputSection()
            Spacer(modifier = Modifier.height(32.dp))
            CategorySection()
            Spacer(modifier = Modifier.height(32.dp))
            DateTimeSection(
                selectedDateTime = selectedDateTime,
                onCick = { showBottomSheet = true }
            )
            Spacer(modifier = Modifier.height(32.dp))
            ReminderTypeSection()
            Spacer(modifier = Modifier.height(32.dp))
            RepeatSection()
            Spacer(modifier = Modifier.height(100.dp))
        }
        
        if (showBottomSheet) {
            DateTimePickerBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                onConfirm = { 
                    selectedDateTime = it
                    showBottomSheet = false 
                }
            )
        }
    }
}

@Composable
fun CreateHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight() // Allow height to expand for text
            .padding(vertical = 8.dp), // Add some breathing room
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "New Reminder",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Stay organized & never miss a beat.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onTertiary
            )
        }

        Box(
            modifier = Modifier
                .height(40.dp)
                .shadow(4.dp, CircleShape, spotColor = PrimaryColor.copy(alpha = 0.5f))
                .clip(CircleShape)
                .background(
                    Brush.horizontalGradient(listOf(PrimaryColor, Color(0xFF7C41F2)))
                )
                .clickable { /* TODO: Save Logic */ }
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
             Text(
                 text = "Save",
                 style = MaterialTheme.typography.labelLarge,
                 fontWeight = FontWeight.Bold,
                 color = Color.White
             )
        }
    }
}



@Composable
fun TitleSection() {
    Column {
        Text(
            text = "Reminder Title",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp, bottom = 12.dp),
             color = MaterialTheme.colorScheme.onBackground
        )
        
        TextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Add a title", color = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.5f)) },
            modifier = Modifier
                .fillMaxWidth()
                .shadow(2.dp, RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(24.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(24.dp),
            singleLine = true
        )
    }
}

@Composable
fun InputSection() {
    Column {
        Text(
            text = "What do you want to remember?",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp, bottom = 12.dp),
             color = MaterialTheme.colorScheme.onBackground
        )
        
        TextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Type here ...", color = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.5f)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .shadow(2.dp, RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(24.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(24.dp)
        )
    }
}

@Composable
fun CategorySection() {
    Column {
        Text(
            text = "Category",
             style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp, bottom = 12.dp),
             color = MaterialTheme.colorScheme.onBackground
        )
        
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { CategoryChip(label = "Study", icon = Icons.Rounded.Edit, isSelected = false, color = MatteBlue) }
            item { CategoryChip(label = "Work", icon = Icons.Rounded.Email, isSelected = false, color = MattePeach) }
            item { CategoryChip(label = "Health", icon = Icons.Rounded.Favorite, isSelected = false, color = MatteMint) }
            
            item {
                Surface(
                    color = SecondaryColor,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .height(44.dp)
                        .clickable { /* TODO: Add Category Logic */ }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "Add Category",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Add",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryChip(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isSelected: Boolean, color: Color) {
    val bgColor = if (isSelected) color else color
    val contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onBackground
    val iconColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onBackground
    
    Surface(
        color = bgColor,
        shape = RoundedCornerShape(50),
        modifier = Modifier.height(44.dp).let { if(isSelected) it.shadow(8.dp, RoundedCornerShape(50), spotColor = color) else it }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, color = contentColor)
        }
    }
}


@Composable
fun DateTimeSection(
    selectedDateTime: LocalDateTime?,
    onCick: () -> Unit
) {
    Column {
        Text(
            text = "Date & Time",
             style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp, bottom = 12.dp),
             color = MaterialTheme.colorScheme.onBackground
        )
        
        // Pick Date & Time Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                   val stroke = Stroke(
                       width = 4f,
                       pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 0f)
                   )
                   drawRoundRect(
                       color = PrimaryColor.copy(alpha = 0.5f),
                       style = stroke,
                       cornerRadius = CornerRadius(24.dp.toPx())
                   )
                }
                .background(MatteLavender, RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .clickable { onCick() }
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Icon Circle
                Surface(
                    shape = CircleShape, 
                    color = Color.White, 
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                         Icon(
                             imageVector = Icons.Rounded.DateRange, 
                             contentDescription = "Calendar",
                             tint = PrimaryColor,
                             modifier = Modifier.size(28.dp)
                         )
                    }
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                     Text(
                         text = selectedDateTime?.format(DateTimeFormatter.ofPattern("MMM dd, hh:mm a")) ?: "Pick Date & Time", 
                         style = MaterialTheme.typography.bodyLarge, 
                         color = PrimaryColor, 
                         fontWeight = FontWeight.Bold
                     )
                     Text(
                         text = if (selectedDateTime != null) "Time set" else "Customize your schedule", 
                         style = MaterialTheme.typography.bodyMedium, 
                         color = PrimaryColor.copy(alpha = 0.6f)
                     )
                }
                
                // Trailing Icons
                Icon(
                    imageVector = Icons.Rounded.DateRange, 
                    contentDescription = null, 
                    tint = PrimaryColor.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    imageVector = Icons.Rounded.Notifications, 
                    contentDescription = null, 
                    tint = PrimaryColor.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Today Card
            DateTimeCard(
                title = "TODAY",
                time = "6:00 PM",
                isSelected = true,
                modifier = Modifier.weight(1f)
            )
             // Tomorrow Card
            DateTimeCard(
                title = "TOMORROW",
                time = "9:00 AM",
                isSelected = false,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun DateTimeCard(title: String, time: String, isSelected: Boolean, modifier: Modifier = Modifier) {
    val borderColor = if (isSelected) PrimaryColor.copy(alpha = 0.4f) else Color.Transparent
    val titleColor = if (isSelected) PrimaryColor else MaterialTheme.colorScheme.onTertiary
    
    Column(
        modifier = modifier
            .shadow(2.dp, RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(24.dp))
            .border(2.dp, borderColor, RoundedCornerShape(24.dp))
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = titleColor,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = time,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun ReminderTypeSection() {
    var selectedType by remember { mutableStateOf("Voice") }

    Column {
        Text(
            text = "Reminder Type",
             style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp, bottom = 12.dp),
             color = MaterialTheme.colorScheme.onBackground
        )
        
        Row(
             modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TypeCard(
                icon = Icons.Rounded.Notifications,
                label = "Notification",
                isSelected = selectedType == "Notification",
                onClick = { selectedType = "Notification" },
                modifier = Modifier.weight(1f)
            )
             TypeCard(
                icon = Icons.Rounded.Warning,
                label = "Alarm",
                isSelected = selectedType == "Alarm",
                onClick = { selectedType = "Alarm" },
                modifier = Modifier.weight(1f)
            )
             TypeCard(
                icon = Icons.Rounded.Person,
                label = "Voice",
                isSelected = selectedType == "Voice",
                onClick = { selectedType = "Voice" },
                modifier = Modifier.weight(1f)
            )
        }
        
        if (selectedType == "Voice") {
            Spacer(modifier = Modifier.height(24.dp))
            VoiceRecordingCard()
        }
    }
}

@Composable
fun TypeCard(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val borderColor = if (isSelected) PrimaryColor.copy(alpha = 0.6f) else Color.Transparent
    val bg = if (isSelected) PrimaryColor.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surface
    val iconBg = if (isSelected) PrimaryColor else MatteLavender
    val iconTint = if (isSelected) Color.White else PrimaryColor
    val textColor = if (isSelected) PrimaryColor else MaterialTheme.colorScheme.onBackground
    
    Column(
        modifier = modifier
            .shadow(if(isSelected) 0.dp else 2.dp, RoundedCornerShape(24.dp))
            .background(bg, RoundedCornerShape(24.dp))
            .border(2.dp, borderColor, RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iconTint)
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}

@Composable
fun VoiceRecordingCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(24.dp))
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Waveform Animation
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.height(32.dp)
        ) {
             val infiniteTransition = rememberInfiniteTransition()
             val heights = (0..8).map { index ->
                 val duration = 500 + (index * 100)
                 infiniteTransition.animateFloat(
                     initialValue = 0.3f,
                     targetValue = 1f,
                     animationSpec = infiniteRepeatable(
                         animation = tween(duration, easing = LinearEasing),
                         repeatMode = RepeatMode.Reverse
                     )
                 )
             }
             
             heights.forEach { heightAnim ->
                 Box(
                     modifier = Modifier
                         .width(4.dp)
                         .fillMaxHeight(heightAnim.value)
                         .clip(RoundedCornerShape(50))
                         .background(PrimaryColor)
                 )
             }
        }
        
        Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
            Text(
                text = "RECORDING...",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = PrimaryColor
            )
            Text(
                text = "Tap to stop or redo",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onTertiary
            )
        }
        
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFEBEE)), // Red-ish bg
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Close, 
                contentDescription = "Stop", 
                tint = Color.Red,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun RepeatSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(24.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.Refresh, contentDescription = "Repeat", tint = MaterialTheme.colorScheme.onTertiary)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Repeat Reminder",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        
        Switch(
            checked = true,
            onCheckedChange = {},
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = PrimaryColor,
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = Color.LightGray
            )
        )
    }
}



