import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * ===================================================================
 * GENESIS BASE CONVENTION PLUGIN
 * ===================================================================
 *
 * Convention plugin for base serialization and native support.
 * Applies: genesis.android.base
 * ID: genesis.android.base
 *
 * Plugin Application Order:
 * 1. Kotlin Serialization
 * 2. Native support (if applicable)
 *
 * @since Genesis Protocol 1.0
 */
class GenesisBasePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // ===== APPLY PLUGINS IN ORDER =====
            with(pluginManager) {
                // 1. Serialization
                apply("org.jetbrains.kotlin.plugin.serialization")

                // 2. Native support would go here if needed
                // apply("org.jetbrains.kotlin.multiplatform") // If multiplatform
            }

            // Additional base configuration can be added here
            // This plugin is meant to be applied alongside android.library or android.application
        }
    }
}
