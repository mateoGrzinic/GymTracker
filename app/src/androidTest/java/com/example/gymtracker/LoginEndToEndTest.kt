package com.example.gymtracker

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class LoginEndToEndTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun loginOpensWorkoutScreen() {
        composeTestRule.onNodeWithText("GymTracker").assertIsDisplayed()
        composeTestRule.onNodeWithText("Prijava").performClick()

        composeTestRule.waitUntil(timeoutMillis = 15000) {
            composeTestRule
                .onAllNodesWithText("Trening")
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule.onNodeWithText("Trening").assertIsDisplayed()
        composeTestRule.onNodeWithText("Moje vježbe:").assertIsDisplayed()
    }
}