package com.ufc.quixada.bookworms.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    data object Feed : BottomNavItem("feed", "Feed", Icons.Default.Home)
    data object Catalog : BottomNavItem("catalog", "Catálogo", Icons.Default.Search)
    data object Notifications : BottomNavItem("notifications", "Notificações", Icons.Default.Notifications)
    data object Profile : BottomNavItem("publicprofile", "Perfil", Icons.Default.Person)
}