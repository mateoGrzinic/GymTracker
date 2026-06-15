package com.example.gymtracker

import com.google.firebase.firestore.FirebaseFirestore

open class FirestoreRepository {
    private val db by lazy { FirebaseFirestore.getInstance() }
    private val collection by lazy { db.collection("exercises") }

    open fun addExercise(exercise: Exercise) {
        collection.add(exercise)
    }

    open fun deleteExercise(id: String) {
        collection.document(id).delete()
    }

    open fun updateExercise(exercise: Exercise) {
        if (exercise.id.isNotEmpty()) {
            collection.document(exercise.id).set(exercise)
        }
    }

    open fun getExercises(onResult: (List<Exercise>) -> Unit) {
        collection.addSnapshotListener { snapshot, _ ->
            val items = snapshot?.documents?.mapNotNull { doc ->
                val item = doc.toObject(Exercise::class.java)
                item?.id = doc.id
                item
            } ?: emptyList()
            onResult(items)
        }
    }
}