package com.cinedex.app.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cinedex.app.domain.model.formattedRating
import com.cinedex.app.domain.model.releaseYear
import com.cinedex.app.presentation.common.GenreFilterRow
import com.cinedex.app.presentation.common.MoviePosterCard
import com.cinedex.app.presentation.common.UiState
import com.cinedex.app.util.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(
    category: String,
    onMovieClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MovieListViewModel = hiltViewModel()
) {
    val moviesState by viewModel.moviesState.collectAsState()
    val selectedGenre by viewModel.selectedGenre.collectAsState()
    val isNextPageLoading by viewModel.isNextPageLoading.collectAsState()

    val selectedGenreId = Constants.GENRE_MAP[selectedGenre]

    var isBackNavigating by remember { mutableStateOf(false) }

    fun handleBackClick() {
        if (!isBackNavigating) {
            isBackNavigating = true
            onBackClick()
        }
    }

    BackHandler(enabled = !isBackNavigating) {
        handleBackClick()
    }

    val categoryTitle = when (category) {
        "now_playing" -> "Now Playing"
        "top_rated" -> "Top Rated"
        "upcoming" -> "Upcoming"
        else -> "Popular Movies"
    }

    val gridState = rememberLazyGridState()
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisible = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = gridState.layoutInfo.totalItemsCount
            totalItems > 0 && lastVisible >= totalItems - Constants.PREFETCH_DISTANCE
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && !isNextPageLoading) {
            viewModel.loadMovies()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = categoryTitle,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { handleBackClick() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            GenreFilterRow(
                selectedGenre = selectedGenre,
                onGenreSelected = { viewModel.selectGenre(it) },
                genres = Constants.GENRES,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            if (isNextPageLoading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            when (val state = moviesState) {
                is UiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(40.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                is UiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = state.message,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error
                            )
                            Button(onClick = { viewModel.loadMovies(isRefresh = true) }) {
                                Text(text = "Retry")
                            }
                        }
                    }
                }
                is UiState.Success -> {
                    val filtered = if (selectedGenreId != null) {
                        state.data.filter { it.genreIds.contains(selectedGenreId) }
                    } else {
                        state.data
                    }

                    if (filtered.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No movies found for $selectedGenre",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            state = gridState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(filtered, key = { it.id }) { movie ->
                                MoviePosterCard(
                                    title = movie.title,
                                    posterUrl = movie.posterPath,
                                    rating = movie.formattedRating(),
                                    releaseYear = movie.releaseYear(),
                                    onClick = { onMovieClick(movie.id) },
                                    cardWidth = 200.dp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
