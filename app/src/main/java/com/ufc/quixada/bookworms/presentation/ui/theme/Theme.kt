package com.ufc.quixada.bookworms.presentation.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = BW_Primary_Green,
    onPrimary = BW_Dark_Green, // Texto escuro sobre o verde claro para leitura
    primaryContainer = BW_Dark_Green,
    onPrimaryContainer = BW_Primary_Green,

    secondary = BW_Secondary_Purple,
    onSecondary = BW_White,
    secondaryContainer = BW_Secondary_Purple,
    onSecondaryContainer = BW_White,

    tertiary = BW_Dark_Green,

    background = BW_Black,
    onBackground = BW_White,

    surface = BW_Black,
    onSurface = BW_White,

    error = BW_Secondary_Purple // Usando o roxo como destaque de erro/atenção no tema escuro se desejar, ou manter padrão
)

private val LightColorScheme = lightColorScheme(
    primary = BW_Primary_Green,
    onPrimary = BW_Dark_Green, // Texto escuro sobre o verde claro
    primaryContainer = BW_Primary_Green,
    onPrimaryContainer = BW_Dark_Green,

    secondary = BW_Secondary_Purple,
    onSecondary = BW_White,

    tertiary = BW_Dark_Green,
    onTertiary = BW_White,

    background = BW_White,
    onBackground = BW_Black,

    surface = BW_White,
    onSurface = BW_Black,
)

@Composable
fun BookwormsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color desativado por padrão para garantir que a identidade visual seja usada
    // Se quiser que o Android 12+ use as cores do papel de parede do usuário, mude para true
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}