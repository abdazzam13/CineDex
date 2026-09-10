package com.cinedex.app.domain.model

import java.util.Locale

data class Review(
    val id: String,
    val author: String,
    val content: String,
    val createdAt: String,
    val rating: Double? = null
)

fun Review.formattedRating(): String {
    return rating?.let {
        String.format(Locale.US, "%.0f/10", it)
    } ?: "N/A"
}
