package com.example.gymtracker

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.gymtracker.ui.theme.GymTrackerTheme
import androidx.compose.foundation.background

@Composable
fun SettingsScreen(darkMode: Boolean,
                   onDarkModeChange: (Boolean) -> Unit) {
    var reminders by remember { mutableStateOf(true) }
    var unit by remember { mutableStateOf("kg") }
    var goal by remember { mutableStateOf("Snaga") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Postavke",
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Tamni način rada",
                color = MaterialTheme.colorScheme.onBackground
            )
            Switch(
                checked = darkMode,
                onCheckedChange = { onDarkModeChange(it) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Podsjetnik za trening",
                color = MaterialTheme.colorScheme.onBackground
            )
            Switch(
                checked = reminders,
                onCheckedChange = { reminders = it }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Jedinica težine",
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                RadioButton(
                    selected = unit == "kg",
                    onClick = { unit = "kg" }
                )
                Text(
                    text = "kg",
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 12.dp))
            }

            Row {
                RadioButton(
                    selected = unit == "lbs",
                    onClick = { unit = "lbs" }
                )
                Text(
                    text = "lbs",
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 12.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Cilj treninga",
            color = MaterialTheme.colorScheme.onBackground
        )

        GoalDropdown(
            selectedGoal = goal,
            onGoalSelected = { goal = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Spremi postavke")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Obriši sve podatke")
        }
    }
}

@Composable
fun GoalDropdown(
    selectedGoal: String,
    onGoalSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val goals = listOf("Snaga", "Masa", "Kondicija", "Mršavljenje")

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selectedGoal)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            goals.forEach { goal ->
                DropdownMenuItem(
                    text = { Text(goal) },
                    onClick = {
                        onGoalSelected(goal)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    GymTrackerTheme {
        SettingsScreen(
            darkMode = false,
            onDarkModeChange = {}
        )
    }
}