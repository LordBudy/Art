package com.example.artphotoframe.core.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.artphotoframe.core.data.models.Picture

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
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
        //автоматически добавляет(padding) под системные панели

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

        // Изображение
        FullPictureFavorite(
            picture = picture,
            onClick = {
                navController.navigate("picture_screen/${picture.id}")
            },
            isFavorite = isFavorite,
            onAddToFavorites = onAddToFavorites,
            onRemoveFromFavorites = onRemoveFromFavorites,
            onUpdateFavorites = onUpdateFavorites,
            menu = {}
        )


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




//@PreviewLightDark
//@Composable
//fun PreviewFullPictureInfo() {
//    val pic = Picture(
//        id = 1,
//        title = "Die Malkunst",
//        previewURL = "",
//        highQualityURL = "",
//        description = "Daten nach Texteingabe migriert, Beschriftung: Signatur"
//    )
//    ArtPhotoFrameTheme {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(color = Color.White)
//        ) {
//            FullPictureInfo(
//                picture = pic,
//                isFavorite = false,
//                onAddToFavorites = {},
//                onRemoveFromFavorites = {},
//                onUpdateFavorites = {},
//                modifier = Modifier,
//                navController = rememberNavController()
//            )
//        }
//    }
//}