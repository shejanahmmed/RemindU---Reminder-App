package com.shejan.remindu.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.ExperimentalFoundationApi
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
import com.shejan.remindu.ui.viewmodels.RemindUViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun CreateReminderScreen(
    onBackClick: () -> Unit,
    viewModel: RemindUViewModel = viewModel()
) {
    val showBottomSheet by viewModel.showBottomSheet
    val showCategoryDialog by viewModel.showCategoryDialog
    val editingCategory by viewModel.editingCategory
    val selectedDateTime by viewModel.selectedDateTime

    val reminderDescription by viewModel.reminderDescription
    
    // Categories State
    val categories = viewModel.categories

    Scaffold(
        containerColor = SoftBeige
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


            InputSection(
                description = reminderDescription,
                onDescriptionChange = viewModel::onDescriptionChange
            )
            Spacer(modifier = Modifier.height(32.dp))
            val selectedCategory by viewModel.selectedCategory
            CategorySection(
                categories = categories,
                selectedCategory = selectedCategory,
                onCreateClick = { 
                    viewModel.onEditCategory(null)
                },
                onCategorySelect = { category ->
                    viewModel.onCategorySelected(category)
                },
                onCategoryEdit = { category ->
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
            Spacer(modifier = Modifier.height(32.dp))
            
            // Save Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(4.dp, RoundedCornerShape(28.dp), spotColor = MatteTerracotta.copy(alpha = 0.5f))
                        .clip(RoundedCornerShape(28.dp))
                        .background(MatteTerracotta)
                        .clickable {
                            viewModel.saveReminder(onSuccess = onBackClick)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Save Reminder",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(120.dp)) // Extra space to scroll past nav bar
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
                    viewModel.onCategorySelected(updatedCategory)
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
            .wrapContentHeight()
            .padding(vertical = 8.dp, horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "New Reminder",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = CharcoalBrown
            )
            Text(
                text = "Stay organized & never miss a beat.",
                style = MaterialTheme.typography.bodySmall,
                color = CharcoalBrown.copy(alpha = 0.7f)
            )
        }
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
             color = CharcoalBrown
        )
        
        TextField(
            value = description,
            onValueChange = onDescriptionChange,
            placeholder = { Text("Type here ...", color = MutedTaupe) },
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .border(1.dp, SubtleBorder, RoundedCornerShape(24.dp))
                .background(WarmOffWhite, RoundedCornerShape(24.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = WarmOffWhite,
                unfocusedContainerColor = WarmOffWhite,
                disabledContainerColor = WarmOffWhite,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = CharcoalBrown,
                unfocusedTextColor = CharcoalBrown
            ),
            shape = RoundedCornerShape(24.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategorySection(
    categories: List<Category>, 
    selectedCategory: Category?,
    onCreateClick: () -> Unit,
    onCategorySelect: (Category) -> Unit,
    onCategoryEdit: (Category) -> Unit
) {
    Column {
        Text(
            text = "Category",
             style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 32.dp, bottom = 12.dp),
             color = CharcoalBrown
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
                    isSelected = category == selectedCategory, 
                    color = category.color,
                    onClick = { onCategorySelect(category) },
                    onLongClick = { onCategoryEdit(category) }
                )
            }
            
            // Show Create Button only if no categories exist
            if (categories.isEmpty()) {
                item {
                    Surface(
                        color = MatteTerracotta,
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .height(44.dp)
                            .shadow(4.dp, RoundedCornerShape(50), spotColor = MatteTerracotta.copy(alpha = 0.5f))
                            .clickable { onCreateClick() }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = "Add Category",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Create",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryChip(
    label: String, 
    icon: androidx.compose.ui.graphics.vector.ImageVector, 
    isSelected: Boolean, 
    color: Color, 
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val bgColor = if (isSelected) color else color.copy(alpha = 0.15f)
    val contentColor = if (isSelected) Color.White else CharcoalBrown
    val iconColor = if (isSelected) Color.White else color
    val borderStroke = if (isSelected) null else BorderStroke(1.dp, color.copy(alpha = 0.3f))
    
    Surface(
        color = bgColor,
        shape = RoundedCornerShape(50),
        border = borderStroke,
        modifier = Modifier
            .height(44.dp)
            .shadow(if(isSelected) 8.dp else 0.dp, RoundedCornerShape(50), spotColor = color)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
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
             color = CharcoalBrown
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
                       color = MatteTerracotta.copy(alpha = 0.4f),
                       style = stroke,
                       cornerRadius = CornerRadius(24.dp.toPx())
                   )
                }
                .background(WarmOffWhite, RoundedCornerShape(24.dp))
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
                    color = MutedClay, 
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                         Icon(
                             imageVector = Icons.Rounded.DateRange, 
                             contentDescription = "Calendar",
                             tint = MatteTerracotta,
                             modifier = Modifier.size(28.dp)
                         )
                    }
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                     Text(
                         text = selectedDateTime?.format(DateTimeFormatter.ofPattern("MMM dd, hh:mm a")) ?: "Pick Date & Time", 
                         style = MaterialTheme.typography.bodyLarge, 
                         color = MatteTerracotta, 
                         fontWeight = FontWeight.Bold
                     )
                     Text(
                         text = if (selectedDateTime != null) "Time set" else "Customize your schedule", 
                         style = MaterialTheme.typography.bodyMedium, 
                         color = MutedTaupe
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
                            tint = MutedTaupe,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Icon(
                            imageVector = Icons.Rounded.Notifications, 
                            contentDescription = null, 
                            tint = MutedTaupe,
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
    val borderColor = if (isSelected) MatteTerracotta.copy(alpha = 0.6f) else SubtleBorder
    val titleColor = if (isSelected) MatteTerracotta else MutedTaupe
    
    Column(
        modifier = modifier
            .border(1.dp, borderColor, RoundedCornerShape(24.dp))
            .background(WarmOffWhite, RoundedCornerShape(24.dp))
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
            color = CharcoalBrown
        )
    }
}

@Composable
fun ReminderTypeSection() {
    var selectedType by remember { mutableStateOf("Notification") }

    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            text = "Reminder Type",
             style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp, bottom = 12.dp),
             color = CharcoalBrown
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
    val borderColor = if (isSelected) MatteTerracotta.copy(alpha = 0.6f) else SubtleBorder
    val bg = if (isSelected) MatteTerracotta.copy(alpha = 0.1f) else WarmOffWhite
    val iconBg = if (isSelected) MatteTerracotta else MutedClay
    val iconTint = if (isSelected) Color.White else MatteTerracotta
    val textColor = if (isSelected) MatteTerracotta else CharcoalBrown
    
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
            .border(1.dp, SubtleBorder, RoundedCornerShape(24.dp))
            .background(WarmOffWhite, RoundedCornerShape(24.dp))
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
                         .background(SoftCoral)
                 )
             }
        }
        
        Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
            Text(
                text = "RECORDING...",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = SoftCoral
            )
            Text(
                text = "Tap to stop or redo",
                style = MaterialTheme.typography.bodySmall,
                color = MutedTaupe
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
                .border(1.dp, SubtleBorder, RoundedCornerShape(24.dp))
                .background(WarmOffWhite, RoundedCornerShape(24.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Repeat, contentDescription = "Repeat", tint = MutedTaupe)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Repeat Reminder",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = CharcoalBrown
                    )
                    if (isRepeatEnabled && selectedDays.isNotEmpty()) {
                        Text(
                           text = getRepeatSummary(selectedDays),
                           style = MaterialTheme.typography.labelSmall,
                           color = MatteTerracotta
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
                    checkedTrackColor = MatteTerracotta,
                    checkedBorderColor = Color.Transparent,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = SubtleBorder,
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
                .background(WarmOffWhite)
                .border(1.dp, SubtleBorderRepeat, RoundedCornerShape(28.dp))
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Repeat Every",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = DeepCocoa
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Select All Button
                val allSelected = currentSelection.size == 7
                Surface(
                    onClick = {
                        currentSelection = if (allSelected) emptySet() else (1..7).toSet()
                    },
                    shape = RoundedCornerShape(24.dp),
                    color = VeryLightPeach,
                    border = BorderStroke(1.dp, SubtleBorderRepeat),
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
                            tint = if (allSelected) MattePastelCoral else WarmGrey,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Select All Days",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = DeepCocoa
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
                        val bgColor = if (isSelected) MattePastelCoral else MutedPebble
                        val textColor = if (isSelected) Color.White else WarmGrey
                        val borderColor = if (isSelected) MattePastelCoral else SubtleBorderRepeat
                        
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .shadow(if (isSelected) 4.dp else 0.dp, CircleShape, spotColor = PrimaryColor.copy(alpha = 0.3f))
                                .clip(CircleShape)
                                .background(bgColor)
                                .border(1.dp, borderColor, CircleShape)
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
                        .background(MattePastelCoral)
                        .clickable { onConfirm(currentSelection) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Confirm",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepCocoa
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
                color = WarmOffWhite,
                tonalElevation = 0.dp,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (existingCategory != null) "Edit Category" else "New Category",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalBrown
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
                        placeholder = { Text("Category Name", color = MutedTaupe) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .border(1.dp, SubtleBorder, RoundedCornerShape(16.dp)),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = SoftBeige,
                            unfocusedContainerColor = SoftBeige,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = CharcoalBrown,
                            unfocusedTextColor = CharcoalBrown
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
                            color = MutedTaupe,
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
                                val borderColor = if (isIconSelected) CharcoalBrown.copy(alpha = 0.7f) else Color.Transparent

                                Box(
                                    modifier = Modifier
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(SoftBeige)
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
                                        tint = if(isIconSelected) CharcoalBrown else MutedTaupe,
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
                                            .background(SoftBeige)
                                            .clickable { isExpanded = true },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.MoreHoriz,
                                            contentDescription = "More",
                                            tint = MutedTaupe,
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
                                            .background(SoftBeige)
                                            .clickable { isExpanded = false },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.KeyboardArrowUp,
                                            contentDescription = "Collapse",
                                            tint = MutedTaupe,
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
                            color = MutedTaupe,
                            modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
                        )
                        
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            listOf(SageGreen, SoftCoral, DustySand, SoftSkyBlue).forEach { color ->
                                val isColorSelected = selectedColor == color
                                val borderColor = if (isColorSelected) CharcoalBrown.copy(alpha = 0.7f) else Color.Transparent
                                
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .border(3.dp, borderColor, CircleShape) // Selected Border
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
                                color = CharcoalBrown,
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
                                .shadow(8.dp, CircleShape, spotColor = MatteTerracotta.copy(alpha = 0.5f)),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MatteTerracotta
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
