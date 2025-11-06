package com.example.artphotoframe.core.presentation.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
// для зума/жестов:
@Composable
fun ZoomableAsyncImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    minScale: Float = 1f,
    maxScale: Float = 5f,
    // остальные параметры пробрасываем в AsyncImage
    contentScale: ContentScale = ContentScale.Crop,
    placeholder: Painter? = null,
    error: Painter? = null
) {
    // Текущие значения зума/позиции
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    // Состояние жестов масштабирования/перетаскивания
    val transformState = rememberTransformableState { zoomChange, panChange, _ ->
        // 1) масштабируем с ограничениями
        val newScale = (scale * zoomChange).coerceIn(minScale, maxScale)

        // 2) если уже увеличили (>1f), разрешаем панорамирование
        // масштабируем смещение пропорционально текущему масштабу
        val allowedPan = if (newScale > 1f) panChange else Offset.Zero

        scale = newScale
        offset += allowedPan
    }

    // Двойной тап: быстрый zoom in/сброс
    val doubleTapZoom = 2f
    val onDoubleTap: (Offset) -> Unit = {
        if (scale > 1f) {
            // сброс
            scale = 1f
            offset = Offset.Zero
        } else {
            // быстрый zoom in
            scale = doubleTapZoom
        }
    }

    AsyncImage(
        model = model,
        contentDescription = contentDescription,
        contentScale = contentScale,
        placeholder = placeholder,
        error = error,
        modifier = modifier
            // применяем трансформации к изображению
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offset.x,
                translationY = offset.y
            )
            // жесты "щипок/перетаскивание"
            .transformable(transformState)
            // двойной тап для быстрого увеличения/сброса
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = onDoubleTap
                )
            }
    )
}