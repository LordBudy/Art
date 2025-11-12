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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import coil.compose.AsyncImage
// для зума/жестов:
@Composable
fun ZoomableAsyncImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    minScale: Float = 1f,
    maxScale: Float = 5f,
    panSpeed: Float = 1.4f,
    contentScale: ContentScale = ContentScale.Crop,
    placeholder: Painter? = null,
    error: Painter? = null
) {
    // Текущие значения зума/позиции
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var containerSize by remember { mutableStateOf(IntSize.Zero) }

    // Функция ограничения смещения: не даём уехать за края.
    fun clampOffset(raw: Offset, s: Float): Offset {
        // если масштаб 1 — центрируем изображение, смещения быть не должно
        if (s <= 1f || containerSize.width == 0 || containerSize.height == 0) {
            return Offset.Zero
        }
        val w = containerSize.width.toFloat()
        val h = containerSize.height.toFloat()

        // На сколько «вырастает» изображение при текущем масштабировании:
        // при scale = 2f ширина визуально становится w*2, «запас» по каждой стороне: (w*(s-1))/2
        val maxX = (w * (s - 1f)) / 2f
        val maxY = (h * (s - 1f)) / 2f

        return Offset(
            x = raw.x.coerceIn(-maxX, +maxX),
            y = raw.y.coerceIn(-maxY, +maxY)
        )
    }

    // Состояние жестов масштабирования/перетаскивания
    val transformState = rememberTransformableState { zoomChange, panChange, _ ->
        //  считаем новый масштаб с ограничениями
        val newScale = (scale * zoomChange).coerceIn(minScale, maxScale)

        val dynamicBoost = panSpeed * newScale
        val acceleratedPan = panChange * dynamicBoost

        //  панорамирование разрешаем только когда картинка увеличена (> 1f)
        val pan = if (newScale > 1f) acceleratedPan else Offset.Zero

        //  применяем пан и сразу же КЛАМПИМ по новым границам
        val newOffset = clampOffset(offset + pan, newScale)

        scale = newScale
        offset = newOffset
    }

    // Двойной тап: быстрый zoom in/сброс.
    // (Для простоты зуммируем относительно центра; чтобы зуммировать в точку тапа —
    // нужно смещать offset с учётом координаты тапа и масштаба.)
    val doubleTapZoom = 2f
    val onDoubleTap: (Offset) -> Unit = {
        if (scale > 1f) {
            // полный сброс
            scale = 1f
            offset = Offset.Zero
        } else {
            // быстрый zoom in и нулевой оффсет
            scale = doubleTapZoom
            offset = clampOffset(offset, scale)
        }
    }

    AsyncImage(
        model = model,
        contentDescription = contentDescription,
        contentScale = contentScale,
        placeholder = placeholder,
        error = error,
        modifier = modifier
            // узнаём размер контейнера (для вычисления границ)
            .onSizeChanged { containerSize = it }
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
                detectTapGestures(onDoubleTap = onDoubleTap)
            }
    )
}