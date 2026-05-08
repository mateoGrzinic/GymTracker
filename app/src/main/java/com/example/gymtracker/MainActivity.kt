package com.example.gymtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.gymtracker.ui.theme.GymTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            var isDarkMode by remember { mutableStateOf(false) }
            var currentScreen by remember { mutableStateOf("login") }

            GymTrackerTheme(darkTheme = isDarkMode) {
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                selected = currentScreen == "login",
                                onClick = { currentScreen = "login" },
                                label = { Text("Login") },
                                icon = { Text("L") }
                            )

                            NavigationBarItem(
                                selected = currentScreen == "workout",
                                onClick = { currentScreen = "workout" },
                                label = { Text("Vježbe") },
                                icon = { Text("V") }
                            )

                            NavigationBarItem(
                                selected = currentScreen == "settings",
                                onClick = { currentScreen = "settings" },
                                label = { Text("Postavke") },
                                icon = { Text("P") }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        when (currentScreen) {
                            "login" -> LoginScreen(
                                onLoginClick = { currentScreen = "workout" }
                            )

                            "workout" -> WorkoutScreen(
                                onSettingsClick = { currentScreen = "settings" }
                            )

                            "settings" -> SettingsScreen(
                                darkMode = isDarkMode,
                                onDarkModeChange = { isDarkMode = it }
                            )
                        }
                    }
                }
            }
        }
    }
}