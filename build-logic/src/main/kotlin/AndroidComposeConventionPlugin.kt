// ==== GENESIS PROTOCOL - ANDROID COMPOSE CONVENTION ====
// Compose-enabled Android library configuration

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.Actions.with
import org.gradle.kotlin.dsl.*

class AndroidComposeConventionPlugin : Plugin<Project> {
    /**
     * Configure the target Gradle project with Android Compose conventions.
     *
     * Enables Jetpack Compose for Android library modules and applies the
     * "com.android.library" and "genesis.android.library" plugins.
     *
     * Note: modules must also apply the Compose Compiler plugin (for example,
     * via `alias(libs.plugins.compose.compiler)`).
     *
     * @param target The Gradle project to configure.
     */
    override fun apply(target: Project) {
        with(target) {
            // Do NOT apply the Hilt Gradle plugin in library modules (AGP 8+/9+)
            pluginManager.apply("com.android.library")
            pluginManager.apply("genesis.android.library")

        }
    }
}