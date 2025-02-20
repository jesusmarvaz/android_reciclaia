package com.ingencode.reciclaia.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun MyComposeWrapper(content: @Composable () -> Unit) {
    ReciclaIaTheme(content = content)
}

@Composable
fun ReciclaIaTheme(darkTheme: Boolean = isSystemInDarkTheme(),
                   dynamicColor: Boolean = true,
                   content: @Composable () -> Unit) {
    val context = LocalContext.current
    val defTyp = MaterialTheme.typography

    val myTypography = Typography(
        displayLarge = defTyp.displayLarge.copy(fontFamily = ComfortaaFontFamily),
        displayMedium = defTyp.displayMedium.copy(fontFamily = ComfortaaFontFamily),
        displaySmall = defTyp.displaySmall.copy(fontFamily = ComfortaaFontFamily),
        headlineLarge = defTyp.headlineLarge.copy(fontFamily = ComfortaaFontFamily),
        headlineMedium = defTyp.headlineMedium.copy(fontFamily = ComfortaaFontFamily),
        headlineSmall = defTyp.headlineSmall.copy(fontFamily = ComfortaaFontFamily),
        bodyLarge = defTyp.bodyLarge.copy(fontFamily = ComfortaaFontFamily),
        bodyMedium = defTyp.bodyMedium.copy(fontFamily = ComfortaaFontFamily),
        bodySmall = defTyp.bodySmall.copy(fontFamily = ComfortaaFontFamily),
        titleLarge = defTyp.titleLarge.copy(fontFamily = ComfortaaFontFamily, fontWeight = FontWeight.Bold),
        titleMedium = defTyp.titleMedium.copy(fontFamily = ComfortaaFontFamily, fontWeight = FontWeight.Bold),
        titleSmall = defTyp.titleSmall.copy(fontFamily = ComfortaaFontFamily, fontWeight = FontWeight.Bold),
        labelLarge = defTyp.labelLarge.copy(fontFamily = ComfortaaFontFamily, fontWeight = FontWeight.Bold),
        labelMedium = defTyp.labelMedium.copy(fontFamily = ComfortaaFontFamily, fontWeight = FontWeight.Bold),
        labelSmall = defTyp.labelSmall.copy(fontFamily = ComfortaaFontFamily, fontWeight = FontWeight.Bold))

    val colorScheme = when {
        dynamicColor -> if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(
            context
        )

        darkTheme -> DarkColors
        else -> LightColors
    }
    MaterialTheme(colorScheme = colorScheme, content = content, typography = myTypography)
}
