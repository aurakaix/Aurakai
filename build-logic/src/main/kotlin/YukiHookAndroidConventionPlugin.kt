
import configureAndroidApplication
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.AppliedPlugin
import org.gradle.kotlin.dsl.configure

class YukiHookAndroidConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Configure Android applications for Yuki Hook
            pluginManager.withPlugin("com.android.application") {
                val configure = extensions.configure<GenesisApplicationPlugin> {
                    configureAndroidApplication(this)
                }
            }

            // Configure Android libraries for Yuki Hook
            pluginManager.withPlugin("com.android.library") {
                extensions.configure<GenesisLibraryPlugin> {
                    configureAndroidLibrary(this)
                }
            }
        }
    }

    /**
     * Apply Android application defaults required by the Yuki Hook integration.
     *
     * Configures compile SDK, defaultConfig (minSdk, targetSdk, and instrumentation runner),
     * the release build type (minification and ProGuard files), and Java compile options.
     *
     * @param extension The Android ApplicationExtension to configure.
     */
}


private fun AppliedPlugin.configureAndroidLibrary(libraryExtension: AppliedPlugin) {
}

private fun AppliedPlugin.configureAndroidApplication(extension: AppliedPlugin) {

}
