package com.ufc.quixada.bookworms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ufc.quixada.bookworms.presentation.navigation.NavigationGraph
import com.ufc.quixada.bookworms.presentation.ui.theme.BookwormsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookwormsTheme {
                NavigationGraph()
            }
        }
    }
}