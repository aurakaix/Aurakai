plugins {
    id("java-library")
    kotlin("jvm")
    alias(libs.plugins.spotless)
}

group = "dev.aurakai.auraframefx.core"
version = "1.0.0"

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(24)) }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_24)
    }
}

dependencies {
    // Module dependency
    api(project(":list"))

    // Concurrency and serialization
    implementation(libs.bundles.coroutines)
    implementation(libs.kotlinx.serialization.json)

    // File operations and compression
    implementation(libs.commons.io)
    implementation(libs.commons.compress)
    implementation(libs.xz)

    // Logging API only (do not bind implementation at runtime for libraries)
    implementation(libs.slf4j.api)

    // Testing (JUnit 5)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.mockk)
    // Use the catalog alias for the SLF4J API (catalog entry is `slf4j-api`, accessor: libs.slf4j.api)
    testRuntimeOnly(libs.slf4j.api)
    implementation(kotlin("stdlib-jdk8"))
}

tasks.test {
    useJUnitPlatform()
}
