package com.ufc.quixada.bookworms.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.ufc.quixada.bookworms.presentation.feed.FeedScreen
import com.ufc.quixada.bookworms.presentation.home.HomeScreen
import com.ufc.quixada.bookworms.presentation.navigation.BottomNavItem
import com.ufc.quixada.bookworms.presentation.notification.NotificationScreen
import com.ufc.quixada.bookworms.presentation.profile.ProfileScreen
import com.ufc.quixada.bookworms.presentation.public_profile.PublicProfileScreen

@Composable
fun MainScreen(
    onLogout: () -> Unit,
    onBookClick: (String) -> Unit
) {
    val navController = rememberNavController()
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val navigateToUserProfile = { userId: String ->
        navController.navigate("public_profile/$userId")
    }

    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navController,
                currentUserId = currentUserId
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Feed.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(BottomNavItem.Feed.route) {
                FeedScreen(onBookClick = onBookClick)
            }

            composable(BottomNavItem.Catalog.route) {
                HomeScreen(
                    onBookClick = onBookClick,
                    onUserClick = navigateToUserProfile
                )
            }

            composable(BottomNavItem.Notifications.route) {
                NotificationScreen()
            }

            composable(
                route = "public_profile/{userId}",
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
                    }
                )
            }

            composable("profile_edit") {
                ProfileScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onLogout = onLogout
                )
            }
        }
    }
}

@Composable
fun BottomBar(
    navController: NavHostController,
    currentUserId: String
) {
    val items = listOf(
        BottomNavItem.Feed,
        BottomNavItem.Catalog,
        BottomNavItem.Notifications,
        BottomNavItem.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
    ) {
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .shadow(8.dp, RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp)),
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            items.forEach { item ->
                val isProfileItem = item == BottomNavItem.Profile

                val selected = if (isProfileItem) {
                    currentDestination?.route?.startsWith("public_profile") == true ||
                            currentDestination?.route == "profile_edit"
                } else {
                    currentDestination?.hierarchy?.any { it.route == item.route } == true
                }

                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title
                        )
                    },
                    selected = selected,
                    onClick = {
                        val route = if (isProfileItem) {
                            "public_profile/$currentUserId"
                        } else {
                            item.route
                        }

                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = Color.Gray,
                        indicatorColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        }
    }
}