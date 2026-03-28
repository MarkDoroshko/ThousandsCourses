package com.example.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import com.example.presentation.component.AppBottomNavigation
import com.example.presentation.screen.login.LoginScreen
import kotlin.collections.contains
import androidx.navigation.compose.NavHost
import com.example.presentation.screen.courses.CoursesScreen
import com.example.presentation.screen.favorites.FavoritesScreen

@Composable
fun NavGraph(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val noBarRoutes = listOf(Screen.Login.route)
    val showBottomBar = currentRoute !in noBarRoutes

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (showBottomBar) {
                AppBottomNavigation(
                    navController = navController,
                    currentRoute = currentRoute
                )
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val screenOrder = listOf(
                Screen.Login.route,
                Screen.Courses.route,
                Screen.Favorites.route,
                Screen.Profile.route
            )

            NavHost(
                modifier = modifier.fillMaxSize(),
                navController = navController,
                startDestination = Screen.Login.route,
                enterTransition = {
                    val from = screenOrder.indexOf(initialState.destination.route)
                    val to = screenOrder.indexOf(targetState.destination.route)
                    slideInHorizontally(initialOffsetX = { if (to >= from) it else -it })
                },
                exitTransition = {
                    val from = screenOrder.indexOf(initialState.destination.route)
                    val to = screenOrder.indexOf(targetState.destination.route)
                    slideOutHorizontally(targetOffsetX = { if (to >= from) -it else it })
                },
                popEnterTransition = {
                    val from = screenOrder.indexOf(initialState.destination.route)
                    val to = screenOrder.indexOf(targetState.destination.route)
                    slideInHorizontally(initialOffsetX = { if (to >= from) it else -it })
                },
                popExitTransition = {
                    val from = screenOrder.indexOf(initialState.destination.route)
                    val to = screenOrder.indexOf(targetState.destination.route)
                    slideOutHorizontally(targetOffsetX = { if (to >= from) -it else it })
                }
            ) {
                composable(Screen.Login.route) {
                    LoginScreen(
                        onSubmit = {
                            navController.navigate(Screen.Courses.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }

                composable(Screen.Courses.route) { CoursesScreen() }

                composable(Screen.Favorites.route) { FavoritesScreen() }

                composable(Screen.Profile.route) {  }
            }
        }
    }
}

sealed class Screen(val route: String) {

    data object Login : Screen("login")

    data object Courses : Screen("courses")

    data object Favorites : Screen("favorites")

    data object Profile : Screen("profile")
}