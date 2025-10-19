// Plugin classpath configuration script
// This isolates plugin resolution from individual module build scripts

buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
        // AGP and Kotlin from version catalog
        val libs = project.extensions.getByType<VersionCatalogsExtension>().named("libs")

        // Core plugins only - Hilt removed for runtime DI approach
        classpath("com.android.tools.build:gradle:${libs.findVersion("agp").get()}")
        classpath(
            "com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:${
                libs.findVersion(
                    "ksp"
                ).get()
            }"
        )
        classpath("com.google.gms:google-services:4.4.4")
    }
}
