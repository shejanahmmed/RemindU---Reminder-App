package com.shejan.remindu.ui.screens

import androidx.compose.animation.*
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
import androidx.compose.ui.platform.LocalConfiguration
import com.shejan.remindu.ui.viewmodels.CreateReminderViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun CreateReminderScreen(
    onBackClick: () -> Unit,
    viewModel: CreateReminderViewModel = viewModel()
) {
    val showBottomSheet by viewModel.showBottomSheet
    val showCategoryDialog by viewModel.showCategoryDialog
    val editingCategory by viewModel.editingCategory
    val selectedDateTime by viewModel.selectedDateTime
    val reminderTitle by viewModel.reminderTitle
    val reminderDescription by viewModel.reminderDescription
    
    // Categories State
    val categories = viewModel.categories

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            CreateHeader()
            Spacer(modifier = Modifier.height(24.dp))

            TitleSection(
                title = reminderTitle,
                onTitleChange = viewModel::onTitleChange
            )
            Spacer(modifier = Modifier.height(32.dp))
            InputSection(
                description = reminderDescription,
                onDescriptionChange = viewModel::onDescriptionChange
            )
            Spacer(modifier = Modifier.height(32.dp))
            CategorySection(
                categories = categories, 
                onCreateClick = { 
                    viewModel.onEditCategory(null)
                },
                onCategoryClick = { category ->
                    viewModel.onEditCategory(category)
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
            DateTimeSection(
                selectedDateTime = selectedDateTime,
                onCick = { viewModel.showBottomSheet.value = true },
                onSuggestionClick = viewModel::onDateTimeSelected,
                onClear = { viewModel.onDateTimeSelected(null) }
            )
            Spacer(modifier = Modifier.height(32.dp))
            ReminderTypeSection()
            Spacer(modifier = Modifier.height(32.dp))
            RepeatSection()
            Spacer(modifier = Modifier.height(140.dp))
        }
        
        if (showBottomSheet) {
            DateTimePickerBottomSheet(
                onDismissRequest = { viewModel.showBottomSheet.value = false },
                onConfirm = { 
                    viewModel.onDateTimeSelected(it)
                    viewModel.showBottomSheet.value = false 
                }
            )
        }
        
        if (showCategoryDialog) {
            CreateCategoryDialog(
                existingCategory = editingCategory,
                onDismiss = { viewModel.showCategoryDialog.value = false },
                onSaveCategory = { updatedCategory ->
                    if (editingCategory != null) {
                        viewModel.updateCategory(editingCategory!!, updatedCategory)
                    } else {
                        viewModel.addCategory(updatedCategory)
                    }
                    viewModel.showCategoryDialog.value = false
                },
                onRemoveCategory = {
                    if (editingCategory != null) {
                        viewModel.removeCategory(editingCategory!!)
                    }
                    viewModel.showCategoryDialog.value = false
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
            .padding(vertical = 8.dp, horizontal = 24.dp), // Add some breathing room
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
fun TitleSection(title: String, onTitleChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            text = "Reminder Title",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp, bottom = 12.dp),
             color = MaterialTheme.colorScheme.onBackground
        )
        
        TextField(
            value = title,
            onValueChange = onTitleChange,
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
fun InputSection(description: String, onDescriptionChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            text = "What do you want to remember?",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp, bottom = 12.dp),
             color = MaterialTheme.colorScheme.onBackground
        )
        
        TextField(
            value = description,
            onValueChange = onDescriptionChange,
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
fun CategorySection(categories: List<Category>, onCreateClick: () -> Unit, onCategoryClick: (Category) -> Unit) {
    Column {
        Text(
            text = "Category",
             style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 32.dp, bottom = 12.dp),
             color = MaterialTheme.colorScheme.onBackground
        )
        
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                CategoryChip(
                    label = category.name, 
                    icon = category.icon, 
                    isSelected = false, 
                    color = category.color,
                    onClick = { onCategoryClick(category) }
                )
            }
            
            if (categories.isEmpty()) {
                item {
                    Surface(
                        color = SecondaryColor,
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .height(44.dp)
                            .clickable { onCreateClick() }
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
                                text = "Create",
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
}

@Composable
fun CategoryChip(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isSelected: Boolean, color: Color, onClick: () -> Unit) {
    val bgColor = if (isSelected) color else color
    val contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onBackground
    val iconColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onBackground
    
    Surface(
        color = bgColor,
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .height(44.dp)
            .shadow(if(isSelected) 8.dp else 0.dp, RoundedCornerShape(50), spotColor = color)
            .clickable { onClick() }
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
    onCick: () -> Unit,
    onSuggestionClick: (LocalDateTime) -> Unit,
    onClear: () -> Unit
) {
    Column {
        Text(
            text = "Date & Time",
             style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 32.dp, bottom = 12.dp),
             color = MaterialTheme.colorScheme.onBackground
        )
        
        // Pick Date & Time Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
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
                if (selectedDateTime != null) {
                    Surface(
                        onClick = onClear,
                        shape = CircleShape,
                        color = Color(0xFFFFE5E5),
                        border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.1f)),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "Clear",
                                tint = Color.Red,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                } else {
                    Row {
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
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        // Suggestions List (Only show if no date is selected)
        if (selectedDateTime == null) {
            val configuration = LocalConfiguration.current
            val screenWidth = configuration.screenWidthDp.dp
            // Calculate width: (Total Width - Start Padding - Gap - End Padding) / 2
            // 24.dp (Start) + 12.dp (Gap) + 24.dp (End) = 60.dp
            val itemWidth = (screenWidth - 60.dp) / 2

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Today 6 PM (or +1h if late)
                item {
                    val now = LocalDateTime.now()
                    val today6pm = now.withHour(18).withMinute(0)
                    val time = if (now.isAfter(today6pm)) now.plusHours(1) else today6pm
                    
                    DateTimeCard(
                        title = "TODAY",
                        time = time.format(DateTimeFormatter.ofPattern("h:mm a")),
                        isSelected = false,
                        modifier = Modifier
                            .width(itemWidth)
                            .clickable { onSuggestionClick(time) }
                    )
                }
                
                // Tomorrow 9 AM
                item {
                    val tmrw9am = LocalDateTime.now().plusDays(1).withHour(9).withMinute(0)
                    DateTimeCard(
                        title = "TOMORROW",
                        time = "9:00 AM",
                        isSelected = false,
                        modifier = Modifier
                             .width(itemWidth)
                            .clickable { onSuggestionClick(tmrw9am) }
                    )
                }

                // Weekend (Next Saturday 10 AM)
                item {
                    var weekend = LocalDateTime.now().withHour(10).withMinute(0)
                    while (weekend.dayOfWeek != java.time.DayOfWeek.SATURDAY) {
                        weekend = weekend.plusDays(1)
                    }
                    if (weekend.isBefore(LocalDateTime.now())) weekend = weekend.plusWeeks(1)

                    DateTimeCard(
                        title = "WEEKEND",
                        time = weekend.format(DateTimeFormatter.ofPattern("EEE h:mm a")),
                        isSelected = false,
                        modifier = Modifier
                             .width(itemWidth)
                            .clickable { onSuggestionClick(weekend) }
                    )
                }
                
                // Next Week (Next Monday 9 AM)
                item {
                    var nextWeek = LocalDateTime.now().withHour(9).withMinute(0)
                    while (nextWeek.dayOfWeek != java.time.DayOfWeek.MONDAY) {
                         nextWeek = nextWeek.plusDays(1)
                    }
                    if (nextWeek.isBefore(LocalDateTime.now())) nextWeek = nextWeek.plusWeeks(1)
                    
                    DateTimeCard(
                        title = "NEXT WEEK",
                        time = nextWeek.format(DateTimeFormatter.ofPattern("EEE h:mm a")),
                        isSelected = false,
                        modifier = Modifier
                             .width(itemWidth)
                            .clickable { onSuggestionClick(nextWeek) }
                    )
                }
                
                // In 1 Hour
                item {
                    val in1Hour = LocalDateTime.now().plusHours(1)
                    DateTimeCard(
                        title = "IN 1 HOUR",
                        time = in1Hour.format(DateTimeFormatter.ofPattern("h:mm a")),
                        isSelected = false,
                        modifier = Modifier
                             .width(itemWidth)
                            .clickable { onSuggestionClick(in1Hour) }
                    )
                }
            }
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

    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
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
    var isRepeatEnabled by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    // Store selected days (Set of Int: 1=Mon, 7=Sun)
    var selectedDays by remember { mutableStateOf(setOf<Int>()) }

    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        // Toggle Card
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
                Icon(Icons.Rounded.Repeat, contentDescription = "Repeat", tint = MaterialTheme.colorScheme.onTertiary)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Repeat Reminder",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    if (isRepeatEnabled && selectedDays.isNotEmpty()) {
                        Text(
                           text = getRepeatSummary(selectedDays),
                           style = MaterialTheme.typography.labelSmall,
                           color = PrimaryColor
                        )
                    }
                }
            }
            
            Switch(
                checked = isRepeatEnabled,
                onCheckedChange = { 
                    isRepeatEnabled = it 
                    if (it) {
                        showDialog = true
                    } else {
                        selectedDays = emptySet()
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = PrimaryColor,
                    checkedBorderColor = Color.Transparent,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFFE9E9EA),
                    uncheckedBorderColor = Color.Transparent
                )
            )
        }
    }
    
    if (showDialog) {
        RepeatSelectionDialog(
            initialSelection = selectedDays,
            onDismiss = { 
                showDialog = false 
                if (selectedDays.isEmpty()) isRepeatEnabled = false
            },
            onConfirm = { days ->
                selectedDays = days
                showDialog = false
                isRepeatEnabled = days.isNotEmpty()
            }
        )
    }
}

@Composable
fun RepeatSelectionDialog(
    initialSelection: Set<Int>,
    onDismiss: () -> Unit,
    onConfirm: (Set<Int>) -> Unit
) {
    var currentSelection by remember { mutableStateOf(initialSelection) }
    val days = listOf("M", "T", "W", "T", "F", "S", "S") // 1 to 7

    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(28.dp))
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Repeat Every",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Select All Button
                val allSelected = currentSelection.size == 7
                Surface(
                    onClick = {
                        currentSelection = if (allSelected) emptySet() else (1..7).toSet()
                    },
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFFEBE7F3), // Matte Lavender
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.CheckCircle,
                            contentDescription = null,
                            tint = if (allSelected) PrimaryColor else PrimaryColor.copy(alpha = 0.5f),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Select All Days",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryColor
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Days Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    days.forEachIndexed { index, dayLabel ->
                        val dayId = index + 1
                        val isSelected = currentSelection.contains(dayId)
                        val bgColor = if (isSelected) PrimaryColor else Color(0xFFF1F1F5) // Matte Gray
                        val textColor = if (isSelected) Color.White else Color.Gray
                        
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .shadow(if (isSelected) 4.dp else 0.dp, CircleShape, spotColor = PrimaryColor.copy(alpha = 0.3f))
                                .clip(CircleShape)
                                .background(bgColor)
                                .clickable {
                                    currentSelection = if (isSelected) {
                                        currentSelection - dayId
                                    } else {
                                        currentSelection + dayId
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = dayLabel,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Confirm Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(8.dp, CircleShape, spotColor = PrimaryColor.copy(alpha = 0.3f))
                        .clip(CircleShape)
                        .background(
                            Brush.horizontalGradient(
                                listOf(PrimaryColor, Color(0xFF7C41F2))
                            )
                        )
                        .clickable { onConfirm(currentSelection) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Confirm",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

fun getRepeatSummary(days: Set<Int>): String {
    if (days.size == 7) return "Every Day"
    if (days.isEmpty()) return ""
    // Basic formatting: Mon, Wed, Fri
    val dayNames = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun") // 1-based index mapped to 0-6
    return days.sorted().joinToString(", ") { dayNames[it - 1] }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCategoryDialog(
    existingCategory: Category? = null,
    onDismiss: () -> Unit, 
    onSaveCategory: (Category) -> Unit,
    onRemoveCategory: () -> Unit = {}
) {
    var categoryName by remember { mutableStateOf(existingCategory?.name ?: "") }
    var selectedIcon by remember { mutableStateOf(existingCategory?.icon) }
    var selectedColor by remember { mutableStateOf(existingCategory?.color) }
    

    
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(
            usePlatformDefaultWidth = false // Allow full width for custom styling
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f)) // Overlay blur/dim
                .clickable { onDismiss() }, // Click outside to dismiss
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .width(360.dp) // max-w-sm
                    .padding(24.dp)
                    .clickable(enabled = false) {}, // Prevent click through
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp,
                shadowElevation = 0.dp
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (existingCategory != null) "Edit Category" else "New Category",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    
                    if (showError) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Input
                    TextField(
                        value = categoryName,
                        onValueChange = { 
                            categoryName = it 
                            showError = false // Clear error on interaction
                        },
                        placeholder = { Text("Category Name", color = Color.Gray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .border(1.dp, Color.Transparent, RoundedCornerShape(16.dp)),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha=0.5f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha=0.5f),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // ICON Section
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "ICON",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
                        )
                        
                        var isExpanded by remember { mutableStateOf(false) }
                        
                        val allIcons = listOf(
                            // Common Activities
                            Icons.Rounded.FitnessCenter, Icons.Rounded.ShoppingBag, Icons.Rounded.Book, Icons.Rounded.Flight,
                            Icons.Rounded.Palette, Icons.Rounded.Restaurant, Icons.Rounded.Movie,
                            // Work & Study
                            Icons.Rounded.Work, Icons.Rounded.School, Icons.Rounded.Computer, Icons.Rounded.BorderColor,
                            // Personal & Health
                            Icons.Rounded.Favorite, Icons.Rounded.Bedtime, Icons.Rounded.Spa, Icons.Rounded.Medication,
                            // Home & Family
                            Icons.Rounded.Home, Icons.Rounded.ChildCare, Icons.Rounded.Pets, Icons.Rounded.Kitchen,
                            // Finance & shopping
                            Icons.Rounded.AttachMoney, Icons.Rounded.ShoppingCart, Icons.Rounded.CreditCard, Icons.Rounded.Receipt,
                            // Travel & Transport
                            Icons.Rounded.DirectionsCar, Icons.Rounded.DirectionsBike, Icons.Rounded.Map, Icons.Rounded.Commute,
                            // Misc
                            Icons.Rounded.MusicNote, Icons.Rounded.CameraAlt, Icons.Rounded.SportsEsports, Icons.Rounded.Build
                        )
                        
                        val displayedIcons = if (isExpanded) allIcons else allIcons.take(7)
                        
                        // Calculate grid height:
                        // Collapsed: 2 rows (approx 130dp)
                        // Expanded: Show exactly 3 rows (approx 185dp) to avoid cutting off 4th row
                        val gridHeight = if (isExpanded) 185.dp else 130.dp
                        
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(4),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.height(gridHeight)
                        ) {
                            items(displayedIcons.size) { index ->
                                val icon = displayedIcons[index]
                                val isIconSelected = selectedIcon == icon
                                val borderColor = if (isIconSelected) Color.Black.copy(alpha = 0.5f) else Color.Transparent

                                Box(
                                    modifier = Modifier
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha=0.5f))
                                        .border(2.dp, borderColor, RoundedCornerShape(16.dp)) // Selected Border
                                        .clickable { 
                                            selectedIcon = icon
                                            showError = false // Clear error on interaction
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = if(isIconSelected) Color.Black else Color.Gray,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            
                            // "More" button (only show if not expanded and we have more icons)
                            if (!isExpanded) {
                                item { 
                                     Box(
                                        modifier = Modifier
                                            .aspectRatio(1f)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha=0.5f))
                                            .clickable { isExpanded = true },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.MoreHoriz,
                                            contentDescription = "More",
                                            tint = Color.Gray,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            } else {
                                item { 
                                     Box(
                                        modifier = Modifier
                                            .aspectRatio(1f)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha=0.5f))
                                            .clickable { isExpanded = false },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.KeyboardArrowUp,
                                            contentDescription = "Collapse",
                                            tint = Color.Gray,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // COLOR Section
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "COLOR",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
                        )
                        
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            listOf(MatteMint, MattePeach, MatteLavender, MatteBlue).forEach { color ->
                                val isColorSelected = selectedColor == color
                                val borderColor = if (isColorSelected) Color.Black.copy(alpha = 0.5f) else Color.Transparent
                                
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .border(2.dp, borderColor, CircleShape) // Selected Border
                                        .clickable { 
                                            selectedColor = color 
                                            showError = false // Clear error on interaction
                                        }
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        TextButton(
                            onClick = { 
                                if (existingCategory != null) onRemoveCategory() else onDismiss()
                            },
                            modifier = Modifier.weight(1f).height(56.dp)
                        ) {
                            Text(
                                if (existingCategory != null) "Remove" else "Cancel",
                                color = Color.Gray,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        
                        Button(
                            onClick = { 
                                when {
                                    categoryName.isEmpty() -> {
                                        errorMessage = "Please enter a category name."
                                        showError = true
                                    }
                                    selectedIcon == null -> {
                                        errorMessage = "Please select an icon for the category."
                                        showError = true
                                    }
                                    selectedColor == null -> {
                                        errorMessage = "Please select a color for the category."
                                        showError = true
                                    }
                                    else -> {
                                       onSaveCategory(Category(categoryName, selectedIcon!!, selectedColor!!))
                                    }
                                }
                            },
                            modifier = Modifier
                                .weight(2f)
                                .height(56.dp)
                                .shadow(8.dp, CircleShape, spotColor = PrimaryColor.copy(alpha = 0.5f)),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryColor
                            ),
                            shape = CircleShape
                        ) {
                            Text(
                                if (existingCategory != null) "Save Changes" else "Add Category",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
    

}

data class Category(
    val name: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val color: Color
)
