package dev.aurakai.auraframefx.romtools

import dev.aurakai.auraframefx.romtools.bootloader.BootloaderManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf

/**
 * Fake implementation of RomToolsManager for Jetpack Compose previews.
 * Provides a pre-initialized state for UI rendering in previews.
 */
open class FakeRomToolsManager : RomToolsManager(
    context = null,
    bootloaderManager = FakeBootloaderManager(),
    recoveryManager = FakeRecoveryManager(),
    systemModificationManager = FakeSystemModificationManager(),
    flashManager = FakeFlashManager(),
    verificationManager = FakeRomVerificationManager(),
    backupManager = FakeBackupManager()
) {
    private val fakeState: RomToolsState = RomToolsState(
        isInitialized = true,
        capabilities = RomCapabilities(
            hasRootAccess = true,
            hasBootloaderAccess = true,
            hasRecoveryAccess = true,
            hasSystemWriteAccess = true,
            supportedArchitectures = listOf("arm64-v8a", "armeabi-v7a"),
            deviceModel = "Pixel 7 Pro",
            androidVersion = "14",
            securityPatchLevel = "2025-10-05"
        )
    )

    override val romToolsState: StateFlow<RomToolsState>
        get() = MutableStateFlow(fakeState)

    override val operationProgress: StateFlow<OperationProgress?>
        get() = MutableStateFlow(null)
}

// Minimal fake managers for preview implementing the interfaces
class FakeBootloaderManager : BootloaderManager {
    override fun checkBootloaderAccess(): Boolean = true
    override fun isBootloaderUnlocked(): Boolean = true
    override suspend fun unlockBootloader(): Result<Unit> = Result.success(Unit)
}

class FakeRecoveryManager : RecoveryManager {
    override fun checkRecoveryAccess(): Boolean = true
    override fun isCustomRecoveryInstalled(): Boolean = true
    override suspend fun installCustomRecovery(): Result<Unit> = Result.success(Unit)
}

class FakeSystemModificationManager : SystemModificationManager {
    override fun checkSystemWriteAccess(): Boolean = true
    override suspend fun installGenesisOptimizations(progressCallback: (Float) -> Unit): Result<Unit> = Result.success(Unit)
}

class FakeFlashManager : FlashManager {
    override suspend fun flashRom(romFile: RomFile, progressCallback: (Float) -> Unit): Result<Unit> = Result.success(Unit)
    override suspend fun downloadRom(rom: AvailableRom): Flow<DownloadProgress> = flowOf(DownloadProgress(0, 0, 0f, 0))
}

class FakeRomVerificationManager : RomVerificationManager {
    override suspend fun verifyRomFile(romFile: RomFile): Result<Unit> = Result.success(Unit)
    override suspend fun verifyInstallation(): Result<Unit> = Result.success(Unit)
}

class FakeBackupManager : BackupManager {
    override suspend fun createFullBackup(): Result<Unit> = Result.success(Unit)
    override suspend fun createNandroidBackup(name: String, progressCallback: (Float) -> Unit): Result<BackupInfo> =
        Result.success(BackupInfo("fake-backup", "", 0, 0, "", "", emptyList()))
    override suspend fun restoreNandroidBackup(backup: BackupInfo, progressCallback: (Float) -> Unit): Result<Unit> = Result.success(Unit)
}

