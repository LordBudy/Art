package com.example.artphotoframe.core.presentation.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.artphotoframe.core.presentation.ui.FavoriteItemMenu
import com.example.artphotoframe.core.presentation.ui.FullPictureFavorite
import com.example.artphotoframe.core.presentation.ui.HomeButton
import org.koin.androidx.compose.koinViewModel


@Composable
fun FavoriteScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    // ViewModel избранного
    val viewModel: FullPicFavoriteViewModel = koinViewModel()

    // ViewModel для установки обоев
    val wallpaperViewModel: WallpaperViewModel = koinViewModel()

    // Список избранных картинок
    val pictures by viewModel.favorites.collectAsStateWithLifecycle(
        emptyList()
    )
    // UI состояние установки обоев (для снекбара)
    val wallpaperUi by wallpaperViewModel.ui.collectAsStateWithLifecycle()

    // Фильтруем дубликаты по ID если есть такие то удаляются
    val uniquePictures = pictures.distinctBy { it.id }

    // Логируем для проверки
    //запускается каждый раз, когда uniquePictures меняется
    LaunchedEffect(uniquePictures) {
        Log.d("FavoriteScreen", "Pictures IDs: ${uniquePictures.map { it.id }}")
        // Проверяет, были ли дубликаты.
        if (uniquePictures.size < pictures.size) {
            Log.w("FavoriteScreen", "Duplicates removed: ${pictures.size - uniquePictures.size}")
        }
    }

    // Показ сообщения после установки обоев
    LaunchedEffect(wallpaperUi.message) {
        val msg = wallpaperUi.message ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(message = msg)
        wallpaperViewModel.clearMessage()
    }

    // Основная колонка
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
        //автоматически добавляет(padding) под системные панели

    ) {
        // Если нет избранных — выводим текст
        if (pictures.isEmpty()) {
            // Обработка пустого списка
            Text(
                text = "Нет избранных картинок 😔",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            // Отображение результатов поиска список избранных
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = uniquePictures,
                    key = { it.id }
                ) { picture ->
                    // Элемент избранного
                    FullPictureFavorite(
                        picture = picture,
                        onImageClick = {
                            // Переход на полный просмотр
                            navController.navigate("picture_screen/${picture.id}")
                                       },
                        isFavorite = true,
                        onAddToFavorites = {},
                        onRemoveFromFavorites = viewModel.onRemoveFromFavorites,
                        onUpdateFavorites = viewModel.onUpdateFavorites,
                        menu = {
                            // Меню установки обоев
                            FavoriteItemMenu { target ->
                                // Берём полноразмерное изображение, если нет то превью
                                val dataForWallpaper =
                                    picture.highQualityURL ?: picture.previewURL ?: ""

                                // Если строка не пустая — ставим обои
                                if (dataForWallpaper.isNotBlank()) {
                                    wallpaperViewModel.apply(dataForWallpaper, target)
                                } else {
                                    Log.w("FavoriteScreen", "Нет ссылки на изображение для ID=${picture.id}"
                                    )
                                }
                            }
                        }
                    )
                    HorizontalDivider()
                }
            }
        }
    }


}
