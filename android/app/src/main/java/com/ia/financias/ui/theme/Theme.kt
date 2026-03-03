package com.ia.financias.ui.theme

import android.app.Activity
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = TealPrimary,
    secondary = TealVariant,
    background = BackgroundDark,
    surface = SurfaceDark,
    onPrimary = Color.White,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark
)

private val LightColorScheme = lightColorScheme(
    primary = TealPrimary,
    secondary = TealVariant,
    background = BackgroundLight,
    surface = SurfaceLight,
    onPrimary = Color.White,
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight
)

@Composable
fun FinanciasIATheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val context = view.context
            var currentContext = context
            while (currentContext is android.content.ContextWrapper) {
                if (currentContext is Activity) break
                currentContext = currentContext.baseContext
            }
            
            val window = (currentContext as? Activity)?.window
            window?.let {
                it.statusBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(it, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
