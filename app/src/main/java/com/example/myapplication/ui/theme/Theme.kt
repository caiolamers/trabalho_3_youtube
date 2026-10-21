package com.example.myapplication.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaEscuro = darkColorScheme(
    primary = Color.White,
    onPrimary = Color.Black,
    secondary = Destaque,
    background = FundoEscuro,
    onBackground = Color.White,
    surface = FundoEscuro,
    onSurface = Color.White,
    surfaceVariant = SuperficieEscura,
    onSurfaceVariant = Color.LightGray
)

// O aplicativo usa apenas o tema escuro, igual às telas existentes
@Composable
fun MyApplicationTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaEscuro,
        typography = Typography,
        content = content
    )
}
