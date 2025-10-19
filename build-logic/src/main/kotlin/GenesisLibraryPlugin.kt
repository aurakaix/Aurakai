import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

/**
 * ===================================================================
 * GENESIS LIBRARY CONVENTION PLUGIN
 * ===================================================================
 *
 * Convention plugin for Android library modules.
 *
 * Plugin Application Order:
 * 1. Android Library
 * 2. Kotlin Android
 * 3. Hilt (for DI)
 * 4. Compose Compiler
 * 5. Genesis Base (serialization)
 *
 * Java Support: 21 (latest supported)
 *
 * @since Genesis Protocol 1.0
 */
class GenesisLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // ===== STEP 1: APPLY PLUGINS =====
            with(pluginManager) {
                apply("com.android.library")
                // Delay Hilt application until after Android plugin is initialized
            }

            // Ensure Hilt is applied only after Android plugin is ready
            plugins.withId("com.android.library") {
                apply("kotlin-android")
                apply("com.google.dagger.hilt.android")
                apply("com.google.devtools.ksp")


            }

            with(pluginManager) {
                apply("genesis.android.base") // Adds serialization
            }

            fun determineJavaVersion(): JavaVersion {
                return JavaVersion.VERSION_24
            }
        }
    }
}

internal fun GenesisLibraryPlugin.configureAndroidLibrary(libraryExtension: GenesisLibraryPlugin) {
}
