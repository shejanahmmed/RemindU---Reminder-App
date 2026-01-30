package com.shejan.remindu.ui.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.shejan.remindu.ui.screens.Category
import java.time.LocalDateTime

class CreateReminderViewModel : ViewModel() {
    
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
        private set
        
    // Dialog & Sheet Visibility
    var showBottomSheet = mutableStateOf(false)
    var showCategoryDialog = mutableStateOf(false)
    var editingCategory = mutableStateOf<Category?>(null)

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
}
