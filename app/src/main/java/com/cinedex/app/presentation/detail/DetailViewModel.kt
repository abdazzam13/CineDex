package com.cinedex.app.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinedex.app.domain.model.MovieDetail
import com.cinedex.app.domain.model.Review
import com.cinedex.app.domain.repository.MovieRepository
import com.cinedex.app.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieId: Int = savedStateHandle.get<Int>("movieId") ?: 0

    private val _movieDetailState = MutableStateFlow<UiState<MovieDetail>>(UiState.Loading)
    val movieDetailState: StateFlow<UiState<MovieDetail>> = _movieDetailState.asStateFlow()

    private val _reviewsState = MutableStateFlow<UiState<List<Review>>>(UiState.Loading)
    val reviewsState: StateFlow<UiState<List<Review>>> = _reviewsState.asStateFlow()

    val isFavorite: StateFlow<Boolean> = repository.isFavorite(movieId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = false
        )

    init {
        if (movieId > 0) {
            loadDetail(movieId)
            loadReviews(movieId)
        }
    }

    fun loadDetail(id: Int = movieId) {
        viewModelScope.launch {
            _movieDetailState.value = UiState.Loading
            repository.getMovieDetail(id)
                .onSuccess { _movieDetailState.value = UiState.Success(it) }
                .onFailure { _movieDetailState.value = UiState.Error(it.message ?: "Failed to load movie details") }
        }
    }

    fun loadReviews(id: Int = movieId) {
        viewModelScope.launch {
            _reviewsState.value = UiState.Loading
            repository.getMovieReviews(id)
                .onSuccess { _reviewsState.value = UiState.Success(it) }
                .onFailure { _reviewsState.value = UiState.Error(it.message ?: "Failed to load reviews") }
        }
    }

    fun toggleFavorite(onResult: (Boolean) -> Unit = {}) {
        val currentDetail = (_movieDetailState.value as? UiState.Success)?.data ?: return
        val currentlyFav = isFavorite.value

        viewModelScope.launch {
            if (currentlyFav) {
                repository.removeFavorite(currentDetail.id)
                onResult(false)
            } else {
                repository.saveFavorite(currentDetail)
                onResult(true)
            }
        }
    }
}
