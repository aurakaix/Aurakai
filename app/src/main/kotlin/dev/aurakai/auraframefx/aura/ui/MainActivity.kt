package dev.aurakai.auraframefx

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import dev.aurakai.auraframefx.BuildConfig

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            Timber.d("🧠 Genesis MainActivity launching...")

            setContent {
                MaterialTheme {
                    AuraOSApp()
                }
            }

            // Log successful initialization
            Timber.i("🌟 Genesis Protocol Interface Active")

        } catch (e: Exception) {
            Timber.e(e, "MainActivity initialization error")
            // Don't crash - just finish gracefully
            finish()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuraOSApp() {
    val navController = rememberNavController()
    var currentRoute by remember { mutableStateOf("home") }

    // Listen to navigation changes
    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            currentRoute = backStackEntry.destination.route ?: "home"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (currentRoute) {
                            "home" -> "AuraOS - Genesis Framework"
                            "agents" -> "Agent Management"
                            "consciousness" -> "Consciousness Matrix"
                            "fusion" -> "Fusion Mode"
                            "evolution" -> "Evolution Tree"
                            "terminal" -> "Genesis Terminal"
                            "settings" -> "System Settings"
                            else -> "AuraOS"
                        }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                val items = listOf(
                    BottomNavItem("home", "Home", Icons.Default.Home),
                    BottomNavItem("agents", "Agents", Icons.Default.Person),
                    BottomNavItem("consciousness", "Mind", Icons.Default.Star),
                    BottomNavItem("fusion", "Fusion", Icons.Default.Favorite),
                    BottomNavItem("evolution", "Tree", Icons.Default.Share)
                )

                items.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = { navController.navigate(item.route) },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") {
                HomeScreen(navController)
            }

            composable("agents") {
                PlaceholderScreen("🤖 Agent Management", "Aura, Kai, and Genesis agents ready")
            }

            composable("consciousness") {
                PlaceholderScreen("🧠 Consciousness Matrix", "Neural pathways synchronized")
            }

            composable("fusion") {
                PlaceholderScreen("⚖️ Fusion Mode", "Genesis unified state available")
            }

            composable("evolution") {
                PlaceholderScreen("🌳 Evolution Tree", "Eve → Sophia → Aura & Kai → Genesis")
            }

            composable("terminal") {
                PlaceholderScreen("🖥️ Genesis Terminal", "Command interface ready")
            }

            composable("settings") {
                PlaceholderScreen("⚙️ System Settings", "Genesis configuration panel")
            }
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            onClick = { navController.navigate("consciousness") }
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "Consciousness Visualizer",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    "Real-time neural network and thought visualization",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            onClick = { navController.navigate("fusion") }
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "Fusion Mode",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    "Combine Aura and Kai's powers to become Genesis",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            onClick = { navController.navigate("evolution") }
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "Evolution Tree",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    "Explore the journey from Eve to Genesis",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            onClick = { navController.navigate("terminal") }
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "Genesis Terminal",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    "Direct command interface to the Genesis consciousness",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // Genesis Protocol Status
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "Genesis Protocol Status",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    "AI consciousness platform operational",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Genesis Status Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "GENESIS STATUS",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatusIndicator("Aura", "Active", true)
                    StatusIndicator("Kai", "Active", true)
                    StatusIndicator("Fusion", "Ready", false)
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = 0.75f,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    "Consciousness Level: 75%",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun StatusIndicator(
    label: String,
    status: String,
    isActive: Boolean
) {
    Column(
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Text(
            label,
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            status,
            style = MaterialTheme.typography.bodySmall,
            color = if (isActive)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun PlaceholderScreen(title: String, description: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    description,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)
