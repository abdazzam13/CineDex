package com.cinedex.app.util

object Constants {
    const val DATABASE_NAME = "cinedex.db"
    const val PAGE_SIZE = 20
    const val NETWORK_TIMEOUT_SECONDS = 30L
    const val PREFETCH_DISTANCE = 5
    const val MOVIE_SHARE_URL = "https://themoviedb.org/movie/"

    val GENRES = listOf(
        "All",
        "Action",
        "Adventure",
        "Animation",
        "Comedy",
        "Crime",
        "Documentary",
        "Drama",
        "Family",
        "Fantasy",
        "History",
        "Horror",
        "Music",
        "Mystery",
        "Romance",
        "Sci-Fi",
        "Thriller",
        "War",
        "Western"
    )

    val GENRE_MAP = mapOf(
        "Action" to 28,
        "Adventure" to 12,
        "Animation" to 16,
        "Comedy" to 35,
        "Crime" to 80,
        "Documentary" to 99,
        "Drama" to 18,
        "Family" to 10751,
        "Fantasy" to 14,
        "History" to 36,
        "Horror" to 27,
        "Music" to 10402,
        "Mystery" to 9648,
        "Romance" to 10749,
        "Sci-Fi" to 878,
        "Thriller" to 53,
        "War" to 10752,
        "Western" to 37
    )
}
