import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Delete
import org.gradle.kotlin.dsl.register

/**
 * ===================================================================
 * GENESIS OPENAPI GENERATOR CONVENTION PLUGIN
 * ===================================================================
 *
 * Configures OpenAPI code generation with Kotlin, Retrofit2, and Hilt.
 *
 * OpenAPI Generator Version: 7.16.0
 *
 * @since Genesis Protocol 1.0
 */
class GenesisOpenApiPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Apply OpenAPI Generator plugin
            pluginManager.apply("org.openapi.generator")

            // ===== OPENAPI GENERATOR CONFIGURATION =====
            // For AGP 9.x, we'll use a simpler approach
            // The OpenAPI generator will be configured in the module's build.gradle.kts

            // ===== CLEANUP TASK =====
            val cleanApiGeneration = tasks.register<Delete>("cleanApiGeneration") {
                group = "genesis"
                description = "🧹 Clean generated OpenAPI files"
                delete(fileTree("${project.layout.buildDirectory.get()}/generated/source/openapi/"))
            }

            tasks.named("clean") {
                dependsOn(cleanApiGeneration)
            }
        }
    }
}
