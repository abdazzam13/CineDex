package com.cinedex.app.domain.repository

import com.cinedex.app.domain.model.FavoriteMovie
import com.cinedex.app.domain.model.Movie
import com.cinedex.app.domain.model.MovieDetail
import com.cinedex.app.domain.model.Review
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    suspend fun getNowPlayingMovies(page: Int = 1, region: String? = null): Result<List<Movie>>

    suspend fun getPopularMovies(page: Int = 1, region: String? = null): Result<List<Movie>>

    suspend fun getTopRatedMovies(page: Int = 1, region: String? = null): Result<List<Movie>>

    suspend fun getUpcomingMovies(page: Int = 1, region: String? = null): Result<List<Movie>>

    suspend fun getMovieDetail(movieId: Int): Result<MovieDetail>

    suspend fun getMovieReviews(movieId: Int, page: Int = 1): Result<List<Review>>

    fun getFavoriteMovies(): Flow<List<FavoriteMovie>>

    fun isFavorite(movieId: Int): Flow<Boolean>

    suspend fun saveFavorite(movie: MovieDetail)

    suspend fun removeFavorite(movieId: Int)

    suspend fun clearAllFavorites()

    suspend fun syncFavorites(): Result<Unit>
}
