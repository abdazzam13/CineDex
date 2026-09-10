package com.cinedex.app.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cinedex.app.domain.model.Movie
import com.cinedex.app.domain.model.formattedRating
import com.cinedex.app.domain.model.releaseYear
import com.cinedex.app.presentation.common.GenreFilterRow
import com.cinedex.app.presentation.common.MoviePosterCard
import com.cinedex.app.presentation.common.SectionHeader
import com.cinedex.app.presentation.common.UiState
import com.cinedex.app.util.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onMovieClick: (Int) -> Unit,
    onSeeMoreClick: (String) -> Unit,
    onFavoriteClick: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val selectedGenre by viewModel.selectedGenre.collectAsState()
    val nowPlayingState by viewModel.nowPlayingState.collectAsState()
    val popularState by viewModel.popularState.collectAsState()
    val topRatedState by viewModel.topRatedState.collectAsState()

    val selectedGenreId = Constants.GENRE_MAP[selectedGenre]

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "CineDex",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                actions = {
                    IconButton(onClick = onFavoriteClick) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorites",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                GenreFilterRow(
                    selectedGenre = selectedGenre,
                    onGenreSelected = { viewModel.selectGenre(it) },
                    genres = Constants.GENRES,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SectionHeader(
                        title = "Now Playing",
                        onSeeMoreClick = { onSeeMoreClick("now_playing") }
                    )
                    MovieSectionContent(
                        state = nowPlayingState,
                        selectedGenre = selectedGenre,
                        selectedGenreId = selectedGenreId,
                        cardWidth = 148.dp,
                        badgeText = "NOW PLAYING",
                        onMovieClick = onMovieClick
                    )
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SectionHeader(
                        title = "Popular Movies",
                        onSeeMoreClick = { onSeeMoreClick("popular") }
                    )
                    MovieSectionContent(
                        state = popularState,
                        selectedGenre = selectedGenre,
                        selectedGenreId = selectedGenreId,
                        cardWidth = 136.dp,
                        showRank = true,
                        onMovieClick = onMovieClick
                    )
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SectionHeader(
                        title = "Top Rated Movies",
                        onSeeMoreClick = { onSeeMoreClick("top_rated") }
                    )
                    MovieSectionContent(
                        state = topRatedState,
                        selectedGenre = selectedGenre,
                        selectedGenreId = selectedGenreId,
                        cardWidth = 136.dp,
                        onMovieClick = onMovieClick
                    )
                }
            }
        }
    }
}

@Composable
private fun MovieSectionContent(
    state: UiState<List<Movie>>,
    selectedGenre: String,
    selectedGenreId: Int?,
    cardWidth: Dp,
    onMovieClick: (Int) -> Unit,
    badgeText: String? = null,
    showRank: Boolean = false
) {
    when (state) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(28.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        is UiState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        is UiState.Success -> {
            val filtered = if (selectedGenreId != null) {
                state.data.filter { it.genreIds.contains(selectedGenreId) }
            } else {
                state.data
            }
            if (filtered.isEmpty()) {
                Text(
                    text = "No movies found for $selectedGenre",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            } else {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (showRank) {
                        itemsIndexed(filtered, key = { _, movie -> movie.id }) { index, movie ->
                            MoviePosterCard(
                                title = movie.title,
                                posterUrl = movie.posterPath,
                                rating = movie.formattedRating(),
                                releaseYear = movie.releaseYear(),
                                onClick = { onMovieClick(movie.id) },
                                cardWidth = cardWidth,
                                rank = index + 1
                            )
                        }
                    } else {
                        items(filtered, key = { it.id }) { movie ->
                            MoviePosterCard(
                                title = movie.title,
                                posterUrl = movie.posterPath,
                                rating = movie.formattedRating(),
                                releaseYear = movie.releaseYear(),
                                onClick = { onMovieClick(movie.id) },
                                cardWidth = cardWidth,
                                badgeText = badgeText
                            )
                        }
                    }
                }
            }
        }
    }
}
