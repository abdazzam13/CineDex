package com.cinedex.app.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinedex.app.domain.model.Movie
import com.cinedex.app.domain.repository.MovieRepository
import com.cinedex.app.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _nowPlayingState = MutableStateFlow<UiState<List<Movie>>>(UiState.Loading)
    val nowPlayingState: StateFlow<UiState<List<Movie>>> = _nowPlayingState.asStateFlow()

    private val _popularState = MutableStateFlow<UiState<List<Movie>>>(UiState.Loading)
    val popularState: StateFlow<UiState<List<Movie>>> = _popularState.asStateFlow()

    private val _topRatedState = MutableStateFlow<UiState<List<Movie>>>(UiState.Loading)
    val topRatedState: StateFlow<UiState<List<Movie>>> = _topRatedState.asStateFlow()

    private val _selectedGenre = MutableStateFlow("All")
    val selectedGenre: StateFlow<String> = _selectedGenre.asStateFlow()

    init {
        loadAllMovies()
    }

    fun selectGenre(genre: String) {
        _selectedGenre.value = genre
    }

    fun loadAllMovies() {
        loadNowPlaying()
        loadPopular()
        loadTopRated()
    }

    private fun loadNowPlaying() {
        viewModelScope.launch {
            _nowPlayingState.value = UiState.Loading
            repository.getNowPlayingMovies()
                .onSuccess { _nowPlayingState.value = UiState.Success(it) }
                .onFailure { _nowPlayingState.value = UiState.Error(it.message ?: "Failed to load now playing movies") }
        }
    }

    private fun loadPopular() {
        viewModelScope.launch {
            _popularState.value = UiState.Loading
            repository.getPopularMovies()
                .onSuccess { _popularState.value = UiState.Success(it) }
                .onFailure { _popularState.value = UiState.Error(it.message ?: "Failed to load popular movies") }
        }
    }

    private fun loadTopRated() {
        viewModelScope.launch {
            _topRatedState.value = UiState.Loading
            repository.getTopRatedMovies()
                .onSuccess { _topRatedState.value = UiState.Success(it) }
                .onFailure { _topRatedState.value = UiState.Error(it.message ?: "Failed to load top rated movies") }
        }
    }
}
