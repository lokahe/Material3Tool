package com.lokahe.material3

import android.annotation.SuppressLint
import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.android.material.color.utilities.DynamicScheme
import com.google.android.material.color.utilities.Hct
import com.google.android.material.color.utilities.SchemeTonalSpot
import com.google.android.material.color.utilities.TonalPalette
import com.google.android.material.color.utilities.Variant
import com.lokahe.material3.ui.theme.ColorSeed

class Utils {
    companion object {
        @SuppressLint("RestrictedApi")
        fun createColorScheme(seedColor: ColorSeed, dark: Boolean): ColorScheme {
            val hct = Hct.fromInt(/*if (dark) seedColor.seedDark else */seedColor.seed)

            // Build tonal palettes based on Material 3 defaults
//            val primary = TonalPalette.fromHueAndChroma(hct.hue, 24.0)
//            val secondary = TonalPalette.fromHueAndChroma(hct.hue, 16.0)
//            val tertiary = TonalPalette.fromHueAndChroma(hct.hue + 60.0, 24.0)
//            val neutral = TonalPalette.fromHueAndChroma(hct.hue, 4.0)
//            val neutralVariant = TonalPalette.fromHueAndChroma(hct.hue, 8.0)
            val primary = TonalPalette.fromHueAndChroma(hct.hue, hct.chroma)
            val secondary = TonalPalette.fromHueAndChroma(hct.hue, hct.chroma * 0.66)
            val tertiary = TonalPalette.fromHueAndChroma(hct.hue + 60.0, hct.chroma)
            val neutral = TonalPalette.fromHueAndChroma(hct.hue, hct.chroma / 6)
            val neutralVariant = TonalPalette.fromHueAndChroma(hct.hue, hct.chroma / 3)

            return DynamicScheme(
                hct, Variant.TONAL_SPOT, // same as Material You
                dark, 0.0, primary, secondary,
                tertiary, neutral, neutralVariant
            ).toColorScheme()
        }

        fun createColorSchemeFromSeed(seedColor: Color, isDark: Boolean): ColorScheme {
            // 1. ComposeのColorをARGB形式(Int)に変換し、HCTに変換
            val hct = Hct.fromInt(seedColor.toArgb())

            // 2. DynamicSchemeの生成 (OSの「壁紙から生成」と同じロジック)
            val dynamicScheme = SchemeTonalSpot(
                hct,          // シードカラー
                isDark,       // ダークモードかどうか
                0.0           // コントラストレベル (-1.0 ～ 1.0)
            )

            // 3. Compose の ColorScheme にマッピング
            /**
             *     val primary: Color,
             *     val onPrimary: Color,
             *     val primaryContainer: Color,
             *     val onPrimaryContainer: Color,
             *     val inversePrimary: Color,
             *     val secondary: Color,
             *     val onSecondary: Color,
             *     val secondaryContainer: Color,
             *     val onSecondaryContainer: Color,
             *     val tertiary: Color,
             *     val onTertiary: Color,
             *     val tertiaryContainer: Color,
             *     val onTertiaryContainer: Color,
             *     val background: Color,
             *     val onBackground: Color,
             *     val surface: Color,
             *     val onSurface: Color,
             *     val surfaceVariant: Color,
             *     val onSurfaceVariant: Color,
             *     val surfaceTint: Color,
             *     val inverseSurface: Color,
             *     val inverseOnSurface: Color,
             *     val error: Color,
             *     val onError: Color,
             *     val errorContainer: Color,
             *     val onErrorContainer: Color,
             *     val outline: Color,
             *     val outlineVariant: Color,
             *     val scrim: Color,
             *     val surfaceBright: Color,
             *     val surfaceDim: Color,
             *     val surfaceContainer: Color,
             *     val surfaceContainerHigh: Color,
             *     val surfaceContainerHighest: Color,
             *     val surfaceContainerLow: Color,
             *     val surfaceContainerLowest: Color,
             */
            return ColorScheme(
                primary = Color(dynamicScheme.primary),
                onPrimary = Color(dynamicScheme.onPrimary),
                primaryContainer = Color(dynamicScheme.primaryContainer),
                onPrimaryContainer = Color(dynamicScheme.onPrimaryContainer),
                inversePrimary = Color(dynamicScheme.inversePrimary),
                secondary = Color(dynamicScheme.secondary),
                onSecondary = Color(dynamicScheme.onSecondary),
                secondaryContainer = Color(dynamicScheme.secondaryContainer),
                onSecondaryContainer = Color(dynamicScheme.onSecondaryContainer),
                tertiary = Color(dynamicScheme.tertiary),
                onTertiary = Color(dynamicScheme.onTertiary),
                tertiaryContainer = Color(dynamicScheme.tertiaryContainer),
                onTertiaryContainer = Color(dynamicScheme.onTertiaryContainer),
                background = Color(dynamicScheme.background),
                onBackground = Color(dynamicScheme.onBackground),
                surface = Color(dynamicScheme.surface),
                onSurface = Color(dynamicScheme.onSurface),
                surfaceVariant = Color(dynamicScheme.surfaceVariant),
                onSurfaceVariant = Color(dynamicScheme.onSurfaceVariant),
                surfaceTint = Color(dynamicScheme.surfaceTint),
                inverseSurface = Color(dynamicScheme.inverseSurface),
                inverseOnSurface = Color(dynamicScheme.inverseOnSurface),
                error = Color(dynamicScheme.error),
                onError = Color(dynamicScheme.onError),
                errorContainer = Color(dynamicScheme.errorContainer),
                onErrorContainer = Color(dynamicScheme.onErrorContainer),
                outline = Color(dynamicScheme.outline),
                outlineVariant = Color(dynamicScheme.outlineVariant),
                scrim = Color(dynamicScheme.scrim),
                surfaceBright = Color(dynamicScheme.surfaceBright),
                surfaceDim = Color(dynamicScheme.surfaceDim),
                surfaceContainer = Color(dynamicScheme.surfaceContainer),
                surfaceContainerHigh = Color(dynamicScheme.surfaceContainerHigh),
                surfaceContainerHighest = Color(dynamicScheme.surfaceContainerHighest),
                surfaceContainerLow = Color(dynamicScheme.surfaceContainerLow),
                surfaceContainerLowest = Color(dynamicScheme.surfaceContainerLowest)
                // ... 他のすべての色（secondary, tertiary, error, surface等）を同様にマッピング
                // ※ 非常に多くのプロパティがあるため、ヘルパー関数を作成するのが一般的です
            )
        }
    }
}


@SuppressLint("RestrictedApi")
fun DynamicScheme.toColorScheme(): ColorScheme {
    return ColorScheme(
        primary = Color(primary),
        onPrimary = Color(onPrimary),
        primaryContainer = Color(primaryContainer),
        onPrimaryContainer = Color(onPrimaryContainer),
        inversePrimary = Color(inversePrimary),
        secondary = Color(secondary),
        onSecondary = Color(onSecondary),
        secondaryContainer = Color(secondaryContainer),
        onSecondaryContainer = Color(onSecondaryContainer),
        tertiary = Color(tertiary),
        onTertiary = Color(onTertiary),
        tertiaryContainer = Color(tertiaryContainer),
        onTertiaryContainer = Color(onTertiaryContainer),
        background = Color(background),
        onBackground = Color(onBackground),
        surface = Color(surface),
        onSurface = Color(onSurface),
        surfaceVariant = Color(surfaceVariant),
        onSurfaceVariant = Color(onSurfaceVariant),
        surfaceTint = Color(primary),
        inverseSurface = Color(inverseSurface),
        inverseOnSurface = Color(inverseOnSurface),
        error = Color(error),
        onError = Color(onError),
        errorContainer = Color(errorContainer),
        onErrorContainer = Color(onErrorContainer),
        outline = Color(outline),
        outlineVariant = Color(outlineVariant),
        scrim = Color(scrim),
        // Add default values for new M3 colors if needed, but the above covers the core
        surfaceBright = Color(surfaceBright),
        surfaceDim = Color(surfaceDim),
        surfaceContainer = Color(surfaceContainer),
        surfaceContainerHigh = Color(surfaceContainerHigh),
        surfaceContainerHighest = Color(surfaceContainerHighest),
        surfaceContainerLow = Color(surfaceContainerLow),
        surfaceContainerLowest = Color(surfaceContainerLowest),
    )
}

/** Pack ARGB components (0–255 each) into a single Int. */
fun argbToInt(a: Int, r: Int, g: Int, b: Int): Int =
    (a and 0xFF shl 24) or (r and 0xFF shl 16) or (g and 0xFF shl 8) or (b and 0xFF)

/** Unpack an ARGB Int into a Compose Color. */
fun Int.toComposeColor(): Color = Color(this.toLong() and 0xFFFFFFFFL)

/** Format a Compose Color as #AARRGGBB string. */
fun Color.toHexString(): String {
    val argb = this.toArgb()
    return "#%02X%02X%02X%02X".format(
        (argb shr 24) and 0xFF,
        (argb shr 16) and 0xFF,
        (argb shr 8) and 0xFF,
        argb and 0xFF
    )
}

/** Try to parse a #AARRGGBB or #RRGGBB string, returning null on failure. */
fun parseHexColor(hex: String): Color? = runCatching {
    val clean = hex.trimStart('#')
    when (clean.length) {
        6 -> Color(("FF$clean").toLong(16) or 0xFF000000L)
        8 -> Color(clean.toLong(16))
        else -> null
    }
}.getOrNull()
