package com.example.artphotoframe.core.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.artphotoframe.R
import com.example.artphotoframe.core.data.models.Picture
import com.example.artphotoframe.core.domain.wallpaper.WallpaperTarget

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun PictureContent(
    picture: Picture,
    isFavorite: Boolean,
    onBack: () -> Unit,
    onAddFavorite: (Picture) -> Unit,
    onRemoveFavorite: (Picture) -> Unit,
    onUpdateFavorite: (Picture) -> Unit,
    onApplyWallpaper: (String, WallpaperTarget) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    // Держим «фактический» URL, который показали (с учётом fallback)
    var resolvedUrl by remember { mutableStateOf(picture.highQualityURL ?: picture.previewURL) }

    BottomSheetScaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        sheetPeekHeight = 28.dp,                          // почти закрытая шторка
        sheetDragHandle = { BottomSheetDefaults.DragHandle() },
        sheetContainerColor = MaterialTheme.colorScheme.surface,
        sheetContent = {
            // Контент шторки (заголовок/описание)
            BottomSheetInfo(
                title = picture.title,
                description = picture.description
            )
        }
    ) { innerPadding ->
        // Главный слой экрана: фото + оверлеи
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Слой с картинкой (загрузка, fallback, zoom)
            ImageLayer(
                hqUrl = picture.highQualityURL,
                previewUrl = picture.previewURL,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.media),
                error = painterResource(R.drawable.media),
                modifier = Modifier.fillMaxSize(),
                onUrlResolved = { resolvedUrl = it } // сообщаем наружу, какой URL реально показан
            )

            // Верхняя панель: ← Назад + ❤ Избранное
            TopBar(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp),
                picture = picture,
                isFavorite = isFavorite,
                onBack = onBack,
                onAddFavorite = onAddFavorite,
                onRemoveFavorite = onRemoveFavorite,
                onUpdateFavorite = onUpdateFavorite
            )

            // ⋮ Меню (справа сверху): установка обоев
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
            ) {
                FavoriteItemMenu { target ->
                    val url = resolvedUrl
                    if (!url.isNullOrBlank()) {
                        onApplyWallpaper(url, target)
                    }
                }
            }
        }
    }
}