package com.example.gymtracker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymtracker.ui.theme.GymTrackerTheme

@Composable
fun ProfileScreen(
    repository: FirestoreRepository = FirestoreRepository()
) {
    val rememberedRepository = remember { repository }

    var name by remember { mutableStateOf("") }
    var about by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("Snaga") }
    var level by remember { mutableStateOf("Početnik") }
    var saveMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        rememberedRepository.getUserSettings { settings ->
            name = settings.name
            about = settings.about
            goal = settings.goal
            level = settings.level
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        Text(
            text = "Profil korisnika",
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Ime",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = if (name.isNotEmpty()) name else "Nije uneseno",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "O meni",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = about,
                    onValueChange = { about = it },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    placeholder = { Text("Napiši nešto o sebi...") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Cilj",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = goal,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Razina",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                LevelDropdown(
                    selectedLevel = level,
                    onLevelSelected = { level = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        rememberedRepository.updateUserProfile(
                            about = about,
                            level = level
                        ) { success ->
                            saveMessage = if (success) {
                                "Profil je spremljen"
                            } else {
                                "Greška kod spremanja profila"
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Spremi profil")
                }

                if (saveMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = saveMessage,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


    }
}

@Composable
fun LevelDropdown(
    selectedLevel: String,
    onLevelSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val levels = listOf("Početnik", "Rekreativac", "Amater", "Napredni", "Profesionalac")

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selectedLevel)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            levels.forEach { level ->
                DropdownMenuItem(
                    text = { Text(level) },
                    onClick = {
                        onLevelSelected(level)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    GymTrackerTheme {
        ProfileScreen(
            repository = object : FirestoreRepository() {
                override fun getUserSettings(onResult: (UserSettings) -> Unit) {
                    onResult(
                        UserSettings(
                            name = "Mateo Gržinić",
                            unit = "kg",
                            goal = "Snaga",
                            about = "Treniram 3 puta tjedno i želim pratiti napredak.",
                            level = "Početnik"
                        )
                    )
                }

                override fun updateUserProfile(
                    about: String,
                    level: String,
                    onComplete: (Boolean) -> Unit
                ) {
                    onComplete(true)
                }
            }
        )
    }
}