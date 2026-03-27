package com.example.presentation.util

import com.example.presentation.R
import com.example.presentation.navigation.Screen

object Constants {
    const val VK_URL = "https://vk.com/"

    const val OK_URL = "https://ok.ru/"

    val BottomNavItems = listOf(
        BottomNavItem(
            labelId = R.string.home,
            iconId = R.drawable.ic_home,
            route = Screen.Courses.route
        ),
        BottomNavItem(
            labelId = R.string.favorites,
            iconId = R.drawable.ic_favorites,
            route = Screen.Favorites.route
        ),
        BottomNavItem(
            labelId = R.string.profile,
            iconId = R.drawable.ic_profile,
            route = Screen.Profile.route
        )
    )
}

data class BottomNavItem(
    val labelId: Int,
    val iconId: Int,
    val route: String,
)