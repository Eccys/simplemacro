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
    primary = PrimaryDark, // White buttons
    onPrimary = Color(0xFF121212), // Dark text on white buttons
    primaryContainer = Color(0xFF2A2A2A),
    onPrimaryContainer = PearlWhite,
    secondary = SecondaryDark,
    onSecondary = Color(0xFF121212),
    tertiary = TertiaryDark,
    background = BackgroundDark, // #121212
    onBackground = PearlWhite, // Pearl white text
    surface = SurfaceDark,
    onSurface = PearlWhite, // Pearl white text
    surfaceVariant = Color(0xFF1E1E1E),
    onSurfaceVariant = Color(0xFFE5E5E5),
    error = ErrorDark,
    onError = Color(0xFF121212)
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight, // Dark buttons
    onPrimary = Color.White, // White text on dark buttons
    primaryContainer = Color(0xFFE5E5E5),
    onPrimaryContainer = Color(0xFF121212),
    secondary = SecondaryLight,
    onSecondary = Color.White,
    tertiary = TertiaryLight,
    background = BackgroundLight, // Light background
    onBackground = Color(0xFF121212), // Dark text
    surface = SurfaceLight,
    onSurface = Color(0xFF121212), // Dark text
    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = Color(0xFF404040),
    error = ErrorLight,
    onError = Color.White
)

@Composable
fun SimpleMacroTheme(
    darkTheme: Boolean = true, // Default to dark mode
    dynamicColor: Boolean = false, // Disabled for monochrome theme
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
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
