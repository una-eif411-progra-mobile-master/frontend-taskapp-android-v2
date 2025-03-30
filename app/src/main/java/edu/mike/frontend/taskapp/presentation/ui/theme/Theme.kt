package edu.mike.frontend.taskapp.presentation.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Dark color scheme definition for the application.
 *
 * This color scheme is used when the app is in dark theme mode and when
 * dynamic color is disabled or not available.
 */
private val DarkColorScheme = darkColorScheme(
    primary = DarkPurple,
    secondary = DarkPurpleGrey,
    tertiary = DarkPink
)

/**
 * Light color scheme definition for the application.
 *
 * This color scheme is used when the app is in light theme mode and when
 * dynamic color is disabled or not available.
 */
private val LightColorScheme = lightColorScheme(
    secondary = LightPurpleGrey,
    tertiary = LightPink
)

/**
 * Main theme composable for the Task Application.
 *
 * This composable function configures and applies the application's theme settings,
 * including color schemes, typography, and system UI integration.
 *
 * Features:
 * - Support for both light and dark themes
 * - Dynamic color support for Android 12+ devices
 * - System UI harmonization with theme colors
 * - Material 3 design system implementation
 *
 * @param darkTheme Whether to use the dark theme. Defaults to false (light theme).
 * @param dynamicColor Whether to use dynamic colors on supported devices. Defaults to true.
 * @param content Composable content to which the theme will be applied.
 */
@Composable
fun TaskAppTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val view = LocalView.current

    // Determine the color scheme based on theme mode and dynamic color availability
    val colorScheme = when {
        dynamicColor -> {
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Apply status bar appearance adjustments when not in edit mode
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    // Apply Material theme with the determined color scheme
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}