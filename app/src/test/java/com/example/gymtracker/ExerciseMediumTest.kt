package com.example.gymtracker

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ExerciseMediumTest {

    private lateinit var exerciseList: MutableList<Exercise>

    @Before
    fun setUp() {
        exerciseList = mutableListOf()
    }

    @Test
    fun addValidExercise_appearsInList() {
        val name = "Bench Press"
        val sets = 3
        val reps = 10
        val weight = 80.0

        // isValidExercise + dodavanje u listu zajedno
        if (isValidExercise(name, sets, reps, weight)) {
            exerciseList.add(Exercise(name = name, sets = sets, reps = reps, weight = weight))
        }

        assertEquals(1, exerciseList.size)
        assertEquals("Bench Press", exerciseList[0].name)
    }

    @Test
    fun addInvalidExercise_doesNotAppearInList() {
        val name = ""
        val sets = 0
        val reps = 10
        val weight = 80.0

        if (isValidExercise(name, sets, reps, weight)) {
            exerciseList.add(Exercise(name = name, sets = sets, reps = reps, weight = weight))
        }

        assertEquals(0, exerciseList.size)
    }

    @Test
    fun addMultipleExercises_filterDone_returnsOnlyCompleted() {
        // Dodajemo vježbe kroz validaciju
        listOf(
            Triple("Bench Press", false, true),
            Triple("Squat", true, true),
            Triple("Deadlift", false, true)
        ).forEach { (name, isDone, valid) ->
            if (valid) {
                exerciseList.add(Exercise(name = name, sets = 3, reps = 10, weight = 80.0, isDone = isDone))
            }
        }

        val doneExercises = exerciseList.filter { it.isDone }

        assertEquals(1, doneExercises.size)
        assertEquals("Squat", doneExercises[0].name)
    }

    @Test
    fun deleteExercise_thenVerifyRemovedFromList() {
        if (isValidExercise("Bench Press", 3, 10, 80.0)) {
            exerciseList.add(Exercise(id = "1", name = "Bench Press", sets = 3, reps = 10, weight = 80.0))
        }
        if (isValidExercise("Squat", 4, 8, 100.0)) {
            exerciseList.add(Exercise(id = "2", name = "Squat", sets = 4, reps = 8, weight = 100.0))
        }

        exerciseList.removeIf { it.id == "1" }

        assertEquals(1, exerciseList.size)
        assertEquals("Squat", exerciseList[0].name)
    }
}