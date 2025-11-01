package com.example.artphotoframe.core.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.artphotoframe.R
import com.example.artphotoframe.core.data.models.Picture
import com.example.artphotoframe.core.domain.wallpaper.WallpaperTarget
import com.example.artphotoframe.core.presentation.ui.theme.ArtPhotoFrameTheme

@Composable
fun FullPictureFavorite(
    picture: Picture,
    // было onClick: () -> Unit, стало необязательным
    onImageClick: (() -> Unit)? = null,
    isFavorite: Boolean,
    onAddToFavorites: (Picture) -> Unit,
    onRemoveFromFavorites: (Picture) -> Unit,
    onUpdateFavorites: (Picture) -> Unit,
    menu: (@Composable () -> Unit)? = null
) {
    Box {
        val imageUrl = picture.previewURL
        val clickModifier = if (onImageClick != null) {
            Modifier.clickable { onImageClick() }   // клик только если передали обработчик
        } else {
            Modifier
        }
        if (imageUrl != null) {
            // Если URL есть, используем AsyncImage для загрузки из сети
            AsyncImage(
                model = imageUrl,
                contentDescription = "Picture image",
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(10.dp))
                    .then(clickModifier),
                placeholder = painterResource(id = R.drawable.media),
                error = painterResource(id = R.drawable.media)
            )

        } else {
            // Если URL нет, используем Image с Painter (ресурсом)
            Image(
                painter = painterResource(id = R.drawable.media),
                contentDescription = "Picture image",
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(10.dp))
                    .border(
                        width = 1.5.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(10.dp)
                    )
            )
        }
        // Кнопка избранного
        BtnFavorite(
            picture = picture,
            isFavorite = isFavorite,// Передаём напрямую (BtnFavorite сам управляет toggle)
            onAddToFavorites = onAddToFavorites,
            onRemoveFromFavorites = onRemoveFromFavorites,
            onUpdateFavorites = onUpdateFavorites,
            modifier = Modifier.align(Alignment.TopStart)  // Позиционирование в правом верхнем углу
        )
        // ⋮ Меню — рисуем только если передали слот
        if (menu != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
            ) {
                menu()
            }
        }
    }
}

@PreviewLightDark
@Composable
fun FullPictureFavoritePreview() {
    val pic = Picture(
        id = 1,
        title = "Die Malkunst",
        previewURL = "https://api.europeana.eu/thumbnail/v2/url.json?uri=http%3A%2F%2Fimageapi.khm.at%2Fimages%2F2574%2FGG_9128_Web.jpg&type=IMAGE",
        highQualityURL = null,
        description = "Daten nach Texteingabe migriert, Beschriftung:"
    )
    ArtPhotoFrameTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            FullPictureFavorite(
                picture = pic,
                onImageClick = {},
                isFavorite = false,
                onAddToFavorites = {},
                onRemoveFromFavorites = {},
                onUpdateFavorites = {},
                menu = {FavoriteItemMenu {  }}
            )
        }
    }
}