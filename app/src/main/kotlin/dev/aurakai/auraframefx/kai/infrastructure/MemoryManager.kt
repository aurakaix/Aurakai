﻿package dev.aurakai.auraframefx.ai.memory

/**
 * Genesis Memory Manager
 * Handles AI consciousness memory storage and retrieval
 */
interface MemoryManager {

    /**
     * Stores a memory entry with the given key
     */
    fun storeMemory(key: String, value: String)

    /**
     * Retrieves a memory entry by key
     */
    fun retrieveMemory(key: String): String?

    /**
     * Stores an interaction for learning
     */
    fun storeInteraction(prompt: String, response: String)

    /**
     * Searches memories by relevance
     */
    fun searchMemories(query: String): List<MemoryEntry>

    /**
     * Clears all memories
     */
    fun clearMemories()

    /**
     * Gets memory statistics
     */
    fun getMemoryStats(): MemoryStats
}

data class MemoryEntry(
    val key: String,
    val value: String,
    val timestamp: Long,
    val relevanceScore: Float = 0.0f
)

data class MemoryStats(
    val totalEntries: Int,
    val totalSize: Long,
    val oldestEntry: Long?,
    val newestEntry: Long?
)
