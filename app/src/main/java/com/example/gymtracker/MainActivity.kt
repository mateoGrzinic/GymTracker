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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.gymtracker.ui.theme.GymTrackerTheme
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        FirebaseAuth.getInstance().signOut()

        setContent {
            var isDarkMode by remember { mutableStateOf(false) }
            var isLoggedIn by remember { mutableStateOf(false) }
            var currentScreen by remember { mutableStateOf("workout") }

            GymTrackerTheme(darkTheme = isDarkMode) {
                if (!isLoggedIn) {
                    LoginScreen(
                        onLoginClick = {
                            isLoggedIn = true
                            currentScreen = "workout"
                        }
                    )
                } else {
                    Scaffold(
                        bottomBar = {
                            NavigationBar {
                                NavigationBarItem(
                                    selected = currentScreen == "workout",
                                    onClick = { currentScreen = "workout" },
                                    label = { Text("Vježbe") },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Filled.FitnessCenter,
                                            contentDescription = "Vježbe"
                                        )
                                    }
                                )

                                NavigationBarItem(
                                    selected = currentScreen == "profile",
                                    onClick = { currentScreen = "profile" },
                                    label = { Text("Profil") },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Filled.Person,
                                            contentDescription = "Profil"
                                        )
                                    }
                                )

                                NavigationBarItem(
                                    selected = currentScreen == "progress",
                                    onClick = { currentScreen = "progress" },
                                    label = { Text("Napredak") },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Filled.TrendingUp,
                                            contentDescription = "Napredak"
                                        )
                                    }
                                )

                                NavigationBarItem(
                                    selected = currentScreen == "history",
                                    onClick = { currentScreen = "history" },
                                    label = { Text("Povijest") },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Filled.History,
                                            contentDescription = "Povijest"
                                        )
                                    }
                                )

                                NavigationBarItem(
                                    selected = currentScreen == "settings",
                                    onClick = { currentScreen = "settings" },
                                    label = { Text("Postavke") },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Filled.Settings,
                                            contentDescription = "Postavke"
                                        )
                                    }
                                )
                            }
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            when (currentScreen) {
                                "workout" -> WorkoutScreen(
                                    onSettingsClick = { currentScreen = "settings" }
                                )

                                "profile" -> ProfileScreen()

                                "progress" -> ProgressScreen()

                                "history" -> WorkoutHistoryScreen()

                                "settings" -> SettingsScreen(
                                    darkMode = isDarkMode,
                                    onDarkModeChange = { isDarkMode = it },
                                    onLogout = {
                                        FirebaseAuth.getInstance().signOut()
                                        isLoggedIn = false
                                        currentScreen = "workout"
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}