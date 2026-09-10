package com.cinedex.app.domain.model

import java.util.Locale

data class FavoriteMovie(
    val id: Int,
    val title: String,
    val posterPath: String,
    val releaseDate: String,
    val runtime: Int,
    val genres: List<String>,
    val voteAverage: Double
)

fun FavoriteMovie.formattedRating(): String = String.format(Locale.US, "%.1f / 10", voteAverage)

fun FavoriteMovie.formattedSubtitle(): String {
    return if (runtime > 0) {
        "$releaseDate \u2022 ${runtime}m"
    } else {
        releaseDate
    }
}
