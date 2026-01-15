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
    // Primária: Verde Claro (Mantém a identidade principal legível no escuro)
    primary = BW_Secondary_LightGreen,
    onPrimary = BW_Primary_DarkGreen,
    primaryContainer = BW_Primary_DarkGreen,
    onPrimaryContainer = BW_Secondary_LightGreen,

    // Secundária: Lilás (Acentos e botões flutuantes)
    secondary = BW_Purple_Light,
    onSecondary = BW_Purple_Deep,
    secondaryContainer = BW_Purple_Deep,
    onSecondaryContainer = BW_Purple_Light,

    // Terciária: Lilás também para consistência
    tertiary = BW_Purple_Light,
    onTertiary = BW_Purple_Deep,

    background = BW_Background_Black,
    onBackground = BW_White,

    surface = BW_Surface_DarkGrey, // Cards principais continuam cinza escuro para não cansar a vista
    onSurface = BW_White,

    // --- MUDANÇA AQUI: Fundo Secundário agora é ROXO ---
    // Containers secundários agora terão o fundo roxo escuro
    surfaceVariant = BW_Purple_Deep,
    onSurfaceVariant = BW_Purple_Light, // Texto lilás sobre o fundo roxo

    error = BW_Error,
    onError = BW_White
)

private val LightColorScheme = lightColorScheme(
    // Light Mode mantém o visual "Clean" Creme + Verde
    primary = BW_Primary_DarkGreen,
    onPrimary = BW_White,

    secondary = BW_Secondary_LightGreen,
    onSecondary = BW_Primary_DarkGreen,

    tertiary = BW_Purple_Deep, // Detalhes sutis em roxo
    onTertiary = BW_White,

    background = BW_Background_YellowishWhite,
    onBackground = BW_Primary_DarkGreen,

    surface = BW_White,
    onSurface = BW_Primary_DarkGreen,

    surfaceVariant = BW_Detail_PaleGreen, // Fundo secundário verde pálido
    onSurfaceVariant = BW_Primary_DarkGreen,

    error = BW_Error,
    onError = BW_White
)

@Composable
fun BookwormsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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