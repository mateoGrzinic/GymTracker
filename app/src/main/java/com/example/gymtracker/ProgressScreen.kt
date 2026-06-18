package com.example.gymtracker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.gymtracker.ui.theme.GymTrackerTheme

data class ProgressStat(
    val title: String,
    val value: String,
    val description: String
)

fun calculateCompletion(completedWorkouts: Int, plannedWorkouts: Int): Int {
    if (plannedWorkouts <= 0) return 0
    return ((completedWorkouts.toDouble() / plannedWorkouts.toDouble()) * 100).toInt()
}

fun convertWeightFromKg(weight: Double, unit: String): Double {
    return if (unit == "lbs") {
        weight * 2.20462
    } else {
        weight
    }
}

fun calculateProgressStats(exercises: List<Exercise>, unit: String): List<ProgressStat> {
    val completed = exercises.count { it.isDone }
    val planned = exercises.size
    val completion = calculateCompletion(completed, planned)

    val totalVolumeKg = exercises
        .filter { it.isDone }
        .sumOf { it.sets * it.reps * it.weight }

    val totalVolume = convertWeightFromKg(totalVolumeKg, unit)

    return listOf(
        ProgressStat(
            title = "Odrađene vježbe",
            value = "$completed / $planned",
            description = "Ukupni broj odrađenih vježbi"
        ),
        ProgressStat(
            title = "Napredak",
            value = "$completion%",
            description = "Postotak odrađenih vježbi"
        ),
        ProgressStat(
            title = "Ukupan volumen",
            value = "${totalVolume.toInt()} $unit",
            description = "Zbroj svih dignutih težina"
        )
    )
}

@Composable
fun ProgressScreen(
    repository: FirestoreRepository = FirestoreRepository()
) {
    val rememberedRepository = remember { repository }

    var exercises by remember { mutableStateOf(listOf<Exercise>()) }
    var unit by remember { mutableStateOf("kg") }

    LaunchedEffect(Unit) {
        rememberedRepository.getExercises { exercises = it }

        rememberedRepository.getUserSettings { settings ->
            unit = settings.unit
        }
    }

    ProgressContent(
        stats = calculateProgressStats(
            exercises = exercises,
            unit = unit
        )
    )
}

@Composable
fun ProgressContent(stats: List<ProgressStat>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        Text(
            text = "Napredak",
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        stats.forEach { stat ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = stat.title, fontSize = 18.sp)

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = stat.value,
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(text = stat.description)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProgressScreenPreview() {
    GymTrackerTheme {
        ProgressContent(
            stats = calculateProgressStats(
                exercises = listOf(
                    Exercise(name = "Bench Press", sets = 3, reps = 10, weight = 80.0, isDone = true),
                    Exercise(name = "Squat", sets = 4, reps = 8, weight = 100.0, isDone = false),
                    Exercise(name = "Deadlift", sets = 3, reps = 5, weight = 120.0, isDone = true)
                ),
                unit = "kg"
            )
        )
    }
}