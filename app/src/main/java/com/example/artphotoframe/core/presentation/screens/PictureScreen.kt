package com.example.artphotoframe.core.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.artphotoframe.core.presentation.ui.FullPictureInfo
import org.koin.androidx.compose.koinViewModel

@Composable
fun PictureScreen(
    pictureId: Int,
    navController: NavController
) {
    val viewModel: FullPicFavoriteViewModel = koinViewModel()

    // Загружаем картинку при создании экрана
    LaunchedEffect(pictureId) {
        viewModel.loadPictureById(pictureId)
    }
    // Собираем состояние текущей картинки
    val picture = viewModel.currentPicture.collectAsStateWithLifecycle().value
    val isFavorite = viewModel.isFavorite.collectAsStateWithLifecycle().value



    if (picture != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
            ) {
                FullPictureInfo(
                    picture = picture,
                    navController = navController,
                    isFavorite = isFavorite,
                    onAddToFavorites = viewModel.onAddToFavorites,
                    onRemoveFromFavorites = viewModel.onRemoveFromFavorites,
                    onUpdateFavorites = viewModel.onUpdateFavorites
                )
            }

    } else {
        // если картинка не найдена
        Text("Картинка не найдена !",
            color = Color.Red,
            fontSize = 32.sp ,
            modifier = Modifier
                .padding(start = 70.dp, top = 70.dp))

    }
}
