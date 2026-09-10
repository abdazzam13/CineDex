package com.cinedex.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String,
    val backdropPath: String,
    val voteAverage: Double,
    val voteCount: Int,
    val releaseDate: String,
    val genres: List<String>,
    val runtime: Int,
    val isFavorite: Boolean = true,
    val category: String? = null,
    val savedAt: Long = System.currentTimeMillis()
)
