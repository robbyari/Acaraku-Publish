package com.robbyari.acaraku.domain.model

import com.google.gson.annotations.SerializedName

data class ResponseWebhook(

    @field:SerializedName("per_page")
    val perPage: Int? = null,

    @field:SerializedName("total")
    val total: Int? = null,

    @field:SerializedName("data")
    val data: List<DataItem?>? = null,

    @field:SerializedName("from")
    val from: Int? = null,

    @field:SerializedName("is_last_page")
    val isLastPage: Boolean? = null,

    @field:SerializedName("to")
    val to: Int? = null,

    @field:SerializedName("current_page")
    val currentPage: Int? = null
)

data class Headers(

    @field:SerializedName("content-length")
    val contentLength: List<String?>? = null,

    @field:SerializedName("host")
    val host: List<String?>? = null,

    @field:SerializedName("connection")
    val connection: List<String?>? = null,

    @field:SerializedName("content-type")
    val contentType: List<String?>? = null,

    @field:SerializedName("user-agent")
    val userAgent: List<String?>? = null
)

data class DataItem(

    @field:SerializedName("headers")
    val headers: Headers? = null,

    @field:SerializedName("method")
    val method: String? = null,

    @field:SerializedName("ip")
    val ip: String? = null,

    @field:SerializedName("query")
    val query: Any? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("custom_action_output")
    val customActionOutput: List<Any?>? = null,

    @field:SerializedName("team_id")
    val teamId: Any? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("uuid")
    val uuid: String? = null,

    @field:SerializedName("content")
    val content: String? = null,

    @field:SerializedName("url")
    val url: String? = null,

    @field:SerializedName("hostname")
    val hostname: String? = null,

    @field:SerializedName("token_id")
    val tokenId: String? = null,

    @field:SerializedName("size")
    val size: Int? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("sorting")
    val sorting: Long? = null,

    @field:SerializedName("files")
    val files: List<Any?>? = null,

    @field:SerializedName("user_agent")
    val userAgent: String? = null
)
