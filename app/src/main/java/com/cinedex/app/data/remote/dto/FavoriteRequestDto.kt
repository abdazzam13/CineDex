package com.cinedex.app.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FavoriteRequestDto(
    @Json(name = "media_type") val mediaType: String = "movie",
    @Json(name = "media_id") val mediaId: Int,
    @Json(name = "favorite") val favorite: Boolean
)
