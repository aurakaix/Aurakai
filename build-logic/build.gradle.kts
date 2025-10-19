// AOSP-ReGenesis/build-logic/build.gradle.kts
plugins {
    `kotlin-dsl`

}

group = "dev.aurakai.auraframefx.buildlogic"

// Dependencies required for the convention plugins themselves.
val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {

    // Compile-time only dependency so convention plugin sources can reference
    // Kotlin Gradle types (KotlinAndroidProjectExtension, etc.) without
    // adding the plugin to the runtime classpath which causes plugin resolution
    // conflicts at configuration time.

    // Keep runtime/runtime-classpath plugins explicit (Hilt)

    // Test dependencies

    testImplementation("org.junit.jupiter:junit-jupiter-params:6.0.0")
    testImplementation("org.gradle:gradle-tooling-api:9.1.0")
    testImplementation(gradleTestKit())
}

// Configure test execution (temporarily disabled for bleeding-edge compatibility)
tasks.test {
    useJUnitPlatform()
    enabled = true // Re-enabled for full test support
}

tasks.compileTestKotlin {
    enabled = true // Re-enabled for full test support
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "genesis.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "genesis.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidHilt") {
            id = "genesis.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidCompose") {
            id = "genesis.android.compose"
            implementationClass = "AndroidComposeConventionPlugin"
        }
        register("androidNative") {
            id = "genesis.android.native"
            implementationClass = "AndroidNativeConventionPlugin"
        }
        register("androidBase") {
            id = "android.base"
            implementationClass = "plugins.AndroidBasePlugin"
        }
        register("agentFusion") {
            id = "agent.fusion"
            implementationClass = "plugins.AgentFusionPlugin"
        }
    }
}

kotlin {
    jvmToolchain(24)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(24))
    }
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
