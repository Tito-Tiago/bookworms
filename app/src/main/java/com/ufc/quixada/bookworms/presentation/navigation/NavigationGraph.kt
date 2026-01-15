package com.ufc.quixada.bookworms.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ufc.quixada.bookworms.presentation.auth.login.LoginScreen
import com.ufc.quixada.bookworms.presentation.auth.register.RegisterScreen
import com.ufc.quixada.bookworms.presentation.book_details.BookDetailsScreen
import com.ufc.quixada.bookworms.presentation.main.MainScreen
import com.ufc.quixada.bookworms.presentation.splash.SplashScreen
import com.ufc.quixada.bookworms.presentation.public_profile.PublicProfileScreen

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Main : Screen("main")
    data object PublicProfile : Screen("public_profile/{userId}") {
        fun createRoute(userId: String) = "public_profile/$userId"
    }
    data object BookDetails : Screen("book_details/{bookId}") {
        fun createRoute(bookId: String) = "book_details/$bookId"
    }
}

@Composable
fun NavigationGraph(navController: androidx.navigation.NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onTimeout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                },
                onRegisterSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Main.route) {
            MainScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                },
                onBookClick = { bookId ->
                    navController.navigate(Screen.BookDetails.createRoute(bookId))
                }
            )
        }

        composable(
            route = Screen.PublicProfile.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) {
            PublicProfileScreen(
                onNavigateBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                },
                onEditClick = {
                    navController.navigate("profile_edit")
                },
                onBookClick = { bookId ->
                    navController.navigate(Screen.BookDetails.createRoute(bookId))
                }
            )
        }

        composable(
            route = Screen.BookDetails.route,
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) {
            BookDetailsScreen(
                onNavigateBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                }
            )
        }
    }
}