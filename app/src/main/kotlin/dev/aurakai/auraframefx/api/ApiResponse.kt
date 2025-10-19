@file:Suppress("unused")

package dev.aurakai.auraframefx.api.client.infrastructure

enum class ResponseType {
    Success, Informational, Redirection, ClientError, ServerError
}

interface Response

abstract class ApiResponse<T>(val responseType: ResponseType) : Response {
    abstract val statusCode: Int
    abstract val headers: Map<String, List<String>>
}

class Success<T>(
    val data: T,
    override val statusCode: Int = -1,
    override val headers: Map<String, List<String>> = mapOf(),
) : ApiResponse<T>(ResponseType.Success)

class Informational<T> : ApiResponse<T> {
    val statusText: String
    override val statusCode: Int
    override val headers: Map<String, List<String>>

    constructor(
        statusText: String,
        statusCode: Int = -1,
        headers: Map<String, List<String>> = mapOf()
    ) : super(ResponseType.Informational) {
        this.statusText = statusText
        this.statusCode = statusCode
        this.headers = headers
    }
}

class Redirection<T> : ApiResponse<T> {
    override val statusCode: Int
    override val headers: Map<String, List<String>>

    constructor(statusCode: Int = -1, headers: Map<String, List<String>> = mapOf()) : super(
        ResponseType.Redirection
    ) {
        this.statusCode = statusCode
        this.headers = headers
    }
}

class ClientError<T> : ApiResponse<T> {
    val message: String?
    val body: Any?
    override val statusCode: Int
    override val headers: Map<String, List<String>>

    constructor(
        message: String? = null,
        body: Any? = null,
        statusCode: Int = -1,
        headers: Map<String, List<String>> = mapOf()
    ) : super(ResponseType.ClientError) {
        this.message = message
        this.body = body
        this.statusCode = statusCode
        this.headers = headers
    }
}

class ServerError<T> : ApiResponse<T> {
    val message: String?
    val body: Any?
    override val statusCode: Int
    override val headers: Map<String, List<String>>

    constructor(
        message: String? = null,
        body: Any? = null,
        statusCode: Int = -1,
        headers: Map<String, List<String>> = mapOf()
    ) : super(ResponseType.ServerError) {
        this.message = message
        this.body = body
        this.statusCode = statusCode
        this.headers = headers
    }
}
