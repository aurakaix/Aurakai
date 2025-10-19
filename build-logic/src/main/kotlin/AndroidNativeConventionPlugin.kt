package dev.aurakai.auraframefx.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidNativeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        // Apply Java library plugin for Java support
        target.pluginManager.apply("java-library")
        // Apply Kotlin serialization plugin for multiplatform serialization
        target.pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")
        // Apply Android library plugin for NDK/native support
        target.pluginManager.apply("com.android.library")
        // Additional NDK/native configuration can be added here if needed
    }
}
