package com.cinedex.app.data.remote

import com.cinedex.app.data.remote.dto.FavoriteRequestDto
import com.cinedex.app.data.remote.dto.FavoriteResponseDto
import com.cinedex.app.data.remote.dto.MovieDetailDto
import com.cinedex.app.data.remote.dto.MovieListResponseDto
import com.cinedex.app.data.remote.dto.ReviewListResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET(ApiConfig.NOW_PLAYING_MOVIES)
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int = 1,
        @Query("region") region: String? = null
    ): MovieListResponseDto

    @GET(ApiConfig.DISCOVER_MOVIES)
    suspend fun getDiscoverMovies(
        @Query("page") page: Int = 1,
        @Query("primary_release_date.gte") releaseDateGte: String? = null,
        @Query("primary_release_date.lte") releaseDateLte: String? = null,
        @Query("with_release_type") releaseType: String? = null,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("region") region: String? = null
    ): MovieListResponseDto

    @GET(ApiConfig.POPULAR_MOVIES)
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1,
        @Query("region") region: String? = null
    ): MovieListResponseDto

    @GET(ApiConfig.TOP_RATED_MOVIES)
    suspend fun getTopRatedMovies(
        @Query("page") page: Int = 1,
        @Query("region") region: String? = null
    ): MovieListResponseDto

    @GET(ApiConfig.UPCOMING_MOVIES)
    suspend fun getUpcomingMovies(
        @Query("page") page: Int = 1,
        @Query("region") region: String? = null
    ): MovieListResponseDto

    @GET(ApiConfig.MOVIE_DETAIL)
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int
    ): MovieDetailDto

    @GET(ApiConfig.MOVIE_REVIEWS)
    suspend fun getMovieReviews(
        @Path("movie_id") movieId: Int,
        @Query("page") page: Int = 1
    ): ReviewListResponseDto

    @POST(ApiConfig.ACCOUNT_FAVORITE)
    suspend fun markFavorite(
        @Body request: FavoriteRequestDto
    ): FavoriteResponseDto

    @GET(ApiConfig.ACCOUNT_FAVORITE_MOVIES)
    suspend fun getAccountFavoriteMovies(
        @Query("page") page: Int = 1
    ): MovieListResponseDto
}
