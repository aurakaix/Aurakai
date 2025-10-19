package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

class OpenApiConventionPlugin : Plugin<Project> {
    /**
     * No-op plugin stub for OpenAPI-related conventions.
     *
     * All OpenAPI plugin application and configuration should be performed in the module's
     * build.gradle.kts using the plugins block and `openApiGenerate`.
     *
     * @param target The project to which this plugin is applied.
     */
    override fun apply(target: Project) {
        // No OpenAPI logic here. Configure in build.gradle.kts instead.
    }
}