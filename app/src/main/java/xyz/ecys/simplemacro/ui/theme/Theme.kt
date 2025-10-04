package xyz.ecys.simplemacro.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = Color(0xFF0A0A0A),
    primaryContainer = Color(0xFF6B21A8),
    onPrimaryContainer = Color(0xFFF3E8FF),
    secondary = SecondaryDark,
    onSecondary = Color(0xFF0A0A0A),
    tertiary = TertiaryDark,
    background = BackgroundDark,
    onBackground = Color(0xFFF5F5F5),
    surface = SurfaceDark,
    onSurface = Color(0xFFF5F5F5),
    surfaceVariant = Color(0xFF262626),
    onSurfaceVariant = Color(0xFFD4D4D4),
    error = ErrorDark,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFF3E8FF),
    onPrimaryContainer = Color(0xFF5B21B6),
    secondary = SecondaryLight,
    onSecondary = Color.White,
    tertiary = TertiaryLight,
    background = BackgroundLight,
    onBackground = Color(0xFF0A0A0A),
    surface = SurfaceLight,
    onSurface = Color(0xFF0A0A0A),
    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = Color(0xFF525252),
    error = ErrorLight,
    onError = Color.White
)

@Composable
fun SimpleMacroTheme(
    darkTheme: Boolean = true, // Default to dark mode
    dynamicColor: Boolean = true,
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
