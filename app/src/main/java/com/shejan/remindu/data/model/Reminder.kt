package com.shejan.remindu.data.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.shejan.remindu.ui.screens.Category
import java.time.LocalDateTime

data class Reminder(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val dateTime: LocalDateTime,
    val type: String, // "Notification", "Alarm", "Voice"
    val category: Category?,
    val isCompleted: Boolean = false,
    val repeatDays: Set<Int> = emptySet()
)
