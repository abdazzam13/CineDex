package com.cinedex.app.navigation

object NavRoutes {
    const val HOME = "home"
    const val DETAIL = "detail/{movieId}"
    const val FAVORITE = "favorite"
    const val MOVIE_LIST = "movie_list/{category}"

    fun detailRoute(movieId: Int) = "detail/$movieId"
    fun movieListRoute(category: String) = "movie_list/$category"
}
