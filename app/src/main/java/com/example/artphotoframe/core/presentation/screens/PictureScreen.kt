package com.example.artphotoframe.core.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.artphotoframe.core.data.models.Picture
import com.example.artphotoframe.core.presentation.ui.FullPictureInfo
import com.example.artphotoframe.core.presentation.ui.theme.ArtPhotoFrameTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun PictureScreen(
    picture: Picture
    ) {
        val viewModel: FavoritePicViewModel = koinViewModel()

        // Вычисляем isFavorite (из ViewModel)
        val isFavorite = viewModel.isFavorite(picture)

        ArtPhotoFrameTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
            ) {
                FullPictureInfo(
                    picture = picture,
                    isFavorite = isFavorite,
                    onAddToFavorites = viewModel.onAddToFavorites,
                    onRemoveFromFavorites = viewModel.onRemoveFromFavorites,
                    onUpdateFavorites = viewModel.onUpdateFavorites
                )
            }
        }
    }

    @PreviewLightDark
    @Composable
    fun PrevPicScreen() {
        val pic = Picture(
            id = "1",
            title = "Die Malkunst",
            previewURL = "",
            highQualityURL = "",
            description = "Daten nach Texteingabe migriert, Beschriftung: Signatur:"
        )
        ArtPhotoFrameTheme {
            PictureScreen(picture = pic)
        }
    }