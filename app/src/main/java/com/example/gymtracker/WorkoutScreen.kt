package com.example.gymtracker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.gymtracker.ui.theme.GymTrackerTheme
import androidx.compose.ui.graphics.Color
fun isValidExercise(name: String, sets: Int, reps: Int, weight: Double): Boolean {
    return name.isNotEmpty() && sets > 0 && reps > 0 && weight > 0
}

fun weightToKg(weight: Double, unit: String): Double {
    return if (unit == "lbs") {
        weight / 2.20462
    } else {
        weight
    }
}

fun weightFromKg(weight: Double, unit: String): Double {
    return if (unit == "lbs") {
        weight * 2.20462
    } else {
        weight
    }
}

fun formatWeight(weight: Double): String {
    return if (weight % 1.0 == 0.0) {
        weight.toInt().toString()
    } else {
        String.format("%.1f", weight)
    }
}

@Composable
fun WorkoutScreen(
    onSettingsClick: () -> Unit,
    repository: FirestoreRepository = FirestoreRepository()
) {
    val rememberedRepository = remember { repository }
    var exercises by remember { mutableStateOf(listOf<Exercise>()) }

    var exerciseName by remember { mutableStateOf("") }
    var sets by remember { mutableStateOf("") }
    var reps by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("kg") }
    val buttonTextColor = Color(0xFF0F172A)
    val activeExercises = exercises.filter { !it.isDone }

    LaunchedEffect(Unit) {
        rememberedRepository.getExercises { exercises = it }

        rememberedRepository.getUserSettings { settings ->
            unit = settings.unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        Text(
            text = "Trening",
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = exerciseName,
            onValueChange = { exerciseName = it },
            label = { Text("Naziv vježbe") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = sets,
            onValueChange = { sets = it },
            label = { Text("Broj serija") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = reps,
            onValueChange = { reps = it },
            label = { Text("Broj ponavljanja") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Težina u $unit") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val setsInt = sets.toIntOrNull() ?: 0
                val repsInt = reps.toIntOrNull() ?: 0
                val enteredWeight = weight.replace(",", ".").toDoubleOrNull() ?: 0.0
                val weightInKg = weightToKg(enteredWeight, unit)

                if (isValidExercise(exerciseName, setsInt, repsInt, enteredWeight)) {
                    val exercise = Exercise(
                        name = exerciseName,
                        sets = setsInt,
                        reps = repsInt,
                        weight = weightInKg
                    )

                    rememberedRepository.addExercise(exercise)

                    exerciseName = ""
                    sets = ""
                    reps = ""
                    weight = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Spremi vježbu",
                color = buttonTextColor
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onSettingsClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Postavke",
                color = buttonTextColor
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Moje vježbe:",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(activeExercises) { exercise ->
                ExerciseItem(
                    exercise = exercise,
                    unit = unit,
                    onDelete = { rememberedRepository.deleteExercise(exercise.id) },
                    onToggleDone = { checked ->
                        if (checked) {
                            val updated = exercise.copy(isDone = true)

                            exercises = exercises.map {
                                if (it.id == exercise.id) updated else it
                            }

                            rememberedRepository.updateExercise(updated)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ExerciseItem(
    exercise: Exercise,
    unit: String,
    onDelete: () -> Unit,
    onToggleDone: (Boolean) -> Unit
) {
    val displayedWeight = weightFromKg(exercise.weight, unit)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = exercise.isDone,
                onCheckedChange = { checked ->
                    onToggleDone(checked)
                }
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = exercise.name,
                    fontSize = 16.sp
                )

                Text(
                    text = "${exercise.sets} serija × ${exercise.reps} ponavljanja | ${formatWeight(displayedWeight)} $unit",
                    fontSize = 12.sp
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Obriši"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutScreenPreview() {
    GymTrackerTheme {
        WorkoutScreen(
            onSettingsClick = {},
            repository = object : FirestoreRepository() {
                override fun getExercises(onResult: (List<Exercise>) -> Unit) {
                    onResult(
                        listOf(
                            Exercise(name = "Bench Press", sets = 3, reps = 10, weight = 80.0),
                            Exercise(name = "Squat", sets = 4, reps = 8, weight = 100.0)
                        )
                    )
                }

                override fun getUserSettings(onResult: (UserSettings) -> Unit) {
                    onResult(UserSettings(unit = "kg", goal = "Snaga"))
                }

                override fun addExercise(exercise: Exercise) {}

                override fun deleteExercise(id: String) {}

                override fun updateExercise(exercise: Exercise) {}
            }
        )
    }
}