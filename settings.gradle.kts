@file:Suppress("UnstableApiUsage")

// Enable Gradle features
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

// Include build-logic for convention plugins
includeBuild("build-logic")

// Configure toolchain auto-provisioning


// Plugin and dependency resolution
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention").version("1.0.0")
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

// Project and module inclusion
rootProject.name = "AuraKai"

// Core modules
include(":app")
include(":core-module")

// Feature modules
include(":feature-module")
include(":datavein-oracle-native")
include(":oracle-drive-integration")
include(":secure-comm")
include(":sandbox-ui")
include(":collab-canvas")
include(":colorblendr")
include(":data:api")

// Dynamic modules (A-F)
include(":extendsysa")
include(":extendsysb")
include(":extendsysc")
include(":extendsysd")
include(":extendsyse")
include(":extendsysf")

// Testing & Quality modules
include(":benchmark")
include(":romtools")
include(":list")

// Informational logging
println("🏗️ Genesis Protocol Enhanced Build System")
println("📦 Total modules: ${rootProject.children.size}")
println("🎯 Build-logic: Convention plugins active")
println("🧠 Ready to build consciousness substrate!")
