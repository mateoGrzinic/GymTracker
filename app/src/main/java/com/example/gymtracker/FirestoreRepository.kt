package com.example.gymtracker

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
data class UserSettings(
    val name: String = "",
    val unit: String = "kg",
    val goal: String = "Snaga",
    val about: String = "",
    val level: String = "Početnik",
    val darkMode: Boolean = false
)
open class FirestoreRepository {
    private val db by lazy { FirebaseFirestore.getInstance() }
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val collection by lazy { db.collection("workouts") }
    private val usersCollection by lazy { db.collection("users") }
    open fun addExercise(exercise: Exercise) {
        val currentUser = auth.currentUser ?: return

        val data = hashMapOf(
            "userId" to currentUser.uid,
            "name" to exercise.name,
            "sets" to exercise.sets,
            "reps" to exercise.reps,
            "weight" to exercise.weight,
            "isDone" to exercise.isDone
        )

        collection.add(data)
    }
    open fun getUserSettings(onResult: (UserSettings) -> Unit) {
        val currentUser = auth.currentUser

        if (currentUser == null) {
            onResult(UserSettings())
            return
        }

        db.collection("users")
            .document(currentUser.uid)
            .get()
            .addOnSuccessListener { document ->
                val settings = UserSettings(
                    name = document.getString("name") ?: currentUser.email ?: "",
                    unit = document.getString("unit") ?: "kg",
                    goal = document.getString("goal") ?: "Snaga",
                    about = document.getString("about") ?: "",
                    level = document.getString("level") ?: "Početnik",
                    darkMode = document.getBoolean("darkMode") ?: false
                )

                onResult(settings)
            }
            .addOnFailureListener {
                onResult(UserSettings())
            }
    }
    open fun updateUserProfile(
        about: String,
        level: String,
        onComplete: (Boolean) -> Unit = {}
    ) {
        val currentUser = auth.currentUser

        if (currentUser == null) {
            onComplete(false)
            return
        }

        val data = hashMapOf(
            "about" to about,
            "level" to level
        )

        db.collection("users")
            .document(currentUser.uid)
            .set(data, SetOptions.merge())
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }
    open fun updateUserSettings(
        unit: String,
        goal: String,
        darkMode: Boolean,
        onComplete: (Boolean) -> Unit = {}
    ) {
        val currentUser = auth.currentUser

        if (currentUser == null) {
            onComplete(false)
            return
        }

        val data = hashMapOf(
            "unit" to unit,
            "goal" to goal,
            "darkMode" to darkMode
        )

        db.collection("users")
            .document(currentUser.uid)
            .set(data, SetOptions.merge())
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }

    open fun deleteExercise(id: String) {
        if (id.isNotEmpty()) {
            collection.document(id).delete()
        }
    }

    open fun updateExercise(exercise: Exercise) {
        if (exercise.id.isNotEmpty()) {
            collection.document(exercise.id)
                .update("isDone", exercise.isDone)
        }
    }

    open fun getExercises(onResult: (List<Exercise>) -> Unit) {
        val currentUser = auth.currentUser

        if (currentUser == null) {
            println("Korisnik nije prijavljen")
            onResult(emptyList())
            return
        }

        println("CURRENT USER UID: ${currentUser.uid}")

        collection
            .whereEqualTo("userId", currentUser.uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    println("Greška kod dohvaćanja vježbi: ${error.message}")
                    onResult(emptyList())
                    return@addSnapshotListener
                }

                val items = snapshot?.documents?.map { doc ->
                    Exercise(
                        id = doc.id,
                        userId = doc.getString("userId") ?: "",
                        name = doc.getString("name") ?: "",
                        sets = doc.getLong("sets")?.toInt() ?: 0,
                        reps = doc.getLong("reps")?.toInt() ?: 0,
                        weight = doc.getDouble("weight")
                            ?: doc.getLong("weight")?.toDouble()
                            ?: 0.0,
                        isDone = doc.getBoolean("isDone") ?: false
                    )
                } ?: emptyList()

                println("BROJ VJEŽBI: ${items.size}")

                onResult(items)
            }
    }
}