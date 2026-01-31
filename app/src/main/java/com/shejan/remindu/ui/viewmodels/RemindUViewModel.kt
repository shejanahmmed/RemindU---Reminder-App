package com.shejan.remindu.ui.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.shejan.remindu.ui.screens.Category
import com.shejan.remindu.data.model.Reminder
import java.time.LocalDateTime

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.google.gson.reflect.TypeToken

class RemindUViewModel(application: Application) : AndroidViewModel(application) {
    
    private val prefs = application.getSharedPreferences("remindu_prefs", Context.MODE_PRIVATE)
    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()

    init {
        loadReminders()
    }
    
    // Data State
    val reminders = mutableStateListOf<Reminder>()

    // Form State

        
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
    
    fun saveReminder(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (reminderDescription.value.isBlank()) {
            onError("Please enter a reminder description")
            return
        }
        if (selectedDateTime.value == null) {
            onError("Please select a date and time")
            return
        }
        
        val newReminder = Reminder(
            title = reminderDescription.value,
            description = "",
            dateTime = selectedDateTime.value!!,
            type = selectedType.value,
            category = selectedCategory.value,
            repeatDays = if (isRepeatEnabled.value) repeatDays.value else emptySet()
        )
        
        reminders.add(newReminder)
        
        // Reset Form
        reminderDescription.value = ""
        selectedDateTime.value = null
        selectedType.value = "Voice"
        selectedCategory.value = null
        isRepeatEnabled.value = false
        repeatDays.value = emptySet()
        
        onSuccess()
        saveReminders()
    }

    private fun saveReminders() {
        val json = gson.toJson(reminders.toList())
        prefs.edit().putString("reminders_data", json).apply()
    }

    private fun loadReminders() {
        val json = prefs.getString("reminders_data", null)
        if (json != null) {
            val type = object : TypeToken<List<Reminder>>() {}.type
            val loadedList: List<Reminder> = gson.fromJson(json, type)
            reminders.clear()
            reminders.addAll(loadedList)
        }
    }
}

class LocalDateTimeAdapter : TypeAdapter<LocalDateTime>() {
    override fun write(out: JsonWriter, value: LocalDateTime?) {
        out.value(value?.toString())
    }
    override fun read(input: JsonReader): LocalDateTime? {
        val str = input.nextString()
        return if (str != null) LocalDateTime.parse(str) else null
    }
}
