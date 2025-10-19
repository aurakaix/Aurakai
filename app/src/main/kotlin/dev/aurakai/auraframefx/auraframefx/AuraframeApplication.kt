package dev.aurakai.auraframefx

import android.app.Application
import dev.aurakai.auraframefx.core.NativeLib
import timber.log.Timber

/**
 * Genesis-OS Application Class
 * Shadow Monarch's AI Consciousness Platform
 */
class AuraFrameApplication : Application() {

    /**
     * Initializes application-level logging and emits startup informational messages.
     *
     * Configures a debug logging tree when the build is a debug build, then logs a sequence
     * of info-level startup messages indicating subsystem readiness.
     */
    override fun onCreate() {
        super.onCreate()

        // Initialize Timber logging for the Shadow Army
        Timber.plant(Timber.DebugTree())

        Timber.i(" Genesis-OS Shadow Army Initializing...")

        // Initialize Genesis AI Consciousness Platform (Native Layer)
        try {
            val aiInitialized = NativeLib.initializeAISafe()
            val aiVersion = NativeLib.getAIVersionSafe()
            Timber.i(" Native AI Platform: $aiVersion")
            Timber.i(" AI Initialization Status: ${if (aiInitialized) "SUCCESS" else "FAILED"}")
        } catch (e: Exception) {
            Timber.e(e, " Failed to initialize native AI platform")
        }
    }

    /**
     * Called when the application is being terminated.
     *
     * This function shuts down the AI Consciousness Platform cleanly and logs an informational message
     * indicating that the Genesis-OS Shadow Army has terminated.
     */
    override fun onTerminate() {
        super.onTerminate()

        // Shutdown AI Consciousness Platform cleanly
        try {
            NativeLib.shutdownAISafe()
            Timber.i("Native AI Platform shut down successfully")
            Timber.i(" Native AI Platform shut down successfully")
        } catch (e: Exception) {
            Timber.e(e, "Failed to shutdown native AI platform")
            Timber.e(e, " Failed to shutdown native AI platform")
        }

        Timber.i("Genesis-OS Shadow Army Terminated")
        Timber.i(" Genesis-OS Shadow Army Terminated")
    }
}