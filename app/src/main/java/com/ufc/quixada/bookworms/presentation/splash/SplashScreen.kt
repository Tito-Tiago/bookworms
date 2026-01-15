package com.ufc.quixada.bookworms.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ufc.quixada.bookworms.R
import kotlinx.coroutines.delay

private const val SPLASH_DURATION_MS = 2000L

@Composable
fun SplashScreen(
    onTimeout: () -> Unit
) {
    LaunchedEffect(key1 = true) {
        delay(SPLASH_DURATION_MS)
        onTimeout()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "Logo Bookworms",
            modifier = Modifier.size(120.dp)
        )

        Text(
            text = "Bookworms",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 48.sp
            ),
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}