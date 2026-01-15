package com.ufc.quixada.bookworms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.ufc.quixada.bookworms.presentation.navigation.NavigationGraph
import com.ufc.quixada.bookworms.presentation.ui.theme.BookwormsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }

            BookwormsTheme(darkTheme = isDarkTheme) {
                val navController = rememberNavController()

                NavigationGraph(
                    navController = navController,
                    isDarkTheme = isDarkTheme,
                    onThemeSwitch = { isDarkTheme = !isDarkTheme }
                )
            }
        }
    }
}