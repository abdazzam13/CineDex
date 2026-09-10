package com.cinedex.app

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cinedex.app.navigation.NavRoutes
import com.cinedex.app.presentation.common.CineDexBottomNav
import com.cinedex.app.presentation.detail.MovieDetailScreen
import com.cinedex.app.presentation.favorite.FavoriteScreen
import com.cinedex.app.presentation.home.HomeScreen
import com.cinedex.app.presentation.list.MovieListScreen
import com.cinedex.app.ui.theme.CineDexTheme

@Composable
fun CineDexApp() {
    CineDexTheme {
        val navController = rememberNavController()
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        val bottomNavRoutes = listOf(NavRoutes.HOME, NavRoutes.FAVORITE)

        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                if (currentRoute in bottomNavRoutes) {
                    CineDexBottomNav(
                        currentRoute = currentRoute,
                        onNavigate = { route ->
                            navController.navigate(route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = NavRoutes.HOME,
                modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
            ) {
                composable(NavRoutes.HOME) { backStack ->
                    HomeScreen(
                        onMovieClick = { movieId ->
                            if (backStack.lifecycle.currentState == Lifecycle.State.RESUMED) {
                                navController.navigate(NavRoutes.detailRoute(movieId))
                            }
                        },
                        onSeeMoreClick = { category ->
                            if (backStack.lifecycle.currentState == Lifecycle.State.RESUMED) {
                                navController.navigate(NavRoutes.movieListRoute(category))
                            }
                        },
                        onFavoriteClick = {
                            if (backStack.lifecycle.currentState == Lifecycle.State.RESUMED) {
                                navController.navigate(NavRoutes.FAVORITE) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
                composable(NavRoutes.FAVORITE) { backStack ->
                    FavoriteScreen(
                        onMovieClick = { movieId ->
                            if (backStack.lifecycle.currentState == Lifecycle.State.RESUMED) {
                                navController.navigate(NavRoutes.detailRoute(movieId))
                            }
                        },
                        onExploreClick = {
                            if (backStack.lifecycle.currentState == Lifecycle.State.RESUMED) {
                                navController.navigate(NavRoutes.HOME) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
                composable(
                    route = NavRoutes.DETAIL,
                    arguments = listOf(navArgument("movieId") { type = NavType.IntType })
                ) { backStack ->
                    val movieId = backStack.arguments?.getInt("movieId") ?: 0
                    MovieDetailScreen(
                        movieId = movieId,
                        onBackClick = {
                            if (backStack.lifecycle.currentState == Lifecycle.State.RESUMED &&
                                navController.currentDestination?.route == NavRoutes.DETAIL) {
                                navController.popBackStack()
                            }
                        }
                    )
                }
                composable(
                    route = NavRoutes.MOVIE_LIST,
                    arguments = listOf(navArgument("category") { type = NavType.StringType })
                ) { backStack ->
                    val category = backStack.arguments?.getString("category") ?: "popular"
                    MovieListScreen(
                        category = category,
                        onMovieClick = { movieId ->
                            if (backStack.lifecycle.currentState == Lifecycle.State.RESUMED) {
                                navController.navigate(NavRoutes.detailRoute(movieId))
                            }
                        },
                        onBackClick = {
                            if (backStack.lifecycle.currentState == Lifecycle.State.RESUMED &&
                                navController.currentDestination?.route == NavRoutes.MOVIE_LIST) {
                                navController.popBackStack()
                            }
                        }
                    )
                }
            }
        }
    }
}
