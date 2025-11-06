package com.example.artphotoframe.core.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.request.ImageRequest
import com.example.artphotoframe.R
import com.example.artphotoframe.core.presentation.ui.BackButton
import com.example.artphotoframe.core.presentation.ui.BtnFavorite
import com.example.artphotoframe.core.presentation.ui.FavoriteItemMenu
import com.example.artphotoframe.core.presentation.ui.ZoomableAsyncImage
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PictureScreen(
    pictureId: Int,
    navController: NavController
) {
    val viewModel: FullPicFavoriteViewModel = koinViewModel()
    val wallpaperVm: WallpaperViewModel = koinViewModel()

    // Загружаем картинку при создании экрана
    LaunchedEffect(pictureId) { viewModel.loadPictureById(pictureId) }

    // Текущее изображение и статус избранного
    val picture = viewModel.currentPicture.collectAsStateWithLifecycle().value
    val isFavorite = viewModel.isFavorite.collectAsStateWithLifecycle().value

    // Сообщения от WallpaperViewModel
    val wallpaperUi = wallpaperVm.ui.collectAsStateWithLifecycle().value
    val snackbarHostState = remember { SnackbarHostState() }

    // Показываем сообщение
    LaunchedEffect(wallpaperUi.message) {
        val msg = wallpaperUi.message ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(msg)
        wallpaperVm.clearMessage()
    }

    // Если картинка не найдена
    if (picture == null) {
        Text("Картинка не найдена")
        return
    }
    // сперва пробуем загрузить HQ, если нет то  preview
    val hqUrl = picture.highQualityURL
    val previewUrl = picture.previewURL
    var currentUrl by remember(picture) { mutableStateOf(hqUrl ?: previewUrl) } // стартуем с HQ, если есть
    var triedFallback by remember(picture) { mutableStateOf(false) }            // чтобы не зациклиться

    // Показывать ли индикатор загрузки
    var isLoading by remember(currentUrl) { mutableStateOf(true) }

    // Настройка загрузки картинки
    val context = LocalContext.current
    val imageRequest = remember(currentUrl) {
        ImageRequest.Builder(context)
            .data(currentUrl)
            .crossfade(true)
            .allowHardware(false)
            .listener(
                // старт загрузки
                onStart = { isLoading = true },
                // успешно загрузили → прячем индикатор
                onSuccess = { _, _ -> isLoading = false },
                onError = { _, _ ->
                    // если HQ не загрузилось — используем превью
                    if (!triedFallback && hqUrl != null && previewUrl != null && currentUrl == hqUrl) {
                        triedFallback = true
                        currentUrl = previewUrl
                    } else {
                        // если фолбэка нет или он тоже упал — завершаем загрузку и покажем error-плейсхолдер
                        isLoading = false
                    }
                }
            )
            .build()
    }

    BottomSheetScaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        // нижняя шторка почти закрыта
        sheetPeekHeight = 28.dp,
        sheetDragHandle = {
            BottomSheetDefaults.DragHandle()
        },
        sheetContainerColor = MaterialTheme.colorScheme.surface,
        sheetContent = {
            // Текст в нижней шторке
            Column(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(bottom = 12.dp)
            ) {
                Text(
                    text = picture.title ?: "Нет заголовка",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
                Text(
                    text = picture.description ?: "Нет описания",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Полноэкранная картинка
            ZoomableAsyncImage(
                model = imageRequest,
                contentDescription = "Full screen image",
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.media),
                error = painterResource(R.drawable.media),
                modifier = Modifier.fillMaxSize()
            )
            //  Индикатор загрузки поверх картинки
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.05f)), // лёгкий полупрозрачный слой (по желанию)
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            //  Назад + Избранное
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButton(
                    onClick = { navController.popBackStack() },
                    color = MaterialTheme.colorScheme.onBackground
                )
                // Небольшой отступ между стрелкой и сердцем
                androidx.compose.foundation.layout.Spacer(Modifier.padding(horizontal = 8.dp))
                BtnFavorite(
                    picture = picture,
                    isFavorite = isFavorite,
                    onAddToFavorites = viewModel.onAddToFavorites,
                    onRemoveFromFavorites = viewModel.onRemoveFromFavorites,
                    onUpdateFavorites = viewModel.onUpdateFavorites
                )
            }

            // ⋮ Меню — установка обоев через WallpaperViewModel
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
            ) {
                FavoriteItemMenu { target ->
                    // берём актуальный URL: currentUrl (уже с возможным фолбэком)
                    currentUrl?.let { url ->
                        if (url.isNotBlank()) {
                            wallpaperVm.apply(url, target) // UseCase → Repository
                        }
                    }
                }
            }
        }
    }
}