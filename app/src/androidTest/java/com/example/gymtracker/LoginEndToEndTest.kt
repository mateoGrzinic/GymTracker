package com.example.gymtracker

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.gymtracker.ui.theme.GymTrackerTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun loginButton_click_navigatesToWorkoutScreen() {
        // Provjera da smo na Login screenu
        composeTestRule.onNodeWithText("Prijava").assertIsDisplayed()

        // Klik na Prijava
        composeTestRule.onNodeWithText("Prijava").performClick()

        // Čekamo da se WorkoutScreen učita
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithText("Trening")
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        // Provjera da smo prešli na WorkoutScreen
        composeTestRule.onNodeWithText("Trening").assertIsDisplayed()
    }
}