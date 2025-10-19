import org.gradle.api.JavaVersion.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.Delete
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register

/**
 * ===================================================================
 * GENESIS APPLICATION CONVENTION PLUGIN
 * ===================================================================
 *
 * The primary convention plugin for Android application modules.
 *
 * Plugin Application Order (Critical!):
 * 1. Android Application
 * 2. Hilt (Dependency Injection)
 * 3. KSP (Annotation Processing)
 * 4. Compose Compiler
 * 5. Google Services (Firebase)
 *
 * @since Genesis Protocol 1.0
 */
class GenesisApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // ===== STEP 1: APPLY PLUGINS IN ORDER =====
            // AGP 9.0+ has built-in Kotlin support - DO NOT apply kotlin-android or compose.compiler
            // Apply the Android plugin immediately.
            pluginManager.apply("com.android.application")
            pluginManager.apply("com.google.devtools.ksp")

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            // ===== STEP 2: CONFIGURE ANDROID =====
            // Use the modern ApplicationExtension (AGP 7+) API
            // Now that the Android extension is configured and available, apply plugins that
            // depend on the Android extension (Hilt, KSP, Kotlin serialization, Google services).
            // Applying them here ensures their apply() sees the Android extension and avoids
            // the "Android BaseExtension not found" error from the Hilt plugin.
            pluginManager.apply("com.google.dagger.hilt.android")
            pluginManager.apply("com.google.devtools.ksp")
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")
            pluginManager.apply("com.google.gms.google-services")



            // ===== STEP 4: KSP CLEANUP TASK =====
            tasks.register<Delete>("cleanKspCache") {
                group = "genesis"
                description = "🧹 Clean KSP caches to prevent annotation processing issues"

                delete(
                    layout.buildDirectory.dir("generated/ksp"),
                    layout.buildDirectory.dir("generated/source/ksp"),
                    layout.buildDirectory.dir("tmp/kapt3"),
                    layout.buildDirectory.dir("tmp/kotlin-classes"),
                    layout.buildDirectory.dir("kotlin")
                )
            }

            tasks.named("preBuild") {
                dependsOn("cleanKspCache")
            }

            // ===== STEP 5: CORE LIBRARY DESUGARING =====
            dependencies.add("coreLibraryDesugaring", libs.findLibrary("desugar.jdk.libs").get())
            
            // ===== STEP 6: HILT DEPENDENCIES =====
            dependencies.add("implementation", libs.findLibrary("hilt.android").get())
            dependencies.add("ksp", libs.findLibrary("hilt.compiler").get())
            dependencies.add("implementation", libs.findLibrary("hilt.navigation.compose").get())
            dependencies.add("implementation", libs.findLibrary("hilt.work").get())
            
            // ===== STEP 7: LIBSU (ROOT ACCESS) =====
            dependencies.add("implementation", libs.findBundle("su").get())
            
            // ===== STEP 8: ROOM (KSP) =====
            dependencies.add("ksp", libs.findLibrary("androidx.room.compiler").get())
        }
    }
}

internal fun GenesisApplicationPlugin.configureAndroidApplication(extension: GenesisApplicationPlugin) {
}
