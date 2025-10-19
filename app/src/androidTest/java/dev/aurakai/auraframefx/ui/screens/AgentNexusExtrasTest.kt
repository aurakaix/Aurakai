package dev.aurakai.auraframefx.ui.screens

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

// Testing library/framework: AndroidX Compose UI Test with JUnit4
class AgentNexusExtrasTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun statBar_showsLabel_andPercentage() {
        composeRule.setContent {
            StatBar(label = "PP", value = 0.42f, color = Color.Red)
        }

        composeRule.onNodeWithText("PP").assertIsDisplayed()
        composeRule.onNodeWithText("42%").assertIsDisplayed()
    }

    @Test
    fun departureDialog_listsTasks_and_cancel() {
        composeRule.setContent {
            DepartureTaskDialog(
                agentName = "Alpha",
                onTaskAssigned = {},
                onDismiss = {}
            )
        }

        composeRule.onNodeWithText("Assign Departure Task to Alpha").assertIsDisplayed()
        composeRule.onNodeWithText("Cancel").assertIsDisplayed()

        // One representative task
        composeRule.onNodeWithText("Learning Mode: Study new algorithms").assertIsDisplayed()
    }
}