package com.example.gymtracker

import org.junit.Test
import org.junit.Assert.*

class ExerciseUnitTest {

    @Test
    fun isValidExercise_allValid_returnsTrue() {
        assertTrue(isValidExercise("Bench Press", 3, 10, 80.0))
    }

    @Test
    fun isValidExercise_emptyName_returnsFalse() {
        assertFalse(isValidExercise("", 3, 10, 80.0))
    }

    @Test
    fun isValidExercise_zeroSets_returnsFalse() {
        assertFalse(isValidExercise("Bench Press", 0, 10, 80.0))
    }

    @Test
    fun isValidExercise_negativeWeight_returnsFalse() {
        assertFalse(isValidExercise("Bench Press", 3, 10, -5.0))
    }
}