package com.cinedex.app.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReviewDto(
    val id: String,
    val author: String,
    val content: String,
    @Json(name = "created_at") val createdAt: String,
    @Json(name = "author_details") val authorDetails: AuthorDetailsDto?
)
