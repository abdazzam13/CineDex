package com.cinedex.app.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthorDetailsDto(
    val rating: Double?
)
