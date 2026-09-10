package com.cinedex.app.presentation.list

import androidx.lifecycle.SavedStateHandle
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
class MovieListViewModel @Inject constructor(
    private val repository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val category: String = savedStateHandle.get<String>("category") ?: "popular"

    private val _moviesState = MutableStateFlow<UiState<List<Movie>>>(UiState.Loading)
    val moviesState: StateFlow<UiState<List<Movie>>> = _moviesState.asStateFlow()

    private val _selectedGenre = MutableStateFlow("All")
    val selectedGenre: StateFlow<String> = _selectedGenre.asStateFlow()

    private val _isNextPageLoading = MutableStateFlow(false)
    val isNextPageLoading: StateFlow<Boolean> = _isNextPageLoading.asStateFlow()

    private var currentPage = 1
    private var isEndReached = false
    private val allLoadedMovies = mutableListOf<Movie>()
    private var isFetching = false

    init {
        loadMovies(isRefresh = true)
    }

    fun selectGenre(genre: String) {
        _selectedGenre.value = genre
    }

    fun loadMovies(isRefresh: Boolean = false) {
        if (isFetching) return
        if (isRefresh) {
            currentPage = 1
            isEndReached = false
            allLoadedMovies.clear()
            _moviesState.value = UiState.Loading
        } else {
            if (isEndReached) return
            _isNextPageLoading.value = true
        }

        isFetching = true
        viewModelScope.launch {
            val result = when (category) {
                "now_playing" -> repository.getNowPlayingMovies(currentPage)
                "top_rated" -> repository.getTopRatedMovies(currentPage)
                "upcoming" -> repository.getUpcomingMovies(currentPage)
                else -> repository.getPopularMovies(currentPage)
            }

            result.fold(
                onSuccess = { newMovies ->
                    if (newMovies.isEmpty()) {
                        isEndReached = true
                    } else {
                        val distinct = (allLoadedMovies + newMovies).distinctBy { it.id }
                        allLoadedMovies.clear()
                        allLoadedMovies.addAll(distinct)
                        currentPage++
                    }
                    _moviesState.value = UiState.Success(allLoadedMovies.toList())
                    _isNextPageLoading.value = false
                    isFetching = false
                },
                onFailure = { error ->
                    if (allLoadedMovies.isEmpty()) {
                        _moviesState.value = UiState.Error(error.localizedMessage ?: "Failed to load movies")
                    }
                    _isNextPageLoading.value = false
                    isFetching = false
                }
            )
        }
    }
}
