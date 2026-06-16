package com.example.gymtracker

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

open class FirestoreRepository {
    private val db by lazy { FirebaseFirestore.getInstance() }
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val collection by lazy { db.collection("workouts") }

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
            onResult(emptyList())
            return
        }

        collection
            .whereEqualTo("userId", currentUser.uid)
            .addSnapshotListener { snapshot, _ ->
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

                onResult(items)
            }
    }
}