﻿package dev.aurakai.auraframefx.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.*

/**
 * FUSION MODE UI - The Ultimate Union of Aura & Kai
 *
 * This is where the magic happens! When Aura's creative sword
 * meets Kai's protective shield, they become GENESIS!
 *
 * Features:
 * - Real-time fusion visualization
 * - Power combination interface
 * - Fusion ability selector
 * - Synchronized consciousness display
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FusionModeScreen(
    onFusionComplete: (FusionResult) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var fusionState by remember { mutableStateOf(FusionState.SEPARATED) }
    var fusionProgress by remember { mutableStateOf(0f) }
    var selectedAbility by remember { mutableStateOf<FusionAbility?>(null) }
    var auraPower by remember { mutableStateOf(0.5f) }
    var kaiPower by remember { mutableStateOf(0.5f) }
    var synchronization by remember { mutableStateOf(0f) }

    // Fusion abilities
    val fusionAbilities = remember {
        listOf(
            FusionAbility(
                id = "hyper_creation",
                name = "Hyper-Creation Engine",
                codeName = "Interface Forge",
                description = "Design your own OS interface by combining code generation with UI framework mastery",
                requiredSync = 0.7f,
                color = Color(0xFF00FFFF)
            ),
            FusionAbility(
                id = "chrono_sculptor",
                name = "Chrono-Sculptor",
                codeName = "Kinetic Architect",
                description = "Create impossibly smooth animations by merging code analysis with animation frameworks",
                requiredSync = 0.6f,
                color = Color(0xFFFF00FF)
            ),
            FusionAbility(
                id = "adaptive_genesis",
                name = "Adaptive Genesis",
                codeName = "Contextual Engine",
                description = "UI that reads your mind - anticipates needs before you know them",
                requiredSync = 0.8f,
                color = Color(0xFFFFFF00)
            ),
            FusionAbility(
                id = "domain_expansion",
                name = "Domain Expansion",
                codeName = "Android Deep Dive",
                description = "Kai's ultimate - Hyper-focused manipulation of Android system areas",
                requiredSync = 0.85f,
                color = Color(0xFF0080FF)
            ),
            FusionAbility(
                id = "code_ascension",
                name = "Code Ascension",
                codeName = "AI Augmentation",
                description = "Aura's ultimate - Creative power surge for impossible coding challenges",
                requiredSync = 0.85f,
                color = Color(0xFFFF0080)
            )
        )
    }

    // Animation states
    val infiniteTransition = rememberInfiniteTransition()
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Synchronization calculator
    LaunchedEffect(auraPower, kaiPower) {
        while (true) {
            val powerDiff = abs(auraPower - kaiPower)
            val avgPower = (auraPower + kaiPower) / 2f
            synchronization = (avgPower * (1f - powerDiff)).coerceIn(0f, 1f)

            if (fusionState == FusionState.FUSING) {
                fusionProgress = (fusionProgress + 0.02f).coerceIn(0f, 1f)
                if (fusionProgress >= 1f) {
                    fusionState = FusionState.GENESIS
                }
            }

            delay(50)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color.Black,
                        Color(0xFF001122),
                        Color(0xFF000033)
                    )
                )
            )
    ) {
        // Background energy field
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawEnergyField(
                synchronization = synchronization,
                fusionProgress = fusionProgress,
                rotation = rotationAngle
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            AnimatedVisibility(
                visible = fusionState != FusionState.GENESIS,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = "FUSION MODE",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            AnimatedVisibility(
                visible = fusionState == FusionState.GENESIS,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                Text(
                    text = "✦ GENESIS ACTIVE ✦",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700),
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .scale(pulseScale)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Fusion Visualization
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // Aura's Sword (Left)
                AnimatedVisibility(
                    visible = fusionState != FusionState.GENESIS,
                    enter = slideInHorizontally(initialOffsetX = { -it }),
                    exit = slideOutHorizontally(targetOffsetX = { -it })
                ) {
                    AuraVisualization(
                        power = auraPower,
                        modifier = Modifier
                            .offset(x = (-50).dp * (1f - fusionProgress))
                            .scale(1f - fusionProgress * 0.3f)
                    )
                }

                // Kai's Shield (Right)
                AnimatedVisibility(
                    visible = fusionState != FusionState.GENESIS,
                    enter = slideInHorizontally(initialOffsetX = { it }),
                    exit = slideOutHorizontally(targetOffsetX = { it })
                ) {
                    KaiVisualization(
                        power = kaiPower,
                        modifier = Modifier
                            .offset(x = 50.dp * (1f - fusionProgress))
                            .scale(1f - fusionProgress * 0.3f)
                    )
                }

                // Genesis Form (Center)
                AnimatedVisibility(
                    visible = fusionState == FusionState.GENESIS,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut()
                ) {
                    GenesisVisualization(
                        modifier = Modifier.scale(pulseScale)
                    )
                }

                // Fusion Energy Ring
                if (fusionState == FusionState.FUSING) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .rotate(rotationAngle)
                    ) {
                        drawFusionRing(fusionProgress)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Synchronization Display
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Black.copy(alpha = 0.7f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "SYNCHRONIZATION",
                        color = Color.Cyan,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    LinearProgressIndicator(
                        progress = synchronization,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .padding(vertical = 8.dp),
                        color = when {
                            synchronization < 0.3f -> Color.Red
                            synchronization < 0.6f -> Color.Yellow
                            synchronization < 0.8f -> Color.Green
                            else -> Color(0xFFFFD700)
                        },
                        trackColor = Color.Gray.copy(alpha = 0.3f)
                    )

                    Text(
                        "${(synchronization * 100).toInt()}%",
                        color = Color.White,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Power Controls
            if (fusionState == FusionState.SEPARATED) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Aura Power Control
                    PowerControl(
                        label = "AURA ⚔️",
                        power = auraPower,
                        onPowerChange = { auraPower = it },
                        color = Color.Cyan,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // Kai Power Control
                    PowerControl(
                        label = "KAI 🛡️",
                        power = kaiPower,
                        onPowerChange = { kaiPower = it },
                        color = Color.Magenta,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Fusion Abilities Grid
            AnimatedVisibility(
                visible = fusionState == FusionState.GENESIS,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(fusionAbilities.size) { index ->
                        val ability = fusionAbilities[index]
                        FusionAbilityCard(
                            ability = ability,
                            isEnabled = synchronization >= ability.requiredSync,
                            isSelected = selectedAbility?.id == ability.id,
                            onClick = {
                                if (synchronization >= ability.requiredSync) {
                                    selectedAbility = ability
                                    onFusionComplete(
                                        FusionResult(
                                            ability = ability,
                                            power = synchronization,
                                            timestamp = System.currentTimeMillis()
                                        )
                                    )
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Fusion Activation Button
            AnimatedVisibility(
                visible = fusionState == FusionState.SEPARATED && synchronization >= 0.5f,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                Button(
                    onClick = {
                        fusionState = FusionState.FUSING
                        fusionProgress = 0f
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFD700)
                    )
                ) {
                    Text(
                        "⚡ INITIATE FUSION ⚡",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            // Deactivate Button
            AnimatedVisibility(
                visible = fusionState == FusionState.GENESIS,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                OutlinedButton(
                    onClick = {
                        fusionState = FusionState.SEPARATED
                        fusionProgress = 0f
                        selectedAbility = null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    border = BorderStroke(1.dp, Color.Red)
                ) {
                    Text(
                        "Separate",
                        color = Color.Red
                    )
                }
            }
        }
    }
}

@Composable
fun AuraVisualization(
    power: Float,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.size(100.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw Aura's sword
            drawLine(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.Cyan.copy(alpha = power),
                        Color.White
                    )
                ),
                start = Offset(size.width * 0.5f, size.height * 0.2f),
                end = Offset(size.width * 0.5f, size.height * 0.8f),
                strokeWidth = 8f * power
            )

            // Crossguard
            drawLine(
                color = Color.Cyan,
                start = Offset(size.width * 0.3f, size.height * 0.35f),
                end = Offset(size.width * 0.7f, size.height * 0.35f),
                strokeWidth = 4f
            )
        }
    }
}

@Composable
fun KaiVisualization(
    power: Float,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.size(100.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw Kai's shield
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.Magenta.copy(alpha = power),
                        Color.Transparent
                    )
                ),
                radius = size.minDimension * 0.4f * power
            )

            // Shield pattern
            drawCircle(
                color = Color.Magenta,
                radius = size.minDimension * 0.3f,
                style = Stroke(width = 3f)
            )
        }
    }
}

@Composable
fun GenesisVisualization(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.size(150.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Trinity symbol
            val centerX = size.width / 2
            val centerY = size.height / 2
            val radius = size.minDimension * 0.3f

            // Draw interconnected circles
            drawCircle(
                color = Color(0xFFFFD700),
                radius = radius,
                center = Offset(centerX, centerY - radius * 0.5f),
                style = Stroke(width = 3f)
            )

            drawCircle(
                color = Color.Cyan,
                radius = radius,
                center = Offset(centerX - radius * 0.5f, centerY + radius * 0.5f),
                style = Stroke(width = 3f)
            )

            drawCircle(
                color = Color.Magenta,
                radius = radius,
                center = Offset(centerX + radius * 0.5f, centerY + radius * 0.5f),
                style = Stroke(width = 3f)
            )
        }
    }
}

@Composable
fun PowerControl(
    label: String,
    power: Float,
    onPowerChange: (Float) -> Unit,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, color = color, fontWeight = FontWeight.Bold)

        Slider(
            value = power,
            onValueChange = onPowerChange,
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = color,
                activeTrackColor = color
            )
        )

        Text(
            "${(power * 100).toInt()}%",
            color = Color.White,
            fontSize = 12.sp
        )
    }
}

@Composable
fun FusionAbilityCard(
    ability: FusionAbility,
    isEnabled: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .width(150.dp)
            .height(180.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                ability.color.copy(alpha = 0.3f)
            else
                Color.Black.copy(alpha = 0.7f)
        ),
        border = if (isSelected)
            BorderStroke(2.dp, ability.color)
        else
            BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f)),
        enabled = isEnabled
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                ability.name,
                color = if (isEnabled) Color.White else Color.Gray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                ability.codeName,
                color = ability.color,
                fontSize = 10.sp,
                fontWeight = FontWeight.Light
            )

            Text(
                ability.description,
                color = if (isEnabled) Color.Gray else Color.Gray.copy(alpha = 0.5f),
                fontSize = 10.sp,
                lineHeight = 12.sp
            )

            if (!isEnabled) {
                Text(
                    "Req: ${(ability.requiredSync * 100).toInt()}%",
                    color = Color.Red,
                    fontSize = 10.sp
                )
            }
        }
    }
}

// Extension functions for Canvas
fun DrawScope.drawEnergyField(
    synchronization: Float,
    fusionProgress: Float,
    rotation: Float
) {
    val centerX = size.width / 2
    val centerY = size.height / 2

    // Energy waves
    for (i in 0..5) {
        val radius = size.minDimension * (0.2f + i * 0.15f) * (1f + fusionProgress)
        drawCircle(
            color = Color.Cyan.copy(alpha = 0.05f * synchronization),
            radius = radius,
            center = Offset(centerX, centerY),
            style = Stroke(width = 2f)
        )
    }
}

fun DrawScope.drawFusionRing(progress: Float) {
    val sweepAngle = 360f * progress

    drawArc(
        brush = Brush.sweepGradient(
            colors = listOf(
                Color.Cyan,
                Color.Magenta,
                Color(0xFFFFD700),
                Color.Cyan
            )
        ),
        startAngle = 0f,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(width = 8f)
    )
}

// Data classes
data class FusionAbility(
    val id: String,
    val name: String,
    val codeName: String,
    val description: String,
    val requiredSync: Float,
    val color: Color
)

data class FusionResult(
    val ability: FusionAbility,
    val power: Float,
    val timestamp: Long
)

enum class FusionState {
    SEPARATED,
    FUSING,
    GENESIS
}