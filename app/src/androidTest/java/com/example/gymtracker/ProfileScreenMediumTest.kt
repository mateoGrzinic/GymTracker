package com.example.gymtracker

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import com.example.gymtracker.ui.theme.GymTrackerTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ProfileScreenMediumTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun profileScreen_updatesAboutAndLevel() {
        val repository = FakeProfileRepository()

        composeTestRule.setContent {
            GymTrackerTheme {
                ProfileScreen(repository = repository)
            }
        }

        composeTestRule.onNodeWithText("Profil korisnika").assertIsDisplayed()
        composeTestRule.onNodeWithText("Stari opis").performTextReplacement("Novi opis")
        composeTestRule.onNodeWithText("Početnik").performClick()
        composeTestRule.onNodeWithText("Amater").performClick()
        composeTestRule.onNodeWithText("Spremi profil").performClick()

        assertEquals("Novi opis", repository.savedAbout)
        assertEquals("Amater", repository.savedLevel)

        composeTestRule.onNodeWithText("Profil je spremljen").assertIsDisplayed()
    }
}

private class FakeProfileRepository : FirestoreRepository() {
    var savedAbout = ""
    var savedLevel = ""

    override fun getUserSettings(onResult: (UserSettings) -> Unit) {
        onResult(
            UserSettings(
                name = "Mateo Gržinić",
                unit = "kg",
                goal = "Snaga",
                about = "Stari opis",
                level = "Početnik",
                darkMode = false
            )
        )
    }

    override fun updateUserProfile(
        about: String,
        level: String,
        onComplete: (Boolean) -> Unit
    ) {
        savedAbout = about
        savedLevel = level
        onComplete(true)
    }
}