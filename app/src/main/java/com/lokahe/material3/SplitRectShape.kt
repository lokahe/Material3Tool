package com.lokahe.material3

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

// Custom Shape to clip the Right side of the screen
class SplitRectShape(private val splitRatio: Float) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rectangle(
            Rect(left = size.width * splitRatio, top = 0f, right = size.width, bottom = size.height)
        )
    }
}