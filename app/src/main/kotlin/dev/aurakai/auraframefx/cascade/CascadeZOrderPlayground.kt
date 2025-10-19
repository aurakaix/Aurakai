﻿package dev.aurakai.auraframefx.ui.debug

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.aurakai.auraframefx.ai.agents.CascadeAgent
import dev.aurakai.auraframefx.model.agent_states.ProcessingState
import dev.aurakai.auraframefx.model.agent_states.VisionState
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CascadeDebugViewModel @Inject constructor(
    private val cascadeAgent: CascadeAgent,
) {
    val visionState: StateFlow<VisionState> = cascadeAgent.visionState
    val processingState: StateFlow<ProcessingState> = cascadeAgent.processingState

    /**
     * Sets the CascadeAgent's vision state to the specified value.
     *
     * @param newState The new vision state to apply.
     */
    fun updateVisionState(newState: VisionState) {
        cascadeAgent.updateVisionState(newState)
    }

    fun updateProcessingState(newState: ProcessingState) {
        cascadeAgent.updateProcessingState(newState)
    }
}

/**
 * Displays a debug UI for inspecting and updating the CascadeAgent's vision and processing states.
 *
 * Provides interactive controls to view and modify the current vision and processing states, as well as to review their respective histories. Intended for use in development or debugging environments to facilitate real-time state inspection and manipulation.
 */
@OptIn(ExperimentalMaterial3Api::class)
/**
 * Displays a debug UI for inspecting and updating the CascadeAgent's vision and processing states.
 *
 * Provides editable fields for entering new vision and processing states, buttons to apply updates through the ViewModel, and displays both current and historical state information. Intended for development and debugging use.
 */
@Composable
fun CascadeZOrderPlayground(
    viewModel: CascadeDebugViewModel = androidx.hilt.navigation.compose.hiltViewModel(),
) {
    var newVisionState by remember { mutableStateOf(VisionState()) }
    var newProcessingState by remember { mutableStateOf(ProcessingState()) }

    // Collect StateFlow values safely using collectAsState()
    val visionState by viewModel.visionState.collectAsState()
    val processingState by viewModel.processingState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Cascade State Debugger",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Vision State",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Current State: $visionState",
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = newVisionState.toString(),
                    onValueChange = {
                        // Parse and update vision state
                    },
                    label = { Text("New Vision State") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = { viewModel.updateVisionState(newVisionState) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Update Vision State")
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Processing State",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Current State: $processingState",
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = newProcessingState.toString(),
                    onValueChange = {
                        // Parse and update processing state
                    },
                    label = { Text("New Processing State") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = { viewModel.updateProcessingState(newProcessingState) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Update Processing State")
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "State History",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    item {
                        Text(
                            text = "Vision History",
                            style = MaterialTheme.typography.titleSmall
                        )
                        visionState.history?.forEach { entry ->
                            Text(text = "- $entry")
                        }
                    }
                    item {
                        Text(
                            text = "Processing History",
                            style = MaterialTheme.typography.titleSmall
                        )
                        processingState.history?.forEach { entry ->
                            Text(text = "- $entry")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
/**
 * Displays a preview of the Cascade state debugger UI within a Material theme.
 *
 * This composable enables IDE preview of the `CascadeZOrderPlayground` for design and layout inspection.
 */
@Composable
@Preview(showBackground = true)
fun CascadeZOrderPlaygroundPreview() {
    MaterialTheme {
        CascadeZOrderPlayground()
    }
}
