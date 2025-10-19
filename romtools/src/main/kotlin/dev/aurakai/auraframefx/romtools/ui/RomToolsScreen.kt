import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) { Text(text = "${operation.progress.toInt()}%", color = Color.White, fontSize = 14.sp) }
package dev.aurakai.auraframefx.romtools.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
    val icon: ImageVector,
    val color: Color = Color.White,
    val requiresRoot: Boolean = false,
    val requiresBootloader: Boolean = false,
    val requiresRecovery: Boolean = false,
    val requiresSystem: Boolean = false
)

private fun getRomToolsActions(): List<RomToolAction> = listOf(
    RomToolAction(RomActionType.FLASH_ROM, "Flash Custom ROM", "Install a custom ROM on your device", Icons.Default.FlashOn, Color(0xFFFF6B35), requiresRoot = true, requiresBootloader = true),
    RomToolAction(RomActionType.CREATE_BACKUP, "Create NANDroid Backup", "Create a full system backup", Icons.Default.Backup, Color(0xFF4CAF50), requiresRoot = true, requiresRecovery = true),
    RomToolAction(RomActionType.RESTORE_BACKUP, "Restore Backup", "Restore from a previous backup", Icons.Default.Restore, Color(0xFF2196F3), requiresRoot = true, requiresRecovery = true),
    RomToolAction(RomActionType.UNLOCK_BOOTLOADER, "Unlock Bootloader", "Unlock device bootloader for modifications", Icons.Default.LockOpen, Color(0xFFFF9800)),
    RomToolAction(RomActionType.INSTALL_RECOVERY, "Install Custom Recovery", "Install TWRP or other custom recovery", Icons.Default.Healing, Color(0xFF9C27B0), requiresRoot = true, requiresBootloader = true),
    RomToolAction(RomActionType.GENESIS_OPTIMIZATIONS, "Genesis AI Optimizations", "Apply AI-powered system optimizations", Icons.Default.Psychology, Color(0xFF00E676), requiresRoot = true, requiresSystem = true)
)

data class RomToolsState(
    val capabilities: RomCapabilities? = null,
    val isInitialized: Boolean = false,
    val availableRoms: List<dev.aurakai.auraframefx.romtools.AvailableRom> = emptyList(),
// ------------------------- Previews -------------------------
    onActionClick: (RomToolAction) -> Unit = {}
) {
    Column(
private fun RomToolsScreenPreview() {
    val capabilities = RomCapabilities(true, true, false, true, listOf("arm64-v8a"), "Pixel 8 Pro", "14", "2023-10-01")
    val roms = listOf(dev.aurakai.auraframefx.romtools.AvailableRom("AuraOS", "1.0", "14", "", 2147483648L, "abc", "The best ROM", "AuraKai", System.currentTimeMillis()))
    val backups = listOf(BackupInfo("MyBackup", "/sdcard/backups", 1073741824L, System.currentTimeMillis(), "Pixel 8 Pro", "14", listOf("system")))
    val state = RomToolsState(capabilities = capabilities, isInitialized = true, availableRoms = roms, backups = backups)
    RomToolsScreen(romToolsState = state, operationProgress = null)
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(Color(0xFF0A0A0A), Color(0xFF1A1A1A), Color(0xFF0A0A0A))
                )
            )
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "ROM Tools",
                    color = Color(0xFFFF6B35),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Black.copy(alpha = 0.8f))
        )

        if (!romToolsState.isInitialized) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    CircularProgressIndicator(color = Color(0xFFFF6B35), strokeWidth = 3.dp)
                    Text(text = "Initializing ROM Tools...", color = Color.White, fontSize = 14.sp)
                }
            }
            return
        }

        val actions = getRomToolsActions()
        val roms = romToolsState.availableRoms
        val backups = romToolsState.backups

        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item { DeviceCapabilitiesCard(capabilities = romToolsState.capabilities) }

            if (operationProgress != null) item { OperationProgressCard(operation = operationProgress) }

            item {
                Text(text = "ROM Operations", color = Color(0xFFFF6B35), fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp))
            }

            items(actions) { action ->
                RomToolActionCard(action = action, isEnabled = action.isEnabled(romToolsState.capabilities), onClick = { onActionClick(action) })
            }

            if (roms.isNotEmpty()) {
                item { Text(text = "Available ROMs", color = Color(0xFFFF6B35), fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp)) }
                items(roms) { rom -> AvailableRomCard(rom = rom) }
            }

            if (backups.isNotEmpty()) {
                item { Text(text = "Backups", color = Color(0xFFFF6B35), fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp)) }
                items(backups) { backup -> BackupCard(backup = backup) }
            }
        }
    }
}

// Helpers

@Composable
private fun BackupCard(backup: BackupInfo, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = backup.name, color = Color(0xFFFF6B35), fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(text = "Size: ${backup.size}", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            Text(text = "Date: ${backup.createdAt}", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
        }
    }
}

@Composable
private fun DeviceCapabilitiesCard(capabilities: RomCapabilities?, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(text = "Device Capabilities", color = Color(0xFFFF6B35), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            if (capabilities != null) {
                CapabilityRow("Root Access", capabilities.hasRootAccess)
                CapabilityRow("Bootloader Access", capabilities.hasBootloaderAccess)
                CapabilityRow("Recovery Access", capabilities.hasRecoveryAccess)
                CapabilityRow("System Write Access", capabilities.hasSystemWriteAccess)
                Spacer(modifier = Modifier.height(8.dp))
                InfoRow("Device", capabilities.deviceModel)
                InfoRow("Android", capabilities.androidVersion)
                InfoRow("Security Patch", capabilities.securityPatchLevel)
            } else {
                Text(text = "Checking capabilities...", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun CapabilityRow(label: String, hasCapability: Boolean) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(text = label, color = Color.White, fontSize = 14.sp)
        Icon(imageVector = if (hasCapability) Icons.Default.CheckCircle else Icons.Default.Cancel, contentDescription = null, tint = if (hasCapability) Color(0xFF4CAF50) else Color(0xFFF44336), modifier = Modifier.size(20.dp))
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = "$label:", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
        Text(text = value, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun OperationProgressCard(operation: dev.aurakai.auraframefx.romtools.OperationProgress, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF2E2E2E)), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(text = operation.operation.getDisplayName(), color = Color(0xFFFF6B35), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            LinearProgressIndicator(progress = operation.progress / 100f, modifier = Modifier.fillMaxWidth(), color = Color(0xFFFF6B35), trackColor = Color(0xFF444444))
        }
    }
}

@Composable
private fun RomToolActionCard(action: RomToolAction, isEnabled: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth().clickable(enabled = isEnabled) { onClick() }, colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E), disabledContainerColor = Color(0xFF111111)), shape = RoundedCornerShape(12.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = action.icon, contentDescription = null, tint = if (isEnabled) action.color else Color.Gray, modifier = Modifier.size(32.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = action.title, color = if (isEnabled) Color.White else Color.Gray, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Text(text = action.description, color = if (isEnabled) Color.White.copy(alpha = 0.7f) else Color.Gray.copy(alpha = 0.5f), fontSize = 12.sp)
            }
            if (!isEnabled) Icon(imageVector = Icons.Default.Lock, contentDescription = "Locked", tint = Color.Gray, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun AvailableRomCard(rom: dev.aurakai.auraframefx.romtools.AvailableRom) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = rom.name, color = Color(0xFFFF6B35), fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(text = "Version: ${rom.version}", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            Text(text = "Android: ${rom.androidVersion}", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            Text(text = "Size: ${rom.size}", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
        }
    }
}

// ------------------------- Data & Helpers -------------------------

data class RomToolAction(
    val type: RomActionType,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color = Color.White,
    val requiresRoot: Boolean = false,
    val requiresBootloader: Boolean = false,
    val requiresRecovery: Boolean = false,
    val requiresSystem: Boolean = false
) {
    fun isEnabled(capabilities: RomCapabilities?): Boolean {
        if (capabilities == null) return false
        return (!requiresRoot || capabilities.hasRootAccess) &&
                (!requiresBootloader || capabilities.hasBootloaderAccess) &&
                (!requiresRecovery || capabilities.hasRecoveryAccess) &&
                (!requiresSystem || capabilities.hasSystemWriteAccess)
    }
}


/**
 * Provide a human-readable display name for a RomOperation.
 *
 * @return The human-readable display name corresponding to this operation.
 */
fun dev.aurakai.auraframefx.romtools.RomOperation.getDisplayName(): String {
    return when (this) {
        dev.aurakai.auraframefx.romtools.RomOperation.VERIFYING_ROM -> "Verifying ROM"
        dev.aurakai.auraframefx.romtools.RomOperation.CREATING_BACKUP -> "Creating Backup"
        dev.aurakai.auraframefx.romtools.RomOperation.UNLOCKING_BOOTLOADER -> "Unlocking Bootloader"
        dev.aurakai.auraframefx.romtools.RomOperation.INSTALLING_RECOVERY -> "Installing Recovery"
        dev.aurakai.auraframefx.romtools.RomOperation.FLASHING_ROM -> "Flashing ROM"
        dev.aurakai.auraframefx.romtools.RomOperation.VERIFYING_INSTALLATION -> "Verifying Installation"
        dev.aurakai.auraframefx.romtools.RomOperation.RESTORING_BACKUP -> "Restoring Backup"
        dev.aurakai.auraframefx.romtools.RomOperation.APPLYING_OPTIMIZATIONS -> "Applying Optimizations"
        dev.aurakai.auraframefx.romtools.RomOperation.DOWNLOADING_ROM -> "Downloading ROM"
        dev.aurakai.auraframefx.romtools.RomOperation.COMPLETED -> "Completed"
        dev.aurakai.auraframefx.romtools.RomOperation.FAILED -> "Failed"
    }
}

/**
 * Displays a card summarizing an available ROM, showing key metadata such as name, version,
 * Android target, size, and maintainer.
 *
 * @param rom The AvailableRom whose information is rendered in the card. */
@Composable
private fun AvailableRomCard(rom: dev.aurakai.auraframefx.romtools.AvailableRom) {
    // Implementation for available ROM card
}

@Preview
@Composable
    // Implementation for backup card
}

}

// NOTE: For real file operations, use context.getExternalFilesDir() or similar instead of hardcoded /sdcard paths.
// Example:
// val backupDir = context.getExternalFilesDir("backups")
// val backupPath = backupDir?.absolutePath ?: ""
