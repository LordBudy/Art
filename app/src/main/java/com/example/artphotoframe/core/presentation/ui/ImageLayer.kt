package com.example.artphotoframe.core.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.request.ImageRequest

@Composable
fun ImageLayer(
    hqUrl: String?,
    previewUrl: String?,
    contentScale: ContentScale,
    placeholder: Painter,
    error: Painter,
    modifier: Modifier = Modifier,
    onUrlResolved: (String?) -> Unit
) {
    val context = LocalContext.current

    // Текущий URL: стартуем с HQ, если он есть, иначе — preview
    var currentUrl by remember(hqUrl, previewUrl) { mutableStateOf(hqUrl ?: previewUrl) }
    // Флаг «уже пробовали переключиться на превью»
    var triedFallback by remember(hqUrl, previewUrl) { mutableStateOf(false) }
    // Идёт ли загрузка (для индикатора)
    var isLoading by remember(currentUrl) { mutableStateOf(true) }

    // Сообщаем наружу актуальный URL (например, для установки обоев)
    LaunchedEffect(currentUrl) { onUrlResolved(currentUrl) }

    // Собираем запрос Coil вручную, чтобы отреагировать на onError → fallback
    val request = remember(currentUrl) {
        ImageRequest.Builder(context)
            .data(currentUrl)
            .crossfade(true)
            .allowHardware(false)     // безопасно, если потом нужен Bitmap
            .listener(
                onStart = { isLoading = true },
                onSuccess = { _, _ -> isLoading = false },
                onError = { _, _ ->
                    // Если не пробовали фолбэк и HQ действительно был — переключаемся на preview
                    if (!triedFallback && hqUrl != null && previewUrl != null && currentUrl == hqUrl) {
                        triedFallback = true
                        currentUrl = previewUrl
                    } else {
                        // Фолбэка нет или он тоже упал → убираем индикатор, покажется error-плейсхолдер
                        isLoading = false
                    }
                }
            )
            .build()
    }

    Box(modifier = modifier) {
        // Картинка с поддержкой zoom/pan/double-tap
        ZoomableAsyncImage(
            model = request,
            contentDescription = null,    // CD можно пробросить параметром, если нужно
            contentScale = contentScale,
            placeholder = placeholder,
            error = error,
            modifier = Modifier.fillMaxSize()
        )

        // Лёгкое затемнение + индикатор, пока идёт загрузка
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.05f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}