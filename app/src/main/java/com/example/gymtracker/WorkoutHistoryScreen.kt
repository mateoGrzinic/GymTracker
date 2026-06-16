package com.example.gymtracker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymtracker.ui.theme.GymTrackerTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun WorkoutHistoryScreen() {
    var exercises by remember { mutableStateOf<List<Exercise>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }


    val currentUser = FirebaseAuth.getInstance().currentUser
    val currentUid = currentUser?.uid

    DisposableEffect(currentUid) {
        if (currentUid == null) {
            errorMessage = "Korisnik nije prijavljen."
            isLoading = false
            onDispose { }
        } else {
            val listener = FirebaseFirestore.getInstance()
                .collection("workouts")
                .whereEqualTo("userId", currentUid)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        errorMessage = "Greška Firestore: ${error.message}"
                        isLoading = false
                        return@addSnapshotListener
                    }

                    if (snapshot == null) {
                        errorMessage = "Snapshot je prazan."
                        isLoading = false
                        return@addSnapshotListener
                    }

                    exercises = snapshot.documents.map { document ->
                        Exercise(
                            id = document.id,
                            userId = document.getString("userId") ?: "",
                            name = document.getString("name") ?: "",
                            sets = document.getLong("sets")?.toInt() ?: 0,
                            reps = document.getLong("reps")?.toInt() ?: 0,
                            weight = document.getDouble("weight")
                                ?: document.getLong("weight")?.toDouble()
                                ?: 0.0,
                            isDone = document.getBoolean("isDone") ?: false
                        )
                    }

                    errorMessage = ""
                    isLoading = false
                }

            onDispose {
                listener.remove()
            }
        }
    }

    WorkoutHistoryContent(
        exercises = exercises,
        isLoading = isLoading,
        errorMessage = errorMessage,
        userEmail = currentUser?.email
    )
}

@Composable
fun WorkoutHistoryContent(
    exercises: List<Exercise>,
    isLoading: Boolean,
    errorMessage: String,
    userEmail: String?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Moje spremljene vježbe",
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (userEmail != null) {
            Text(
                text = "Prijavljen korisnik: $userEmail",
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> {
                Text("Učitavanje vježbi iz Firebase baze...")
            }

            errorMessage.isNotEmpty() -> {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }

            exercises.isEmpty() -> {
                Text("Nema spremljenih vježbi za ovog korisnika.")
            }

            else -> {
                exercises.forEach { exercise ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = exercise.name,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text("Serije: ${exercise.sets}")
                            Text("Ponavljanja: ${exercise.reps}")
                            Text("Težina: ${exercise.weight} kg")
                            Text(
                                text = "Status: ${
                                    if (exercise.isDone) "odrađeno" else "nije odrađeno"
                                }"
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
fun WorkoutHistoryScreenPreview() {
    GymTrackerTheme {
        WorkoutHistoryContent(
            exercises = listOf(
                Exercise(
                    name = "Bench Press",
                    sets = 3,
                    reps = 10,
                    weight = 80.0,
                    isDone = false
                )
            ),
            isLoading = false,
            errorMessage = "",
            userEmail = "mateo@gymtracker.com"
        )
    }
}