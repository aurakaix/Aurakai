import com.github.javaparser.utils.Utils.set
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.openapi.generator") version "7.16.0"
    kotlin("jvm")
    `java-library`
}

kotlin {
    jvmToolchain(24)
}

val ecoSpec = file("${rootDir}/data/api/ECO.yaml")
val ecoAiSpecCandidates = listOf(
    file("${rootDir}/data/api/ai.yml"),
    file("${rootDir}/data/ai.yml")
)
val ecoAiSpec = ecoAiSpecCandidates.firstOrNull { it.exists() }
    ?: throw GradleException("OpenAPI spec not found at: data/api/ai.yml or data/ai.yml")

require(ecoSpec.exists()) { "OpenAPI spec not found at: ${ecoSpec.absolutePath}" }
require(ecoAiSpec.exists()) { "OpenAPI spec not found at: ${ecoAiSpec.absolutePath}" }

openApiGenerate {
    generatorName = "kotlin"
    inputSpec = ecoSpec.toURI().toString()
    validateSpec = false
    outputDir = layout.buildDirectory.dir("generated/openapi/ecocore").get().asFile.path
    apiPackage = "dev.aurakai.auraframefx.api.ecocore"
    modelPackage = "dev.aurakai.auraframefx.model.ecocore"

    additionalProperties = mapOf(
        "skipValidateSpec" to "true",
        "legacyDiscriminatorBehavior" to "false"
    )
    // ✅ ADD THESE VALIDATION BYPASSES
    skipValidateSpec.set(true)
    validateSpec.set(false)

    configOptions = mapOf(
        "library" to "jvm-ktor",
        "serializationLibrary" to "kotlinx_serialization",
        "enumPropertyNaming" to "UPPERCASE",
        "collectionType" to "list",
        "dateLibrary" to "kotlinx-datetime",
        "useCoroutines" to "true",
        "omitGradlePluginVersions" to "false",
        "exceptionOnFailedStatusCodes" to "true",
        "generateModelDocumentation" to "false",
        "nonPublicApi" to "false",
        "hideGenerationTimestamp" to "true",
        "sortParamsByRequiredFlag" to "true",
        "sortModelPropertiesByRequiredFlag" to "true"
    )

    openapiNormalizer = mapOf(
        "REFACTOR_ALLOF_WITH_PROPERTIES_ONLY" to "true",
        "SIMPLIFY_ONEOF_ANYOF" to "true"
    )
}

tasks.register(
    "openApiGenerateEcoAi",
    org.openapitools.generator.gradle.plugin.tasks.GenerateTask::class
) {
    generatorName = "kotlin"
    inputSpec = ecoAiSpec.toURI().toString()
    validateSpec = false
    outputDir = layout.buildDirectory.dir("generated/openapi/ecoai").get().asFile.path
    apiPackage = "dev.aurakai.auraframefx.api.ecoai"
    modelPackage = "dev.aurakai.auraframefx.model.ecoai"

    additionalProperties = mapOf(
        "skipValidateSpec" to "true",
        "legacyDiscriminatorBehavior" to "false"
    )

    configOptions = mapOf(
        "library" to "jvm-ktor",
        "serializationLibrary" to "kotlinx_serialization",
        "enumPropertyNaming" to "UPPERCASE",
        "collectionType" to "list",
        "dateLibrary" to "kotlinx-datetime",
        "useCoroutines" to "true",
        "omitGradlePluginVersions" to "false",
        "exceptionOnFailedStatusCodes" to "true",
        "generateModelDocumentation" to "false",
        "nonPublicApi" to "false",
        "hideGenerationTimestamp" to "true",
        "sortParamsByRequiredFlag" to "true",
        "sortModelPropertiesByRequiredFlag" to "true"
    )

    openapiNormalizer = mapOf(
        "REFACTOR_ALLOF_WITH_PROPERTIES_ONLY" to "true",
        "SIMPLIFY_ONEOF_ANYOF" to "true"
    )
}

sourceSets {
    named("main") {
        java.srcDir(layout.buildDirectory.dir("generated/openapi/eco/src/main/kotlin"))
        java.srcDir(layout.buildDirectory.dir("generated/openapi/ecoai/src/main/kotlin"))
    }
}

// ✅ CHANGED: finalizedBy → dependsOn (this is the ONLY change)
tasks.withType<KotlinCompile>().configureEach {
    dependsOn(tasks.named("openApiGenerate"))  // ✅ FIXED - was finalizedBy
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
    }
}

tasks.named<Delete>("clean") {
    delete(layout.buildDirectory.dir("generated/openapi"))
}

tasks.jar {
    dependsOn(tasks.named("openApiGenerate"))
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.3.1")

    implementation(libs.kotlinx.serialization.json)
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
    implementation(libs.kotlinx.coroutines.core)

    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    implementation(libs.slf4j.api)

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.mockk)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
