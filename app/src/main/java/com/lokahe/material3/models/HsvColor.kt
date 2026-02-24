package com.lokahe.material3.models

import androidx.compose.ui.graphics.Color
import kotlin.math.roundToInt

data class HsvColor(val hue: Float, val sat: Float, val value: Float, val alpha: Float = 1f) {
    fun toColor(): Color {
        val hsv = floatArrayOf(hue, sat, value)
        val rgb = android.graphics.Color.HSVToColor(hsv)
        return Color(
            red = android.graphics.Color.red(rgb) / 255f,
            green = android.graphics.Color.green(rgb) / 255f,
            blue = android.graphics.Color.blue(rgb) / 255f,
            alpha = alpha
        )
    }

    companion object {
        fun fromColor(color: Color): HsvColor {
            val hsv = FloatArray(3)
            android.graphics.Color.colorToHSV(
                android.graphics.Color.argb(
                    (color.alpha * 255).roundToInt(),
                    (color.red * 255).roundToInt(),
                    (color.green * 255).roundToInt(),
                    (color.blue * 255).roundToInt()
                ), hsv
            )
            return HsvColor(hsv[0], hsv[1], hsv[2], color.alpha)
        }
    }
}