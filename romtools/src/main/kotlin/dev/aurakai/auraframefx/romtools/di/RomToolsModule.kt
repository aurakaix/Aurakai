// File: romtools/src/main/kotlin/dev/aurakai/auraframefx/romtools/di/RomToolsModule.kt
package dev.aurakai.auraframefx.romtools.di

import android.content.Context
// Hilt imports temporarily commented out
// import dagger.Binds
// import dagger.Module
// import dagger.Provides
// import dagger.hilt.InstallIn
// import dagger.hilt.android.qualifiers.ApplicationContext
// import dagger.hilt.components.SingletonComponent
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.aurakai.auraframefx.romtools.BackupManager
import dev.aurakai.auraframefx.romtools.BackupManagerImpl
import dev.aurakai.auraframefx.romtools.FlashManager
import dev.aurakai.auraframefx.romtools.FlashManagerImpl
import dev.aurakai.auraframefx.romtools.RecoveryManager
import dev.aurakai.auraframefx.romtools.RecoveryManagerImpl
import dev.aurakai.auraframefx.romtools.RomVerificationManager
import dev.aurakai.auraframefx.romtools.RomVerificationManagerImpl
import dev.aurakai.auraframefx.romtools.SystemModificationManager
import dev.aurakai.auraframefx.romtools.SystemModificationManagerImpl
import dev.aurakai.auraframefx.romtools.bootloader.BootloaderManager
import dev.aurakai.auraframefx.romtools.bootloader.BootloaderManagerImpl
import javax.inject.Singleton

/**
 * ROM Tools module - Hilt temporarily disabled.
 * TODO: Re-enable when Hilt plugin configuration is resolved.
 */
// @Module
// @InstallIn(SingletonComponent::class)
class RomToolsModule {

    // @Binds
    // @Singleton
    fun bindBootloaderManager(
        bootloaderManagerImpl: BootloaderManagerImpl
    ): BootloaderManager = bootloaderManagerImpl

    // @Binds
    // @Singleton
    fun bindRecoveryManager(
        recoveryManagerImpl: RecoveryManagerImpl
    ): RecoveryManager = recoveryManagerImpl

    // @Binds
    // @Singleton
    fun bindSystemModificationManager(
        systemModificationManagerImpl: SystemModificationManagerImpl
    ): SystemModificationManager = systemModificationManagerImpl

    // @Binds
    // @Singleton
    fun bindFlashManager(
        flashManagerImpl: FlashManagerImpl
    ): FlashManager = flashManagerImpl

    // @Binds
    // @Singleton
    fun bindRomVerificationManager(
        romVerificationManagerImpl: RomVerificationManagerImpl
    ): RomVerificationManager = romVerificationManagerImpl

    // @Binds
    // @Singleton
    fun bindBackupManager(
        backupManagerImpl: BackupManagerImpl
    ): BackupManager = backupManagerImpl

    companion object {

        // @Provides
        // @RomToolsDataDir
        fun provideRomToolsDataDirectory(
            // @ApplicationContext
            context: Context
        ): String {
            return "${context.filesDir}/romtools"
        }

        // @Provides
        // @RomToolsBackupDir
        fun provideRomToolsBackupDirectory(
            // @ApplicationContext
            context: Context
        ): String {
            return "${context.getExternalFilesDir(null)}/backups"
        }

        // @Provides
        // @RomToolsDownloadDir
        fun provideRomToolsDownloadDirectory(
            // @ApplicationContext
            context: Context
        ): String {
            return "${context.getExternalFilesDir(null)}/downloads"
        }

        // @Provides
        // @RomToolsTempDir
        fun provideRomToolsTempDirectory(
            // @ApplicationContext
            context: Context
        ): String {
            return "${context.cacheDir}/romtools_temp"
        }
    }
}

// Qualifier annotations for ROM tools directories
@Retention(AnnotationRetention.BINARY)
annotation class RomToolsDataDir

/**
 * Qualifier for the backup directory for the ROM tools.
 */
@Retention(AnnotationRetention.BINARY)
annotation class RomToolsBackupDir

/**
 * Qualifier for the download directory for the ROM tools.
 */
@Retention(AnnotationRetention.BINARY)
annotation class RomToolsDownloadDir

/**
 * Qualifier for the temporary directory for the ROM tools.
 */
@Retention(AnnotationRetention.BINARY)
annotation class RomToolsTempDir
