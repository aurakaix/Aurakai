// Main API build configuration
plugins {
    kotlin("jvm")
    id("org.openapi.generator") version "7.0.1" // Using a recent version compatible with OpenAPI 3.1
}

// Apply custom scripts
apply(from = "openapi-generator.gradle.kts")
apply(from = "fix-models.gradle.kts")

// Set up dependencies
dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2")
}

// Ensure proper task ordering
tasks.named("compileKotlin") {
    dependsOn("openApiGenerate")
}

// Add the generated code to your source sets
sourceSets {
    main {
        java {
            srcDir("${buildDir}/generated/openapi/src/main/kotlin")
        }
    }
}

// Configure the openApiGenerate task to run our custom tasks
tasks.register("generateApiWithFixedModels") {
    dependsOn("openApiGenerate")
    group = "api"
    description = "Generates API code with fixed model references"
}

// Add a post-processor task that fixes the generated code
tasks.register<JavaExec>("fixGeneratedCode") {
    description = "Fixes issues in the generated code"
    group = "api"

    dependsOn("openApiGenerate")
    tasks.named("compileKotlin").get().dependsOn(this)

    classpath = files(configurations.compileClasspath)
    mainClass.set("kotlin.script.experimental.jvm.impl.KJvmCompilerRunner")

    doLast {
        // Create the model directory if it doesn't exist
        val modelDir = file("${buildDir}/generated/openapi/src/main/kotlin/dev/aurakai/auraframefx/model")
        modelDir.mkdirs()

        // Create the missing request model classes
        createMissingModels(modelDir)

        // Add @OptIn annotation to all generated files
        addOptInAnnotations("${buildDir}/generated/openapi/src/main/kotlin")

        println("Successfully fixed generated code")
    }
}

// Function to create missing model classes
fun createMissingModels(modelDir: File) {
    // SendConversationMessageRequest1
    file("$modelDir/SendConversationMessageRequest1.kt").writeText("""
        package dev.aurakai.auraframefx.model

        import com.fasterxml.jackson.annotation.JsonProperty
        
        @OptIn(kotlin.time.ExperimentalTime::class)
        data class SendConversationMessageRequest1(
            @JsonProperty("message")
            val message: kotlin.String,
            
            @JsonProperty("attachments")
            val attachments: kotlin.collections.List<Attachment>? = null
        )
        
        @OptIn(kotlin.time.ExperimentalTime::class)
        data class Attachment(
            @JsonProperty("type")
            val type: kotlin.String,
            
            @JsonProperty("url")
            val url: kotlin.String
        )
    """.trimIndent())

    // InstallAgentPluginRequest1
    file("$modelDir/InstallAgentPluginRequest1.kt").writeText("""
        package dev.aurakai.auraframefx.model

        import com.fasterxml.jackson.annotation.JsonProperty
        
        @OptIn(kotlin.time.ExperimentalTime::class)
        data class InstallAgentPluginRequest1(
            @JsonProperty("pluginUrl")
            val pluginUrl: kotlin.String? = null,
            
            @JsonProperty("config")
            val config: kotlin.collections.Map<kotlin.String, kotlin.Any>? = null
        )
    """.trimIndent())

    // CreateThemeRequest
    file("$modelDir/CreateThemeRequest.kt").writeText("""
        package dev.aurakai.auraframefx.model

        import com.fasterxml.jackson.annotation.JsonProperty
        
        @OptIn(kotlin.time.ExperimentalTime::class)
        data class CreateThemeRequest(
            @JsonProperty("name")
            val name: kotlin.String,
            
            @JsonProperty("description")
            val description: kotlin.String? = null,
            
            @JsonProperty("category")
            val category: kotlin.String? = null,
            
            @JsonProperty("primaryColor")
            val primaryColor: kotlin.String,
            
            @JsonProperty("secondaryColor")
            val secondaryColor: kotlin.String,
            
            @JsonProperty("accentColor")
            val accentColor: kotlin.String? = null,
            
            @JsonProperty("backgroundColor")
            val backgroundColor: kotlin.String? = null,
            
            @JsonProperty("textColor")
            val textColor: kotlin.String? = null,
            
            @JsonProperty("tags")
            val tags: kotlin.collections.List<kotlin.String>? = null,
            
            @JsonProperty("compatibility")
            val compatibility: kotlin.collections.List<kotlin.String>? = null
        )
    """.trimIndent())

    // UpdateCurrentUserRequest
    file("$modelDir/UpdateCurrentUserRequest.kt").writeText("""
        package dev.aurakai.auraframefx.model

        import com.fasterxml.jackson.annotation.JsonProperty
        
        @OptIn(kotlin.time.ExperimentalTime::class)
        data class UpdateCurrentUserRequest(
            @JsonProperty("displayName")
            val displayName: kotlin.String? = null,
            
            @JsonProperty("avatarUrl")
            val avatarUrl: kotlin.String? = null,
            
            @JsonProperty("mfaEnabled")
            val mfaEnabled: kotlin.Boolean? = null
        )
    """.trimIndent())
}

// Function to add @OptIn annotation to all generated Kotlin files
fun addOptInAnnotations(baseDir: String) {
    val dir = file(baseDir)
    if (!dir.exists()) return

    dir.walkTopDown()
        .filter { it.isFile && it.extension == "kt" }
        .forEach { file ->
            val content = file.readText()
            if (!content.contains("@OptIn(kotlin.time.ExperimentalTime::class)")) {
                var modified = content
                // Ensure canonical package gets the OptIn annotation (handle once)
                // Add @OptIn annotation to canonical package declarations
                modified = modified.replace(
                    "package dev.aurakai.auraframefx",
                    "package dev.aurakai.auraframefx\n\n@OptIn(kotlin.time.ExperimentalTime::class)"
                )
                if (modified != content) file.writeText(modified)
            }
        }
}

// Configure Java compilation
tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

// Configure Kotlin compilation
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.addAll("-Xopt-in=kotlin.time.ExperimentalTime")
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}
