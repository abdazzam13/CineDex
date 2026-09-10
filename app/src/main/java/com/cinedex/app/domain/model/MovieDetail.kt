package com.cinedex.app.domain.model

data class MovieDetail(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String,
    val backdropPath: String,
    val voteAverage: Double,
    val voteCount: Int,
    val releaseDate: String,
    val genres: List<String>,
    val runtime: Int
)

fun MovieDetail.releaseYear(): String = if (releaseDate.length >= 4) releaseDate.take(4) else "-"

fun MovieDetail.formattedRating(): String = if (voteAverage > 0.0) String.format(java.util.Locale.US, "%.1f", voteAverage) else "-"
