package com.example.artphotoframe.core.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.artphotoframe.R
import com.example.artphotoframe.core.domain.wallpaper.WallpaperTarget
import com.example.artphotoframe.core.presentation.ui.BackButton
import com.example.artphotoframe.core.presentation.ui.BtnFavorite
import com.example.artphotoframe.core.presentation.ui.FavoriteItemMenu
import com.example.artphotoframe.core.presentation.ui.FullPictureInfo
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

    // Собираем состояние текущей картинки
    val picture = viewModel.currentPicture.collectAsStateWithLifecycle().value
    val isFavorite = viewModel.isFavorite.collectAsStateWithLifecycle().value
    val wallpaperUi = wallpaperVm.ui.collectAsStateWithLifecycle().value

    // Локальный снекбар для сообщений из WallpaperViewModel
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(wallpaperUi.message) {
        val msg = wallpaperUi.message ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(msg)
        wallpaperVm.clearMessage()
    }

    // Если картинка не нашлась — коротко выходим
    if (picture == null) {
        Text("Картинка не найдена")
        return
    }
    // --- Fallback: сначала пытаемся HQ, если упало — переключаемся на preview ---
    val hqUrl = picture.highQualityURL
    val previewUrl = picture.previewURL
    var currentUrl by remember(picture) { mutableStateOf(hqUrl ?: previewUrl) } // стартуем с HQ, если есть
    var triedFallback by remember(picture) { mutableStateOf(false) }            // чтобы не зациклиться

    val context = LocalContext.current
    // Собираем ImageRequest вручную, чтобы перехватить onError и переключиться на preview
    val imageRequest = remember(currentUrl) {
        ImageRequest.Builder(context)
            .data(currentUrl)
            .crossfade(true)
            .allowHardware(false) // безопасно для получения Bitmap при необходимости
            .listener(
                onError = { _, _ ->
                    // если не пробовали фолбэк и есть preview — переключаемся на него
                    if (!triedFallback && hqUrl != null && previewUrl != null && currentUrl == hqUrl) {
                        triedFallback = true
                        currentUrl = previewUrl
                    }
                }
            )
            .build()
    }

    BottomSheetScaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        sheetPeekHeight = 28.dp,
        sheetDragHandle = {
            BottomSheetDefaults.DragHandle() // стандартный M3-элемент
        },

        sheetContainerColor = MaterialTheme.colorScheme.surface,

        // В sheetContent лежит уже сам текст: он появится только когда шторку потянут
        sheetContent = {
            Column(
                modifier = Modifier
                    .navigationBarsPadding() // чтобы не налезала на системные кнопки
                    .padding(bottom = 12.dp) // чуть отступа, чтобы текст не упирался
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
        // ===== ГЛАВНЫЙ СЛОЙ: фото на весь экран + оверлеи =====
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Полноэкранная картинка с фолбэком HQ → preview
            AsyncImage(
                model = imageRequest,
                contentDescription = "Full screen image",
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.media),
                error = painterResource(R.drawable.media),
                modifier = Modifier.fillMaxSize()
            )

            // ← Назад + ❤ Избранное (левый верх)
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

            // ⋮ Меню (правый верх) — установка обоев через WallpaperViewModel
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