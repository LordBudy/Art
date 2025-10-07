package com.example.artphotoframe.core.presentation.ui

import FullPicture
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artphotoframe.core.data.models.Picture
import com.example.artphotoframe.core.presentation.ui.theme.ArtPhotoFrameTheme

@Composable
fun FullPictureInfo(
    picture: Picture,
    modifier: Modifier = Modifier
) {

    ArtPhotoFrameTheme {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            // Заголовок
            Text(
                text = picture.title ?: "Нет заголовка",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding( 15.dp)
            )
            // Изображение
            FullPicture(
                picture = picture
            )

            // Описание
            Text(
                                     // Обработка null
                text = picture.description ?: "Нет описания",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding (15.dp)
                    .wrapContentHeight(unbounded = true)

            )
        }
    }
}
@PreviewLightDark
@Composable
fun PreviewFullPictureInfo() {
    val pic = Picture(
        id = "1",
        title = "Die Malkunst",
        imageURL = listOf(
            "https://api.europeana.eu/thumbnail/v2/url.json?uri=http%3A%2F%2Fimageapi.khm.at%2Fimages%2F2574%2FGG_9128_Web.jpg&type=IMAGE"),
        description = "Daten nach Texteingabe migriert, Beschriftung: Signatur"
    )
    ArtPhotoFrameTheme {
        FullPictureInfo(
            picture = pic,
            modifier = Modifier

        )
    }
}