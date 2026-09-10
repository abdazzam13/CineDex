package com.cinedex.app.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FavoriteResponseDto(
    @Json(name = "status_code") val statusCode: Int? = null,
    @Json(name = "status_message") val statusMessage: String? = null,
    val success: Boolean? = null
)
