package com.example.gymtracker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

fun calculateWeeklyCompletion(completedWorkouts: Int, plannedWorkouts: Int): Int {
    if (plannedWorkouts <= 0) return 0
    return ((completedWorkouts.toDouble() / plannedWorkouts.toDouble()) * 100).toInt()
}

fun defaultProgressStats(): List<ProgressStat> {
    val completed = 3
    val planned = 4
    val completion = calculateWeeklyCompletion(completed, planned)

    return listOf(
        ProgressStat(
            "Odrađeni treninzi",
            "$completed / $planned",
            "Broj odrađenih treninga ovaj tjedan"
        ),
        ProgressStat(
            "Tjedni napredak",
            "$completion%",
            "Postotak realizacije plana"
        ),
        ProgressStat(
            "Ukupan volumen",
            "2400 kg",
            "Zbroj težina iz zadnjih treninga"
        )
    )
}

@Composable
fun ProgressScreen(stats: List<ProgressStat> = defaultProgressStats()) {
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
        ProgressScreen()
    }
}