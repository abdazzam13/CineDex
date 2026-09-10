package com.cinedex.app.data.repository

import com.cinedex.app.data.local.dao.MovieDao
import com.cinedex.app.data.mapper.toDetailDomain
import com.cinedex.app.data.mapper.toDomain
import com.cinedex.app.data.mapper.toEntity
import com.cinedex.app.data.mapper.toFavoriteDomain
import com.cinedex.app.data.mapper.toFavoriteEntity
import com.cinedex.app.data.remote.MovieApiService
import com.cinedex.app.data.remote.dto.FavoriteRequestDto
import com.cinedex.app.domain.model.FavoriteMovie
import com.cinedex.app.domain.model.Movie
import com.cinedex.app.domain.model.MovieDetail
import com.cinedex.app.domain.model.Review
import com.cinedex.app.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val apiService: MovieApiService,
    private val movieDao: MovieDao
) : MovieRepository {

    override suspend fun getNowPlayingMovies(page: Int, region: String?): Result<List<Movie>> {
        return try {
            val (minDate, maxDate) = getNowPlayingDateWindow()
            val response = apiService.getDiscoverMovies(
                page = page,
                releaseDateGte = minDate,
                releaseDateLte = maxDate,
                releaseType = "2|3",
                sortBy = "popularity.desc",
                region = region
            )
            val movies = response.results.map { it.toDomain() }
            Result.success(movies)
        } catch (e: Exception) {
            Timber.e(e, "Error fetching now playing movies")
            Result.failure(e)
        }
    }

    private fun getNowPlayingDateWindow(): Pair<String, String> {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        calendar.add(Calendar.DAY_OF_YEAR, 6)
        val maxDate = dateFormat.format(calendar.time)
        calendar.add(Calendar.DAY_OF_YEAR, -42)
        val minDate = dateFormat.format(calendar.time)
        return Pair(minDate, maxDate)
    }

    override suspend fun getPopularMovies(page: Int, region: String?): Result<List<Movie>> {
        return try {
            val response = apiService.getPopularMovies(page, region)
            val movies = response.results.map { it.toDomain() }
            Result.success(movies)
        } catch (e: Exception) {
            Timber.e(e, "Error fetching popular movies")
            Result.failure(e)
        }
    }

    override suspend fun getTopRatedMovies(page: Int, region: String?): Result<List<Movie>> {
        return try {
            val response = apiService.getTopRatedMovies(page, region)
            val movies = response.results.map { it.toDomain() }
            Result.success(movies)
        } catch (e: Exception) {
            Timber.e(e, "Error fetching top rated movies")
            Result.failure(e)
        }
    }

    override suspend fun getUpcomingMovies(page: Int, region: String?): Result<List<Movie>> {
        return try {
            val response = apiService.getUpcomingMovies(page, region)
            val movies = response.results.map { it.toDomain() }
            Result.success(movies)
        } catch (e: Exception) {
            Timber.e(e, "Error fetching upcoming movies")
            Result.failure(e)
        }
    }

    override suspend fun getMovieDetail(movieId: Int): Result<MovieDetail> {
        return try {
            val response = apiService.getMovieDetail(movieId)
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Timber.e(e, "Error fetching movie detail for $movieId")
            val cached = movieDao.getMovieById(movieId)
            if (cached != null) {
                Result.success(cached.toDetailDomain())
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun getMovieReviews(movieId: Int, page: Int): Result<List<Review>> {
        return try {
            val response = apiService.getMovieReviews(movieId, page)
            val reviews = response.results.map { it.toDomain() }
            Result.success(reviews)
        } catch (e: Exception) {
            Timber.e(e, "Error fetching reviews for $movieId")
            Result.failure(e)
        }
    }

    override fun getFavoriteMovies(): Flow<List<FavoriteMovie>> {
        return movieDao.getFavoriteMovies().map { list ->
            list.map { it.toFavoriteDomain() }
        }
    }

    override fun isFavorite(movieId: Int): Flow<Boolean> {
        return movieDao.isFavorite(movieId)
    }

    override suspend fun saveFavorite(movie: MovieDetail) {
        movieDao.insertMovie(movie.toEntity())
        try {
            apiService.markFavorite(
                FavoriteRequestDto(
                    mediaType = "movie",
                    mediaId = movie.id,
                    favorite = true
                )
            )
        } catch (e: Exception) {
            Timber.e(e, "Error syncing favorite to API for ${movie.id}")
        }
    }

    override suspend fun removeFavorite(movieId: Int) {
        movieDao.deleteMovieById(movieId)
        try {
            apiService.markFavorite(
                FavoriteRequestDto(
                    mediaType = "movie",
                    mediaId = movieId,
                    favorite = false
                )
            )
        } catch (e: Exception) {
            Timber.e(e, "Error removing favorite from API for $movieId")
        }
    }

    override suspend fun clearAllFavorites() {
        val currentFavorites = movieDao.getFavoriteMoviesList()
        movieDao.clearAllFavorites()
        try {
            currentFavorites.forEach { movie ->
                apiService.markFavorite(
                    FavoriteRequestDto(
                        mediaType = "movie",
                        mediaId = movie.id,
                        favorite = false
                    )
                )
            }
        } catch (e: Exception) {
            Timber.e(e, "Error clearing favorites from API")
        }
    }

    override suspend fun syncFavorites(): Result<Unit> {
        return try {
            val response = apiService.getAccountFavoriteMovies(page = 1)
            val entities = response.results.map { it.toFavoriteEntity() }
            if (entities.isNotEmpty()) {
                movieDao.insertMovies(entities)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error syncing favorite movies from API")
            Result.failure(e)
        }
    }
}
