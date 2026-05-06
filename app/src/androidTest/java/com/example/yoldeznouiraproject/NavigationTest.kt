package com.example.yoldeznouiraproject

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @Rule
    @JvmField
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testStartGameNavigation() {
        // Wait for splash screen to timeout (approx 2s) or manually skip if possible
        // Since it's a UI test, we might need to wait for the MenuScreen to appear

        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodesWithText("Tunisia Heritage").fetchSemanticsNodes().isNotEmpty()
        }

        // Click Start Game
        composeTestRule.onNodeWithText("Start Game (Roman)").performClick()

        // Check if we are on the Quiz Screen (Title might be Heritage Quest)
        composeTestRule.onNodeWithText("Heritage Quest").assertExists()
    }

    @Test
    fun testCategoryNavigation() {
        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodesWithText("Tunisia Heritage").fetchSemanticsNodes().isNotEmpty()
        }

        // Click Categories
        composeTestRule.onNodeWithText("Categories").performClick()

        // Check if Category screen title exists
        composeTestRule.onNodeWithText("Choose a category").assertExists()
    }
}
