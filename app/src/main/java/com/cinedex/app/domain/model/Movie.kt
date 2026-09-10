package com.cinedex.app.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String,
    val backdropPath: String,
    val voteAverage: Double,
    val releaseDate: String,
    val genreIds: List<Int>
)

fun Movie.releaseYear(): String = if (releaseDate.length >= 4) releaseDate.take(4) else "-"

fun Movie.formattedRating(): String = if (voteAverage > 0.0) String.format(java.util.Locale.US, "%.1f", voteAverage) else "-"
