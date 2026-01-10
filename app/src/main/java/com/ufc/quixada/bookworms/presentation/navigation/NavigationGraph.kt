package com.ufc.quixada.bookworms.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ufc.quixada.bookworms.presentation.auth.login.LoginScreen
import com.ufc.quixada.bookworms.presentation.auth.register.RegisterScreen
import com.ufc.quixada.bookworms.presentation.book_details.BookDetailsScreen
import com.ufc.quixada.bookworms.presentation.main.MainScreen
import com.ufc.quixada.bookworms.presentation.splash.SplashScreen

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Main : Screen("main")

    // ADICIONADO: Definição da rota de Perfil Público
    data object PublicProfile : Screen("public_profile/{userId}") {
        fun createRoute(userId: String) = "public_profile/$userId"
    }

    data object BookDetails : Screen("book_details/{bookId}") {
        fun createRoute(bookId: String) = "book_details/$bookId"
    }
}

@Composable
fun NavigationGraph(navController: androidx.navigation.NavHostController) { // Certifique-se de usar o parâmetro
    // Removido o rememberNavController() interno para usar o que vem da MainActivity

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
                    navController.popBackStack()
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

        // Se você precisar acessar o perfil público de fora da MainScreen (navegação global)
        composable(
            route = Screen.PublicProfile.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) {
            // Importar PublicProfileScreen se necessário aqui, mas geralmente
            // ele está sendo usado dentro do NavHost interno da MainScreen.
            // Se for navegação global, adicione a chamada aqui.
        }

        composable(
            route = Screen.BookDetails.route,
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) {
            BookDetailsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}