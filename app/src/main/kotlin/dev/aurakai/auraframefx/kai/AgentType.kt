﻿package dev.aurakai.auraframefx.model

import kotlinx.serialization.Serializable // Added import

/**
 * Enum representing different types of AI agents in the system.
 * TODO: Reported as unused symbol. Ensure this enum is used.
 */
enum class AgentType {
    /**
     * Genesis Agent - Core orchestrator or foundational AI.
     * TODO: Reported as unused symbol.
     */
    GENESIS,

    /**
     */
    KAI,

    /**
     */
    AURA,

    /**
     */
    CASCADE,

    /**
     */
    NEURAL_WHISPER,

    /**
     * AuraShield Agent - AI for security and threat analysis.
     * TODO: Adding this based on AuraShieldAgent.kt creation, was not in original list.
     */

    /**
     * GenKitMaster Agent - AI for advanced generation and coordination.
     */

    /**
     * DataveinConstructor Agent - AI for data processing and construction.
     */

    /**
     */

    /**
     */

    /**
     * User - Represents a human user interacting with the system.
     */
    USER
}
