package com.example.artphotoframe.core.presentation.screens


import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.artphotoframe.R
import com.example.artphotoframe.core.presentation.screens.viewmodel.FullPicFavoriteViewModel
import com.example.artphotoframe.core.presentation.screens.viewmodel.WallpaperResult
import com.example.artphotoframe.core.presentation.screens.viewmodel.WallpaperViewModel
import com.example.artphotoframe.core.presentation.ui.PictureContent
import org.koin.androidx.compose.koinViewModel

@Composable
fun PictureScreen(
    pictureId: Int,
    navController: NavController
) {
    // Получаем VM через Koin
    val viewModel: FullPicFavoriteViewModel = koinViewModel()
    val wallpaperVm: WallpaperViewModel = koinViewModel()

    // Загружаем картинку при создании экрана
    LaunchedEffect(pictureId) { viewModel.loadPictureById(pictureId) }

    // Текущее изображение и флаг-статус избранного
    val picture = viewModel.currentPicture.collectAsStateWithLifecycle().value
    val isFavorite = viewModel.isFavorite.collectAsStateWithLifecycle().value

    // Сообщения от WallpaperViewModel
    val wallpaperUi = wallpaperVm.ui.collectAsStateWithLifecycle().value
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Показываем сообщение результат установки обоев
    LaunchedEffect(wallpaperUi.result) {
        val text = when (wallpaperUi.result) {
            WallpaperResult.SUCCESS -> context.getString(R.string.wallpaper_success)
            WallpaperResult.PERMISSION_DENIED -> context.getString(R.string.wallpaper_permission_denied)
            WallpaperResult.ERROR -> context.getString(R.string.wallpaper_error)
            null -> null
        }
        if (text != null) {
            snackbarHostState.showSnackbar(text)
            wallpaperVm.clearMessage()
        }
    }

    // Если картинка не найдена
    if (picture == null) {
        Text(stringResource(R.string.picture_not_found))
        return
    }

    PictureContent(
        picture = picture,
        isFavorite = isFavorite,
        onBack = { navController.popBackStack() },
        onAddFavorite = viewModel.onAddToFavorites,
        onRemoveFavorite = viewModel.onRemoveFromFavorites,
        onUpdateFavorite = viewModel.onUpdateFavorites,
        onApplyWallpaper = { url, target -> wallpaperVm.apply(url, target) },
        snackbarHostState = snackbarHostState
    )
}