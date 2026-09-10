package com.cinedex.app.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinedex.app.domain.model.FavoriteMovie
import com.cinedex.app.domain.model.MovieDetail
import com.cinedex.app.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    val favoriteMovies: StateFlow<List<FavoriteMovie>> = repository.getFavoriteMovies()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    init {
        syncFavorites()
    }

    fun syncFavorites() {
        viewModelScope.launch {
            _isSyncing.value = true
            try {
                repository.syncFavorites()
            } finally {
                _isSyncing.value = false
            }
        }
    }

    fun removeFavorite(movieId: Int) {
        viewModelScope.launch {
            repository.removeFavorite(movieId)
        }
    }

    fun restoreFavorite(movie: FavoriteMovie) {
        viewModelScope.launch {
            val detail = MovieDetail(
                id = movie.id,
                title = movie.title,
                overview = "",
                posterPath = movie.posterPath,
                backdropPath = "",
                voteAverage = movie.voteAverage,
                voteCount = 0,
                releaseDate = movie.releaseDate,
                genres = movie.genres,
                runtime = movie.runtime
            )
            repository.saveFavorite(detail)
        }
    }

    fun clearAllFavorites() {
        viewModelScope.launch {
            repository.clearAllFavorites()
        }
    }
}
