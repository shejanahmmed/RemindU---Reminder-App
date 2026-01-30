package com.shejan.remindu

import android.os.Bundle
import androidx.compose.animation.Crossfade
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import com.shejan.remindu.ui.theme.RemindUTheme
import com.shejan.remindu.ui.screens.HomeScreen
import com.shejan.remindu.ui.screens.CreateReminderScreen
import androidx.compose.ui.Alignment
import com.shejan.remindu.ui.screens.BottomNavigationBar
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shejan.remindu.ui.viewmodels.RemindUViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RemindUTheme {
                // Navigation State
                // Navigation State
                var currentScreen by remember { mutableStateOf("home") }
                var selectedTab by remember { mutableStateOf("Home") }
                
                // Shared ViewModel
                val viewModel: RemindUViewModel = viewModel()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                   // Main Content Area
                   Box(modifier = Modifier.fillMaxSize()) {
                       Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
                           when (screen) {
                               "home" -> HomeScreen(
                                   viewModel = viewModel,
                                   onFabClick = { 
                                       currentScreen = "create"
                                       selectedTab = "Add"
                                   }
                               )
                               "create" -> CreateReminderScreen(
                                   viewModel = viewModel,
                                   onBackClick = { 
                                       currentScreen = "home"
                                       selectedTab = "Home"
                                   }
                               )
                           }
                       }
                       
                       // Persistent Bottom Navigation
                       Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                           BottomNavigationBar(
                               selectedItem = selectedTab,
                               onItemSelected = { tab ->
                                   selectedTab = tab
                                   when (tab) {
                                       "Home" -> currentScreen = "home"
                                       "Add" -> currentScreen = "create"
                                       // Other tabs can just switch selection for now or nav to placeholder
                                   }
                               }
                           )
                       }
                   }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RemindUTheme {
        HomeScreen(onFabClick = {})
    }
}