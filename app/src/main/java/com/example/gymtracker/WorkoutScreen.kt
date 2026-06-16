package com.example.gymtracker

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
import androidx.compose.foundation.background
import androidx.compose.ui.tooling.preview.Preview
import com.example.gymtracker.ui.theme.GymTrackerTheme
fun isValidExercise(name: String, sets: Int, reps: Int, weight: Double): Boolean {
    return name.isNotEmpty() && sets > 0 && reps > 0 && weight > 0
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

    LaunchedEffect(Unit) {
        rememberedRepository.getExercises { exercises = it }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        Text(text = "Trening", fontSize = 30.sp,
            color = MaterialTheme.colorScheme.onBackground)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = exerciseName,
            onValueChange = { exerciseName = it },
            label = { Text("Naziv vježbe") },
            modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = sets,
            onValueChange = { sets = it },
            label = { Text("Broj serija") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = reps,
            onValueChange = { reps = it },
            label = { Text("Broj ponavljanja") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = weight,
            onValueChange = { weight = it },
            label = { Text("Težina u kg") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val setsInt = sets.toIntOrNull() ?: 0
                val repsInt = reps.toIntOrNull() ?: 0
                val weightDouble = weight.toDoubleOrNull() ?: 0.0

                if (isValidExercise(exerciseName, setsInt, repsInt, weightDouble)) {
                    val exercise = Exercise(
                        name = exerciseName,
                        sets = setsInt,
                        reps = repsInt,
                        weight = weightDouble
                    )
                    rememberedRepository.addExercise(exercise)
                    exerciseName = ""; sets = ""; reps = ""; weight = ""
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Spremi vježbu", color = MaterialTheme.colorScheme.onBackground)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onSettingsClick, modifier = Modifier.fillMaxWidth()) {
            Text("Postavke", color = MaterialTheme.colorScheme.onBackground)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Moje vježbe:", fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onBackground)

        Spacer(modifier = Modifier.height(8.dp))

        // Lista vježbi iz Firestorea
        LazyColumn {
            items(exercises) { exercise ->
                ExerciseItem(
                    exercise = exercise,
                    onDelete = { rememberedRepository.deleteExercise(exercise.id) },
                    onToggleDone = { checked ->
                        val updated = exercise.copy(isDone = checked)

                        exercises = exercises.map {
                            if (it.id == exercise.id) updated else it
                        }

                        rememberedRepository.updateExercise(updated)
                    }
                )
            }
        }
    }
}

@Composable
fun ExerciseItem(
    exercise: Exercise,
    onDelete: () -> Unit,
    onToggleDone: (Boolean) -> Unit
) {
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
                Text(text = exercise.name, fontSize = 16.sp)
                Text(
                    text = "${exercise.sets} serija × ${exercise.reps} ponavljanja | ${exercise.weight} kg",
                    fontSize = 12.sp
                )
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Obriši")
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
                    onResult(listOf(
                        Exercise(name = "Bench Press", sets = 3, reps = 10, weight = 80.0),
                        Exercise(name = "Squat", sets = 4, reps = 8, weight = 100.0)
                    ))
                }
                override fun addExercise(exercise: Exercise) {}
                override fun deleteExercise(id: String) {}
                override fun updateExercise(exercise: Exercise) {}
            }
        )
    }
}