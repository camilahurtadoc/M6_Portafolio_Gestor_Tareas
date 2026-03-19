package com.tecsup.taskmanagerpro.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Esquema de colores para el tema claro
 */
private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = White,
    primaryContainer = PrimaryLight,
    onPrimaryContainer = PrimaryDark,
    secondary = Secondary,
    onSecondary = White,
    secondaryContainer = SecondaryDark,
    onSecondaryContainer = White,
    tertiary = InfoColor,
    onTertiary = White,
    error = ErrorColor,
    onError = White,
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = ErrorColor,
    background = Background,
    onBackground = TextPrimary,
    surface = Surface,
    onSurface = TextPrimary,
    surfaceVariant = GrayLight,
    onSurfaceVariant = TextSecondary,
    outline = Gray,
    outlineVariant = GrayLight,
    scrim = Black.copy(alpha = 0.32f)
)

/**
 * Esquema de colores para el tema oscuro
 */
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryLight,
    onPrimary = PrimaryDark,
    primaryContainer = PrimaryDark,
    onPrimaryContainer = PrimaryLight,
    secondary = Secondary,
    onSecondary = Color(0xFF003735),
    secondaryContainer = SecondaryDark,
    onSecondaryContainer = Color(0xFF6FF7EC),
    tertiary = InfoColor,
    onTertiary = White,
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF1A1C1E),
    onBackground = Color(0xFFE2E2E6),
    surface = Color(0xFF1A1C1E),
    onSurface = Color(0xFFE2E2E6),
    surfaceVariant = Color(0xFF42474E),
    onSurfaceVariant = Color(0xFFC2C7CF),
    outline = Color(0xFF8C9199),
    outlineVariant = Color(0xFF42474E),
    scrim = Black
)

/**
 * Tema de la aplicación TaskManager Pro
 * @param darkTheme Si se debe usar el tema oscuro
 * @param content Contenido de la aplicación
 */
@Composable
fun TaskManagerProTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
