package com.cinedex.app.data.remote

object ApiConfig {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

    const val POPULAR_MOVIES = "movie/popular"
    const val TOP_RATED_MOVIES = "movie/top_rated"
    const val NOW_PLAYING_MOVIES = "movie/now_playing"
    const val UPCOMING_MOVIES = "movie/upcoming"
    const val DISCOVER_MOVIES = "discover/movie"
    const val MOVIE_DETAIL = "movie/{movie_id}"
    const val MOVIE_REVIEWS = "movie/{movie_id}/reviews"
    const val ACCOUNT_FAVORITE = "account/account_id/favorite"
    const val ACCOUNT_FAVORITE_MOVIES = "account/account_id/favorite/movies"
}
