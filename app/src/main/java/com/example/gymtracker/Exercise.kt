package com.example.gymtracker

data class Exercise(
    var id: String = "",
    var name: String = "",
    var sets: Int = 0,
    var reps: Int = 0,
    var weight: Double = 0.0,
    var isDone: Boolean = false
)