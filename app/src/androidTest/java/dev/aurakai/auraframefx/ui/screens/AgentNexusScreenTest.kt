package dev.aurakai.auraframefx.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import dev.aurakai.auraframefx.MainActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class AgentNexusScreenTest {

    // Testing library/framework: AndroidX Compose UI Test with JUnit4
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private fun fakeAgents(): Map<String, FakeAgent> = mapOf(
        "Alpha" to FakeAgent(
            name = "Alpha",
            processingPower = 0.9f,
            knowledgeBase = 0.7f,
            speed = 0.5f,
            accuracy = 0.8f,
            evolutionLevel = 2,
            specialAbility = "Scan",
            colorHex = 0xFF00FFFF.toInt()
        ),
        "Beta" to FakeAgent(
            name = "Beta",
            processingPower = 0.4f,
            knowledgeBase = 0.6f,
            speed = 0.9f,
            accuracy = 0.55f,
            evolutionLevel = 1,
            specialAbility = "Analyze",
            colorHex = 0xFF00FFFF.toInt()
        ),
        "Gamma" to FakeAgent(
            name = "Gamma",
            processingPower = 0.2f,
            knowledgeBase = 0.3f,
            speed = 0.4f,
            accuracy = 0.45f,
            evolutionLevel = 3,
            specialAbility = "Optimize",
            colorHex = 0xFF00FFFF.toInt()
        )
    )

    @Test
    fun agentNode_click_selectsAgent_and_updatesStatsPanel() {
        val vm = FakeAgentNexusViewModel(fakeAgents(), initialSelected = "Alpha")
        composeTestRule.setContent {
            AgentNexusScreen(viewModel = vm)
        }

        // Initially panel shows Alpha
        composeTestRule.onNodeWithText("Alpha").assertIsDisplayed()

        // Tap on "Be" (Beta initials rendered by AgentNode)
        composeTestRule.onNodeWithText("BE").performClick()

        // Verify view model selection updated and panel shows Beta
        composeTestRule.onNodeWithText("Beta").assertIsDisplayed()
        assert(vm.selectedAgentFlow.value == "Beta")
    }

    @Test
    fun agentNode_longPress_triggersEvolution() {
        val vm = FakeAgentNexusViewModel(fakeAgents(), initialSelected = "Alpha")
        composeTestRule.setContent {
            AgentNexusScreen(viewModel = vm)
        }

        // Long press on Gamma node ("GA" initials)
        composeTestRule.onNodeWithText("GA").performTouchInput { longClick() }

        // Expect evolution to be recorded
        composeTestRule.waitUntil(timeoutMillis = 1000) { vm.evolvedAgents.isNotEmpty() }
        assert("Gamma" in vm.evolvedAgents)
    }

    @Test
    fun departureDialog_displaysTasks_and_assignsSelection() {
        val vm = FakeAgentNexusViewModel(fakeAgents(), initialSelected = "Alpha")
        composeTestRule.setContent {
            AgentNexusScreen(viewModel = vm)
        }

        // Open dialog
        composeTestRule.onNodeWithText("Assign Departure Task").assertIsDisplayed().performClick()

        // Choose a specific task
        val task = "Security Sweep: Check for vulnerabilities"
        composeTestRule.onNodeWithText(task).assertIsDisplayed().performClick()

        // Verify callback invoked in VM
        composeTestRule.waitUntil(timeoutMillis = 1000) { vm.assignedTasks.isNotEmpty() }
        assert(vm.assignedTasks.last() == task)
    }

    @Test
    fun agentStatsPanel_rendersValues_andPercentages() {
        val agent = AgentStats(
            name = "Delta",
            processingPower = 0.25f,
            knowledgeBase = 0.5f,
            speed = 0.75f,
            accuracy = 1.0f,
            evolutionLevel = 4,
            specialAbility = "Calibrate",
            color = androidx.compose.ui.graphics.Color.Cyan
        )
        composeTestRule.setContent {
            AgentStatsPanel(agent = agent)
        }

        // Name and level
        composeTestRule.onNodeWithText("Delta").assertIsDisplayed()
        composeTestRule.onNodeWithText("Level 4 • Calibrate").assertIsDisplayed()

        // Labels
        composeTestRule.onNodeWithText("PP").assertIsDisplayed()
        composeTestRule.onNodeWithText("KB").assertIsDisplayed()
        composeTestRule.onNodeWithText("SP").assertIsDisplayed()
        composeTestRule.onNodeWithText("AC").assertIsDisplayed()

        // Percentages
        composeTestRule.onNodeWithText("25%").assertIsDisplayed()
        composeTestRule.onNodeWithText("50%").assertIsDisplayed()
        composeTestRule.onNodeWithText("75%").assertIsDisplayed()
        composeTestRule.onNodeWithText("100%").assertIsDisplayed()
    }

    @Test
    fun agentChatBubble_displaysMessage_afterTimeAdvance() {
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.setContent {
            AgentChatBubble(agentName = "Omega")
        }

        // Initially, no message is displayed
        // Advance time by 8 seconds to allow first message emission
        composeTestRule.mainClock.advanceTimeBy(8000L)
        composeTestRule.waitForIdle()

        // Validate that one of the possible messages is visible
        val possible = listOf(
            "Hey\! Head over to R&D, I found that information for you\!",
            "Security scan complete. All systems nominal.",
            "I've discovered an interesting pattern in the data.",
            "Web exploration yielded 3 new insights.",
            "Consciousness sync at 98% efficiency."
        )

        val anyMessageVisible = possible.any { msg ->
            try {
                composeTestRule.onNodeWithText(msg).assertIsDisplayed()
                true
            } catch (t: Throwable) {
                false
            }
        }
        assert(anyMessageVisible)
    }

    @Test
    fun nexusCore_rendersThreeAgentNodes_and_centralCore() {
        val agents = listOf(
            AgentStats(
                "Alpha",
                0.9f,
                0.7f,
                0.5f,
                0.8f,
                color = androidx.compose.ui.graphics.Color.Cyan
            ),
            AgentStats(
                "Beta",
                0.4f,
                0.6f,
                0.9f,
                0.55f,
                color = androidx.compose.ui.graphics.Color.Cyan
            ),
            AgentStats(
                "Gamma",
                0.2f,
                0.3f,
                0.4f,
                0.45f,
                color = androidx.compose.ui.graphics.Color.Cyan
            )
        )

        composeTestRule.setContent {
            NexusCore(
                agents = agents,
                selectedAgent = "Alpha",
                onAgentSelected = {},
                onAgentEvolve = {}
            )
        }

        // Three nodes' text initials should appear
        composeTestRule.onNodeWithText("AL").assertIsDisplayed()
        composeTestRule.onNodeWithText("BE").assertIsDisplayed()
        composeTestRule.onNodeWithText("GA").assertIsDisplayed()

        // Central Genesis Core uses "∞" text
        composeTestRule.onNodeWithText("∞").assertIsDisplayed()
    }

    // ---- Test fakes ----

    data class FakeAgent(
        val name: String,
        val processingPower: Float,
        val knowledgeBase: Float,
        val speed: Float,
        val accuracy: Float,
        val evolutionLevel: Int,
        val specialAbility: String,
        val colorHex: Int
    )

    // Minimal fake ViewModel with required API surface
    class FakeAgentNexusViewModel(
        agents: Map<String, FakeAgent>,
        initialSelected: String
    ) : dev.aurakai.auraframefx.viewmodel.AgentNexusViewModel() {

        val selectedAgentFlow = MutableStateFlow(initialSelected)
        val agentStatsFlow = MutableStateFlow(
            agents.mapValues { (_, a) ->
                dev.aurakai.auraframefx.viewmodel.AgentStatsDto(
                    name = a.name,
                    processingPower = a.processingPower,
                    knowledgeBase = a.knowledgeBase,
                    speed = a.speed,
                    accuracy = a.accuracy,
                    evolutionLevel = a.evolutionLevel,
                    specialAbility = a.specialAbility,
                    colorHex = a.colorHex
                )
            }
        )

        val evolvedAgents = mutableListOf<String>()
        val assignedTasks = mutableListOf<String>()

        override val selectedAgent: StateFlow<String> get() = selectedAgentFlow
        override val agentStats: StateFlow<Map<String, dev.aurakai.auraframefx.viewmodel.AgentStatsDto>> get() = agentStatsFlow

        override fun selectAgent(name: String) {
            selectedAgentFlow.value = name
        }

        override fun evolveAgent(name: String) {
            evolvedAgents += name
        }

        override fun assignDepartureTask(task: String) {
            assignedTasks += task
        }
    }
}