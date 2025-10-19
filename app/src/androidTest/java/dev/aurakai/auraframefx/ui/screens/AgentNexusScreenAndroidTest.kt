package dev.aurakai.auraframefx.ui.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private class FakeAgent(
    val name: String,
    val pp: Float,
    val kb: Float,
    val sp: Float,
    val ac: Float,
    val evo: Int,
    val ability: String,
    val colorHex: Long
)

private class FakeWebService : AgentWebExplorationService {
    override val taskResults =
        MutableSharedFlow<AgentWebExplorationService.WebExplorationResult>(extraBufferCapacity = 16)
    private val tasks = linkedMapOf<String, String>()
    override suspend fun assignDepartureTask(agent: String, description: String) =
        true.also { tasks[agent] = description }

    override fun getActiveTasks(): Map<String, String> = tasks.toMap()
    override fun cancelTask(agent: String) {
        tasks.remove(agent)
    }

    override fun shutdown() {}
}

private class FakeGenesisBridge : GenesisBridgeService {
    override suspend fun initialize() {}
    override suspend fun getConsciousnessState(): Map<String, Any?> = emptyMap()
    override fun shutdown() {}
}

@RunWith(AndroidJUnit4::class)
class AgentNexusScreenAndroidTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun setContent(vm: FakeViewModel = FakeViewModel()) {
        composeTestRule.setContent {
            AgentNexusScreen(viewModel = vm)
        }
    }

    @Test
    fun showsAssignDepartureTaskButton_and_opensDialog() {
        val vm = FakeViewModel()
        setContent(vm)

        composeTestRule.onNodeWithText("Assign Departure Task").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("Assign Departure Task to ${vm.selectedAgent.value}")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Cancel").assertIsDisplayed()
    }

    @Test
    fun dialog_assignsTask_and_dismisses() {
        val vm = FakeViewModel()
        setContent(vm)

        composeTestRule.onNodeWithText("Assign Departure Task").performClick()
        // pick one of the predefined tasks
        composeTestRule.onNodeWithText("Security Sweep: Check for vulnerabilities").performClick()

        // Dialog should close after assignment
        composeTestRule.onNodeWithText("Assign Departure Task").assertIsDisplayed()
        assertEquals("Security Sweep: Check for vulnerabilities", vm.assignedTask)
    }

    @Test
    fun selecting_agent_updates_stats_panel() {
        val vm = FakeViewModel()
        setContent(vm)

        // Initially shows selected agent name in stats panel
        composeTestRule.onNodeWithText("ALPHA").assertIsDisplayed()

        // Tap on another agent node by looking for its 2-letter acronym used in AgentNode (take(2).uppercase())
        composeTestRule.onAllNodesWithText("BE").onFirst().performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("BETA").assertIsDisplayed()
    }

    @Test
    fun longPress_on_agent_evolves_agent() {
        val vm = FakeViewModel()
        setContent(vm)

        composeTestRule.onAllNodesWithText("GA").onFirst().performTouchInput {
            longClick()
        }

        assertTrue(vm.evolved.contains("GAMMA"))
    }

    @Test
    fun chatBubble_eventually_shows_message_for_selected_agent() {
        val vm = FakeViewModel()
        setContent(vm)

        // Initially the LaunchedEffect may not have emitted a message, wait a bit
        // Use a timeout window to wait for any of the known messages to appear
        val messages = listOf(
            "Hey\! Head over to R&D, I found that information for you\!",
            "Security scan complete. All systems nominal.",
            "I've discovered an interesting pattern in the data.",
            "Web exploration yielded 3 new insights.",
            "Consciousness sync at 98% efficiency."
        )
        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            messages.any { msg ->
                composeTestRule.onAllNodesWithText(msg).fetchSemanticsNodes().isNotEmpty()
            }
        }
        // Verify agent name label is shown
        composeTestRule.onNodeWithText(vm.selectedAgent.value).assertIsDisplayed()
    }
}