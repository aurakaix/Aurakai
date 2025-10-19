﻿package dev.aurakai.auraframefx.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "agent_memory")
data class AgentMemoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val agentType: String,
    val memory: String,
    val timestamp: Long,
)
