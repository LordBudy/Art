package com.example.artphotoframe.core.presentation.ui

import FullPicture
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.artphotoframe.core.data.models.Picture
import com.example.artphotoframe.core.presentation.ui.theme.ArtPhotoFrameTheme

@Composable
fun FullPictureInfo(
    picture: Picture,
    navController: NavController,
    //начальное состояние (true/false из ViewModel)
    isFavorite: Boolean,
    //для добавления AddToFavoritesUseCase
    onAddToFavorites: (Picture) -> Unit,
    //для удаления  DeleteFavoriteUseCase
    onRemoveFromFavorites: (Picture) -> Unit,
    //для обновления UpdateFavoriteUseCase
    onUpdateFavorites: (Picture) -> Unit,
    modifier: Modifier = Modifier
) {

    ArtPhotoFrameTheme {
        Scaffold(
            modifier = modifier,
            floatingActionButton = {
                FavoritesButton(
                    color = MaterialTheme.colorScheme.onBackground,
                    //переход в избранное
                    onClick = { navController.navigate("search_screen") },
                    modifier = Modifier
                )
            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.background)
                        //автоматически добавляет(padding) под системные панели
                        .padding(innerPadding)
                ) {
                    Row {
                        BackButton(
                            onClick = { navController.popBackStack() },
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        // Заголовок
                        Text(
                            text = picture.title ?: "Нет заголовка",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .padding(15.dp)
                        )
                    }
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Изображение
                        FullPicture(
                            picture = picture,
                            onClick = {
                                navController.navigate("picture_screen/${picture.id}")
                            }
                        )
                        BtnFavorite(
                            picture = picture,
                            isFavorite = isFavorite,
                            onAddToFavorites = onAddToFavorites,
                            onRemoveFromFavorites = onRemoveFromFavorites,
                            onUpdateFavorites = onUpdateFavorites,
                            modifier = Modifier.align(Alignment.TopEnd)  // Позиционирование в правом верхнем углу
                        )

                    }

                    // Описание
                    Text(
                        // Обработка null
                        text = picture.description ?: "Нет описания",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                            .wrapContentHeight(unbounded = true)

                    )
                }
            }
        )
    }
}

@PreviewLightDark
@Composable
fun PreviewFullPictureInfo() {
    val pic = Picture(
        id = 1,
        title = "Die Malkunst",
        previewURL = "",
        highQualityURL = "",
        description = "Daten nach Texteingabe migriert, Beschriftung: Signatur"
    )
    ArtPhotoFrameTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            FullPictureInfo(
                picture = pic,
                isFavorite = false,
                onAddToFavorites = {},
                onRemoveFromFavorites = {},
                onUpdateFavorites = {},
                modifier = Modifier,
                navController = rememberNavController()
            )
        }
    }
}