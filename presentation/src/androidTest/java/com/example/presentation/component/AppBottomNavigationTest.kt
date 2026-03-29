package com.example.presentation.component

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.presentation.navigation.Screen
import com.example.presentation.theme.ThousandsCoursesTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppBottomNavigationTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun appBottomNavigation_show_three_item_menu() {
        rule.setContent {
            ThousandsCoursesTheme {
                val navController = rememberNavController()
                AppBottomNavigation(navController = navController, currentRoute = Screen.Courses.route)
            }
        }

        rule.onNodeWithText("Главная").assertIsDisplayed()
        rule.onNodeWithText("Избранное").assertIsDisplayed()
        rule.onNodeWithText("Аккаунт").assertIsDisplayed()
    }

    @Test
    fun appBottomNavigation_current_item_active() {
        rule.setContent {
            ThousandsCoursesTheme {
                val navController = rememberNavController()
                AppBottomNavigation(navController = navController, currentRoute = Screen.Favorites.route)
            }
        }

        rule.onNodeWithText("Избранное").assertIsSelected()
    }

    @Test
    fun appBottomNavigation_on_click_item() {
        rule.setContent {
            ThousandsCoursesTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Column {
                    NavHost(navController = navController, startDestination = Screen.Courses.route) {
                        composable(Screen.Courses.route) {}
                        composable(Screen.Favorites.route) {}
                        composable(Screen.Profile.route) {}
                    }
                    AppBottomNavigation(navController = navController, currentRoute = currentRoute)
                }
            }
        }

        rule.onNodeWithText("Избранное").performClick()

        rule.onNodeWithText("Избранное").assertIsSelected()
    }
}
