package dev.aurakai.auraframefx.model

import kotlinx.serialization.Serializable // Added import

/**
 * Enum representing different types of AI agents in the system.
 * TODO: Reported as unused symbol. Ensure this enum is used.
 */
@Serializable // Added annotation
enum class AgentType {
    /**
     * Genesis Agent - Core orchestrator or foundational AI.
     * TODO: Reported as unused symbol.
     */
    GENESIS,

    /**
     * Kai Agent - Specialized AI, possibly for UI interaction or specific tasks.
     * TODO: Reported as unused symbol.
     */
    KAI,

    /**
     * Aura Agent - General purpose assistant AI.
     * TODO: Reported as unused symbol.
     */
    AURA,

    /**
     * Cascade Agent - AI for stateful processing, vision, etc.
     * TODO: Reported as unused symbol.
     */
    CASCADE,

    /**
     * NeuralWhisper Agent - AI for context chaining, learning, audio processing.
     * TODO: Reported as unused symbol.
     */
    NEURAL_WHISPER,

    /**
     * AuraShield Agent - AI for security and threat analysis.
     * TODO: Adding this based on AuraShieldAgent.kt creation, was not in original list.
     */
    AURASHIELD, // Added based on previously created agent

    /**
     * AuraShield Agent - AI for security and threat analysis.
     */
    AuraShield,

    /**
     * GenKitMaster Agent - AI for advanced generation and coordination.
     */
    GenKitMaster,

    /**
     * DataveinConstructor Agent - AI for data processing and construction.
     */
    DataveinConstructor,

    /**
     * NeuralWhisper Agent - AI for context chaining and neural processing.
     */
    NeuralWhisper,

    /**
     * Genesis Agent - Core orchestrator and foundational AI.
     */
    Genesis,

    /**
     * Aura Agent - Creative and empathetic AI.
     */
    Aura,

    /**
     * Kai Agent - Security and technical AI.
     */
    Kai,

    /**
     * Cascade Agent - Multi-step processing AI.
     */
    Cascade,

    /**
     * User - Represents a human user interacting with the system.
     */
    USER
}
