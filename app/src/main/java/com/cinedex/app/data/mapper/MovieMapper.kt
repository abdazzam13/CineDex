package com.cinedex.app.data.mapper

import com.cinedex.app.data.local.entity.MovieEntity
import com.cinedex.app.data.remote.ApiConfig
import com.cinedex.app.data.remote.dto.MovieDetailDto
import com.cinedex.app.data.remote.dto.MovieDto
import com.cinedex.app.data.remote.dto.ReviewDto
import com.cinedex.app.domain.model.FavoriteMovie
import com.cinedex.app.domain.model.Movie
import com.cinedex.app.domain.model.MovieDetail
import com.cinedex.app.domain.model.Review

fun MovieDto.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview ?: "",
        posterPath = posterPath?.let { "${ApiConfig.IMAGE_BASE_URL}$it" } ?: "",
        backdropPath = backdropPath?.let { "${ApiConfig.IMAGE_BASE_URL}$it" } ?: "",
        voteAverage = voteAverage ?: 0.0,
        releaseDate = releaseDate ?: "",
        genreIds = genreIds ?: emptyList()
    )
}

fun MovieDto.toFavoriteEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        overview = overview ?: "",
        posterPath = posterPath?.let { "${ApiConfig.IMAGE_BASE_URL}$it" } ?: "",
        backdropPath = backdropPath?.let { "${ApiConfig.IMAGE_BASE_URL}$it" } ?: "",
        voteAverage = voteAverage ?: 0.0,
        voteCount = 0,
        releaseDate = releaseDate ?: "",
        genres = emptyList(),
        runtime = 0,
        isFavorite = true
    )
}

fun MovieDetailDto.toDomain(): MovieDetail {
    return MovieDetail(
        id = id,
        title = title,
        overview = overview ?: "",
        posterPath = posterPath?.let { "${ApiConfig.IMAGE_BASE_URL}$it" } ?: "",
        backdropPath = backdropPath?.let { "${ApiConfig.IMAGE_BASE_URL}$it" } ?: "",
        voteAverage = voteAverage ?: 0.0,
        voteCount = voteCount ?: 0,
        releaseDate = releaseDate ?: "",
        genres = genres?.map { it.name } ?: emptyList(),
        runtime = runtime ?: 0
    )
}

fun MovieDetail.toEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        voteAverage = voteAverage,
        voteCount = voteCount,
        releaseDate = releaseDate,
        genres = genres,
        runtime = runtime,
        isFavorite = true
    )
}

fun MovieEntity.toFavoriteDomain(): FavoriteMovie {
    return FavoriteMovie(
        id = id,
        title = title,
        posterPath = posterPath,
        releaseDate = releaseDate,
        runtime = runtime,
        genres = genres,
        voteAverage = voteAverage
    )
}

fun MovieEntity.toDetailDomain(): MovieDetail {
    return MovieDetail(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        voteAverage = voteAverage,
        voteCount = voteCount,
        releaseDate = releaseDate,
        genres = genres,
        runtime = runtime
    )
}

fun ReviewDto.toDomain(): Review {
    return Review(
        id = id,
        author = author,
        content = content,
        createdAt = createdAt.take(10),
        rating = authorDetails?.rating
    )
}
