package com.example.xepartnerapp.navigation

sealed class Screen(val route: String) {
    object Welcome : Screen(route = "welcome_screen")
    object LoginAndSignUp : Screen(route = "LoginAndSignUp_screen")
}
