package com.lokahe.material3

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.lokahe.material3.ui.theme.Material3CheckerTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @SuppressLint("FrequentlyChangingValue")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var splitRatio by remember { mutableFloatStateOf(0.5f) }
            val listState = rememberLazyListState()
            val listState2 = rememberLazyListState()
            LaunchedEffect(
                listState.firstVisibleItemIndex,
                listState.firstVisibleItemScrollOffset
            ) {
                listState2.scrollToItem(
                    listState.firstVisibleItemIndex,
                    listState.firstVisibleItemScrollOffset
                )
            }
            val scope = rememberCoroutineScope()
            val velocityTracker = VelocityTracker()
            Material3CheckerTheme(darkTheme = false) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = innerPadding,
                        listState2, false
                    )
                }
            }
            Material3CheckerTheme(darkTheme = true) {
                Box(
                    modifier = Modifier.fillMaxSize()
                        // Clip this box so it only shows on the right side of the splitRatio
                        .pointerInput(Unit) {
                            val decay =
                                splineBasedDecay<Float>(this) // Standard Android fling curve
                            detectDragGestures(
                                onDragStart = { velocityTracker.resetTracking() },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    // Update ratio based on screen width
                                    splitRatio =
                                        (splitRatio + dragAmount.x / size.width).coerceIn(0f, 1f)
                                    // Track velocity during drag
                                    velocityTracker.addPosition(
                                        change.uptimeMillis,
                                        change.position
                                    )

                                    scope.launch {
                                        listState.scrollBy(-dragAmount.y)
                                    }
                                },
                                onDragEnd = {
                                    val velocity = velocityTracker.calculateVelocity().y
                                    scope.launch {
                                        // Use Animatable to avoid manual casting errors
                                        val animatable = Animatable(0f)
                                        listState.scroll {
                                            var lastValue = 0f
                                            animatable.animateDecay(velocity, decay) {
                                                val delta = value - lastValue
                                                scrollBy(-delta)
                                                lastValue = value
                                            }
                                        }
                                    }
                                }
                            )
                        }
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize()
                            .clip(SplitRectShape(splitRatio))
                    ) { innerPadding ->
                        Greeting(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = innerPadding,
                            listState, false
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    listState: LazyListState = rememberLazyListState(),
    userScrollEnabled: Boolean = true
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    val colorSchemeStr = "MaterialTheme.colorScheme"
    val typographyStr = "MaterialTheme.typography"
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        state = listState,
        userScrollEnabled = userScrollEnabled
    ) {
        text("$colorSchemeStr.background", color = colorScheme.background)
        text("$colorSchemeStr.onBackground", color = colorScheme.onBackground)
        text("$colorSchemeStr.error", color = colorScheme.error)
        text("$colorSchemeStr.onError", color = colorScheme.onError)
        text("$colorSchemeStr.errorContainer", color = colorScheme.errorContainer)
        text("$colorSchemeStr.onErrorContainer", color = colorScheme.onErrorContainer)
        text("$colorSchemeStr.inversePrimary", color = colorScheme.inversePrimary)
        text("$colorSchemeStr.inverseSurface", color = colorScheme.inverseSurface)
        text("$colorSchemeStr.inverseOnSurface", color = colorScheme.inverseOnSurface)
        text("$colorSchemeStr.primary", color = colorScheme.primary)
        text("$colorSchemeStr.onPrimary", color = colorScheme.onPrimary)
        text("$colorSchemeStr.primaryContainer", color = colorScheme.primaryContainer)
        text("$colorSchemeStr.onPrimaryContainer", color = colorScheme.onPrimaryContainer)
        text("$colorSchemeStr.scrim", color = colorScheme.scrim)
        text("$colorSchemeStr.secondary", color = colorScheme.secondary)
        text("$colorSchemeStr.onSecondary", color = colorScheme.onSecondary)
        text("$colorSchemeStr.secondaryContainer", color = colorScheme.secondaryContainer)
        text("$colorSchemeStr.onSecondaryContainer", color = colorScheme.onSecondaryContainer)
        text("$colorSchemeStr.surface", color = colorScheme.surface)
        text("$colorSchemeStr.onSurface", color = colorScheme.onSurface)
        text("$colorSchemeStr.surfaceBright", color = colorScheme.surfaceBright)
        text("$colorSchemeStr.surfaceContainer", color = colorScheme.surfaceContainer)
        text("$colorSchemeStr.surfaceContainerHigh", color = colorScheme.surfaceContainerHigh)
        text("$colorSchemeStr.surfaceContainerHighest", color = colorScheme.surfaceContainerHighest)
        text("$colorSchemeStr.surfaceContainerLow", color = colorScheme.surfaceContainerLow)
        text("$colorSchemeStr.surfaceContainerLowest", color = colorScheme.surfaceContainerLowest)
        text("$colorSchemeStr.surfaceDim", color = colorScheme.surfaceDim)
        text("$colorSchemeStr.surfaceTint", color = colorScheme.surfaceTint)
        text("$colorSchemeStr.surfaceVariant", color = colorScheme.surfaceVariant)
        text("$colorSchemeStr.onSurfaceVariant", color = colorScheme.onSurfaceVariant)
        text("$colorSchemeStr.tertiary", color = colorScheme.tertiary)
        text("$colorSchemeStr.onTertiary", color = colorScheme.onTertiary)
        text("$colorSchemeStr.tertiaryContainer", color = colorScheme.tertiaryContainer)
        text("$colorSchemeStr.onTertiaryContainer", color = colorScheme.onTertiaryContainer)
        text("$typographyStr.bodySmall", style = typography.bodySmall)
        text("$typographyStr.bodyMedium", style = typography.bodyMedium)
        text("$typographyStr.bodyLarge", style = typography.bodyLarge)
        text("$typographyStr.displaySmall", style = typography.displaySmall)
        text("$typographyStr.displayMedium", style = typography.displayMedium)
        text("$typographyStr.displayLarge", style = typography.displayLarge)
        text("$typographyStr.headlineSmall", style = typography.headlineSmall)
        text("$typographyStr.headlineMedium", style = typography.headlineMedium)
        text("$typographyStr.headlineLarge", style = typography.headlineLarge)
        text("$typographyStr.labelSmall", style = typography.labelSmall)
        text("$typographyStr.labelMedium", style = typography.labelMedium)
        text("$typographyStr.labelLarge", style = typography.labelLarge)
        text("$typographyStr.titleSmall", style = typography.titleSmall)
        text("$typographyStr.titleMedium", style = typography.titleMedium)
        text("$typographyStr.titleLarge", style = typography.titleLarge)
    }
}

fun LazyListScope.text(
    text: String,
    color: Color? = null,
    style: TextStyle = TextStyle.Default
) {
    item {
        Text(
            text,
            modifier = Modifier.fillMaxWidth()
                .background(color = color ?: MaterialTheme.colorScheme.background),
            color = if ((color
                    ?: MaterialTheme.colorScheme.background).luminance() > 0.5f
            ) Color.Black else Color.White,
            style = style
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Material3CheckerTheme {
        Greeting()
    }
}