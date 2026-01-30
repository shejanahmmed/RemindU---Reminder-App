package com.shejan.remindu.ui.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.shejan.remindu.ui.screens.Category
import com.shejan.remindu.data.model.Reminder
import java.time.LocalDateTime

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*

class RemindUViewModel : ViewModel() {
    
    // Data State
    val reminders = mutableStateListOf<Reminder>()

    // Form State
    var reminderTitle = mutableStateOf("")
        private set
        
    var reminderDescription = mutableStateOf("")
        private set
        
    var selectedDateTime = mutableStateOf<LocalDateTime?>(null)
        private set
        
    var selectedType = mutableStateOf("Voice")
        private set

    // Category State
    var categories = mutableStateListOf<Category>()
    var selectedCategory = mutableStateOf<Category?>(null)
        private set
        
    // Dialog & Sheet Visibility
    var showBottomSheet = mutableStateOf(false)
    var showCategoryDialog = mutableStateOf(false)
    var editingCategory = mutableStateOf<Category?>(null)

    // Repeat State
    var isRepeatEnabled = mutableStateOf(false)
    var repeatDays = mutableStateOf<Set<Int>>(emptySet())

    // Events
    fun onTitleChange(newTitle: String) {
        reminderTitle.value = newTitle
    }
    
    fun onDescriptionChange(newDescription: String) {
        reminderDescription.value = newDescription
    }
    
    fun onDateTimeSelected(dateTime: LocalDateTime?) {
        selectedDateTime.value = dateTime
    }
    
    fun onTypeSelected(type: String) {
        selectedType.value = type
    }

    fun onCategorySelected(category: Category) {
        selectedCategory.value = category
    }
    
    fun addCategory(category: Category) {
        categories.add(category)
    }
    
    fun updateCategory(oldCategory: Category, newCategory: Category) {
        val index = categories.indexOf(oldCategory)
        if (index != -1) {
            categories[index] = newCategory
        }
    }
    
    fun removeCategory(category: Category) {
        categories.remove(category)
    }
    
    fun onEditCategory(category: Category?) {
        editingCategory.value = category
        showCategoryDialog.value = true
    }
    
    fun saveReminder(onSuccess: () -> Unit) {
        if (reminderTitle.value.isBlank() || selectedDateTime.value == null) return
        
        val newReminder = Reminder(
            title = reminderTitle.value,
            description = reminderDescription.value,
            dateTime = selectedDateTime.value!!,
            type = selectedType.value,
            category = selectedCategory.value,
            repeatDays = if (isRepeatEnabled.value) repeatDays.value else emptySet()
        )
        
        reminders.add(newReminder)
        
        // Reset Form
        reminderTitle.value = ""
        reminderDescription.value = ""
        selectedDateTime.value = null
        selectedType.value = "Voice"
        selectedCategory.value = null
        isRepeatEnabled.value = false
        repeatDays.value = emptySet()
        
        onSuccess()
    }
}
