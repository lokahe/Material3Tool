package com.lokahe.material3.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.lokahe.material3.models.HsvColor
import com.lokahe.material3.parseHexColor
import com.lokahe.material3.toHexString


@Composable
fun ColorPickerDialog(
    initialColor: Color = Color.Red,
    onDismiss: () -> Unit,
    onReset: () -> Unit = {},
    onColorSelected: (Color) -> Unit
) {
    var hsv by remember { mutableStateOf(HsvColor.fromColor(initialColor)) }
    var hexInput by remember { mutableStateOf(initialColor.toHexString()) }
    var hexError by remember { mutableStateOf(false) }

    val currentColor by remember(hsv) { derivedStateOf { hsv.toColor() } }

    // Keep hex field in sync when sliders change
    LaunchedEffect(hsv) {
        hexInput = currentColor.toHexString()
        hexError = false
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title
                Text(
                    text = "Color Picker (different with the Colours of Wallpaper and style)",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )

                // SV panel
                SatValPanel(
                    hue = hsv.hue,
                    sat = hsv.sat,
                    value = hsv.value,
                    onSatValChanged = { s, v -> hsv = hsv.copy(sat = s, value = v) }
                )

                // Hue slider
                Text(
                    "Hue",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                HueSlider(hue = hsv.hue, onHueChanged = { hsv = hsv.copy(hue = it) })

                // Alpha slider
                Text(
                    "Opacity",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                AlphaSlider(
                    color = currentColor,
                    alpha = hsv.alpha,
                    onAlphaChanged = { hsv = hsv.copy(alpha = it) }
                )

                // Preview + Hex input row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Color preview with checkerboard bg
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline,
                                RoundedCornerShape(8.dp)
                            )
                    ) {
                        Canvas(Modifier.fillMaxSize()) {
                            val tileSize = 8.dp.toPx()
                            val cols = (size.width / tileSize).toInt() + 1
                            val rows = (size.height / tileSize).toInt() + 1
                            for (row in 0..rows) {
                                for (col in 0..cols) {
                                    drawRect(
                                        color = if ((row + col) % 2 == 0) Color(0xFFCCCCCC) else Color.White,
                                        topLeft = Offset(col * tileSize, row * tileSize),
                                        size = androidx.compose.ui.geometry.Size(tileSize, tileSize)
                                    )
                                }
                            }
                            drawRect(color = currentColor)
                        }
                    }

                    // #AARRGGBB text field
                    Column(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = hexInput,
                            onValueChange = { raw ->
                                hexInput = raw
                                val parsed = parseHexColor(raw)
                                if (parsed != null) {
                                    hsv = HsvColor.fromColor(parsed)
                                    hexError = false
                                } else {
                                    hexError = raw.isNotEmpty()
                                }
                            },
                            label = { Text("#AARRGGBB") },
                            isError = hexError,
                            singleLine = true,
                            textStyle = TextStyle(fontFamily = FontFamily.Monospace),
                            supportingText = if (hexError) {
                                { Text("Invalid format. Use #AARRGGBB or #RRGGBB") }
                            } else null,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(Modifier.width(8.dp))
                    TextButton(onClick = onReset) { Text("Reset") }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = { onColorSelected(currentColor) },
                        colors = ButtonDefaults.buttonColors(containerColor = currentColor)
                    ) {
                        Text(
                            "Select",
                            color = if (currentColor.luminance() > 0.4f) Color.Black else Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SatValPanel(
    hue: Float,
    sat: Float,
    value: Float,
    onSatValChanged: (Float, Float) -> Unit
) {
    var size by remember { mutableStateOf(IntSize.Zero) }

    val hueColor = HsvColor(hue, 1f, 1f).toColor()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(8.dp))
            .onSizeChanged { size = it }
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    change.consume()
                    if (size.width > 0 && size.height > 0) {
                        val s = (change.position.x / size.width).coerceIn(0f, 1f)
                        val v = (1f - change.position.y / size.height).coerceIn(0f, 1f)
                        onSatValChanged(s, v)
                    }
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // White → HueColor horizontal gradient
            drawRect(
                brush = Brush.horizontalGradient(listOf(Color.White, hueColor))
            )
            // Transparent → Black vertical gradient
            drawRect(
                brush = Brush.verticalGradient(listOf(Color.Transparent, Color.Black))
            )
        }
        // Thumb
        val thumbX = (sat * size.width).dp
        val thumbY = ((1f - value) * size.height).dp
        Box(
            modifier = Modifier
                .offset(
                    x = with(LocalDensity.current) { (sat * size.width).toDp() } - 8.dp,
                    y = with(LocalDensity.current) { ((1f - value) * size.height).toDp() } - 8.dp
                )
                .size(16.dp)
                .clip(CircleShape)
                .border(2.dp, Color.White, CircleShape)
                .background(HsvColor(hue, sat, value).toColor(), CircleShape)
        )
    }
}

@Composable
fun HueSlider(hue: Float, onHueChanged: (Float) -> Unit) {
    var width by remember { mutableStateOf(0) }
    val hueColors = remember {
        listOf(
            Color(0xFFFF0000), Color(0xFFFFFF00), Color(0xFF00FF00),
            Color(0xFF00FFFF), Color(0xFF0000FF), Color(0xFFFF00FF), Color(0xFFFF0000)
        )
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
            .clip(RoundedCornerShape(12.dp))
            .onSizeChanged { width = it.width }
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    change.consume()
                    if (width > 0) {
                        val h = (change.position.x / width * 360f).coerceIn(0f, 360f)
                        onHueChanged(h)
                    }
                }
            }
    ) {
        Canvas(Modifier.fillMaxSize()) {
            drawRect(brush = Brush.horizontalGradient(hueColors))
        }
        // Thumb
        Box(
            modifier = Modifier
                .offset(
                    x = with(LocalDensity.current) { (hue / 360f * width).toDp() } - 12.dp,
                    y = 0.dp
                )
                .size(24.dp)
                .clip(CircleShape)
                .border(2.dp, Color.White, CircleShape)
                .background(HsvColor(hue, 1f, 1f).toColor(), CircleShape)
        )
    }
}

@Composable
fun AlphaSlider(color: Color, alpha: Float, onAlphaChanged: (Float) -> Unit) {
    var width by remember { mutableStateOf(0) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
            .clip(RoundedCornerShape(12.dp))
            .onSizeChanged { width = it.width }
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    change.consume()
                    if (width > 0) {
                        val a = (change.position.x / width).coerceIn(0f, 1f)
                        onAlphaChanged(a)
                    }
                }
            }
    ) {
        // Checkerboard pattern for transparency indication
        Canvas(Modifier.fillMaxSize()) {
            val tileSize = 12.dp.toPx()
            val cols = (size.width / tileSize).toInt() + 1
            val rows = (size.height / tileSize).toInt() + 1
            for (row in 0..rows) {
                for (col in 0..cols) {
                    drawRect(
                        color = if ((row + col) % 2 == 0) Color.LightGray else Color.White,
                        topLeft = Offset(col * tileSize, row * tileSize),
                        size = androidx.compose.ui.geometry.Size(tileSize, tileSize)
                    )
                }
            }
            drawRect(
                brush = Brush.horizontalGradient(
                    listOf(color.copy(alpha = 0f), color.copy(alpha = 1f))
                )
            )
        }
        // Thumb
        Box(
            modifier = Modifier
                .offset(
                    x = with(LocalDensity.current) { (alpha * width).toDp() } - 12.dp,
                    y = 0.dp
                )
                .size(24.dp)
                .clip(CircleShape)
                .border(2.dp, Color.White, CircleShape)
                .background(color.copy(alpha = alpha), CircleShape)
        )
    }
}