package com.example.artphotoframe.core.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.artphotoframe.core.data.models.Picture
import com.example.artphotoframe.core.presentation.ui.theme.ArtPhotoFrameTheme

@Composable
fun BtnFavorite(
    picture: Picture,
    isFavorite: Boolean,  //начальное состояние
    onAddToFavorites: (Picture) -> Unit,  //для добавления
    onRemoveFromFavorites: (Picture) -> Unit,  //для удаления
    onUpdateFavorites: (Picture) -> Unit,  //для обновления
    modifier: Modifier = Modifier
) {

    var isChecked by remember { mutableStateOf(isFavorite) }  // Начальное значение true, как в твоём примере

    IconButton(
        onClick = {
            if (!isChecked) {
                // клик "добавить" = true
                onAddToFavorites(picture)
            } else {
                //клик удалить = false
                onRemoveFromFavorites(picture)
            }
            // всегда обновляется после изменения
            onUpdateFavorites(picture)
            // переключается состояние
            isChecked = !isChecked
        },
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.Favorite,  // Иконка сердца
            contentDescription = "Сердце",  // Описание для доступности
            tint = if (isChecked) Color.Red else Color.Gray  // Цвет: красный если true, серый если false
        )
    }
}

@PreviewLightDark
@Composable
fun PreviewHeartCheckbox() {
    val pic = Picture(
        id = 1,
        title = "Test",
        previewURL = "",
        highQualityURL = "",
        description = "Test"
    )
    ArtPhotoFrameTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            BtnFavorite(
                picture = pic,
                isFavorite = false,
                onAddToFavorites = {},
                onRemoveFromFavorites = {},
                onUpdateFavorites = {}
            )
        }
    }
}