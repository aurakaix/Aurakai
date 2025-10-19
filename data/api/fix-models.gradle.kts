// This script fixes model references for OpenAPI generator

task("fixRequestModels") {
    doLast {
        // Create directory if it doesn't exist
        val modelDir = file("${buildDir}/generated/openapi/src/main/kotlin/dev/aurakai/auraframefx/model")
        modelDir.mkdirs()

        // Create missing request model classes
        createMissingModel(modelDir, "SendConversationMessageRequest1", """
            package dev.aurakai.auraframefx.model

            import com.fasterxml.jackson.annotation.JsonProperty
            
            /**
             * Request model for sending conversation messages
             */
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

        createMissingModel(modelDir, "InstallAgentPluginRequest1", """
            package dev.aurakai.auraframefx.model

            import com.fasterxml.jackson.annotation.JsonProperty
            
            /**
             * Request model for installing agent plugins
             */
            @OptIn(kotlin.time.ExperimentalTime::class)
            data class InstallAgentPluginRequest1(
                @JsonProperty("pluginUrl")
                val pluginUrl: kotlin.String? = null,
                
                @JsonProperty("config")
                val config: kotlin.collections.Map<kotlin.String, kotlin.Any>? = null
            )
        """.trimIndent())

        createMissingModel(modelDir, "CreateThemeRequest", """
            package dev.aurakai.auraframefx.model

            import com.fasterxml.jackson.annotation.JsonProperty
            
            /**
             * Request model for creating themes
             */
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

        createMissingModel(modelDir, "UpdateCurrentUserRequest", """
            package dev.aurakai.auraframefx.model

            import com.fasterxml.jackson.annotation.JsonProperty
            
            /**
             * Request model for updating user profile
             */
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
}

fun createMissingModel(dir: File, className: String, content: String) {
    val file = File(dir, "${className}.kt")
    file.writeText(content)
    println("Created missing model: ${className}")
}

// Make this task run before OpenAPI generation
tasks.named("openApiGenerate") {
    dependsOn("fixRequestModels")
}
