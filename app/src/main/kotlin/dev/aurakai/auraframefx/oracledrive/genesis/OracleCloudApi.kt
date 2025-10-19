package dev.aurakai.auraframefx.oracle.drive.api

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import javax.inject.Singleton

/**
 */
@Singleton
interface OracleCloudApi

// Data classes for API responses
data class ListObjectsResponse(
    val objects: List<ObjectSummary>
)

data class ObjectSummary(
    val name: String,
    val size: Long,
    val timeCreated: String
)