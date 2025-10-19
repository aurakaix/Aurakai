// ==== GENESIS PROTOCOL - ANDROID HILT CONVENTION ====
// Hilt dependency injection configuration for Android modules

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltConventionPlugin : Plugin<Project> {
    /**
     * Configures a Gradle Project to enable Dagger Hilt for Android modules.
     *
     * Ensures an Android plugin is present (applies a library convention if missing), waits for the appropriate Android plugin to be applied, applies the Android base shim, Dagger Hilt and KSP plugins, and adds `hilt-android` (implementation) and `hilt-compiler` (ksp) from the `libs` version catalog.
     *
     * @param target The Gradle Project to configure.
     */
    override fun apply(target: Project) {
        with(target) {
            // Ensure Android plugin is applied FIRST
            val hasApp = plugins.hasPlugin("com.android.application")
            val hasLib = plugins.hasPlugin("com.android.library")

            if (!hasApp && !hasLib) {
                pluginManager.apply("genesis.android.library")
            }

            // Apply KSP later, after Hilt
            // Use pluginManager.withPlugin to ensure Android is fully configured
            val androidPluginId = if (hasApp || plugins.hasPlugin("com.android.application")) {
                "com.android.application"
            } else {
                "com.android.library"
            }

            pluginManager.withPlugin(androidPluginId) {
                // AGP 9 alpha: ensure BaseExtension shim is available for Hilt
                pluginManager.apply("com.android.base")
                // Apply Hilt after Android is ready, then KSP
                pluginManager.apply("com.google.dagger.hilt.android")
                pluginManager.apply("com.google.devtools.ksp")

                // Configure dependencies through version catalog
                val libs =
                    extensions.getByType(org.gradle.api.artifacts.VersionCatalogsExtension::class.java)
                        .named("libs")
                dependencies {
                    add("implementation", libs.findLibrary("hilt-android").get())
                    add("ksp", libs.findLibrary("hilt-compiler").get())
                }
            }
        }
    }
}