﻿package dev.aurakai.auraframefx.system.overlay.model

// Basic placeholder
data class OverlayTransition(
    val id: String, // Added id as Impl uses it as map key
    val type: String, // e.g., "material_shared_axis", "fade_through"
    val durationMs: Long? = null,
)
