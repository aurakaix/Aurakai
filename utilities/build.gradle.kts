import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.spotless)
}

group = "dev.aurakai.auraframefx.utilities"
version = "1.0.0"
// Centralized toolchain version

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(jdkVersion)) }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_25)
    }
}



dependencies {
    // Module dependency (utilities depends on list)
    api(project(":list"))

    implementation(libs.bundles.coroutines)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.commons.io)
    implementation(libs.commons.compress)
    implementation(libs.xz)


    // Testing (JUnit 5)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.mockk)
    testImplementation(kotlin("stdlib"))
    // Bind a simple logger only during tests
    testRuntimeOnly(libs.slf4j.simple)
    implementation(kotlin("stdlib-jdk8"))
}

tasks.test {
    useJUnitPlatform()
}

android {
    // ...existing code...
    defaultConfig {
        minSdk = 33
    }
    ndkVersion = "29.0.14206865"
    // ...existing code...
}
