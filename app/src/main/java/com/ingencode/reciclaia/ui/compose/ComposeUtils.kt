package com.ingencode.reciclaia.ui.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.ingencode.reciclaia.R
import com.ingencode.reciclaia.ui.theme.Pink40
import com.ingencode.reciclaia.ui.theme.Pink80
import com.ingencode.reciclaia.ui.theme.Purple40
import com.ingencode.reciclaia.ui.theme.Purple80
import com.ingencode.reciclaia.ui.theme.PurpleGrey40
import com.ingencode.reciclaia.ui.theme.PurpleGrey80

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-02-10.
 */

private val LightColors = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40)

private val DarkColors = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80)

val ComfortaaFontFamily = FontFamily(
    Font(resId = R.font.comfortaa_regular, weight = FontWeight.Normal),
    Font(resId = R.font.comfortaa_bold, weight = FontWeight.Bold))

val monospaceFontFamily = FontFamily(
    Font(R.font.ubuntumono_regular, FontWeight.Normal),
    Font(R.font.ubuntumono_bold, FontWeight.Bold),
    Font(R.font.ubuntumono_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.ubuntumono_bolditalic, FontWeight.Bold, FontStyle.Italic))

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
    val colorScheme = when {
        dynamicColor -> if (darkTheme) dynamicLightColorScheme(context) else dynamicDarkColorScheme(context)
        darkTheme -> DarkColors
        else -> LightColors
    }

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

    MaterialTheme(colorScheme = colorScheme, content = content, typography = myTypography)
}