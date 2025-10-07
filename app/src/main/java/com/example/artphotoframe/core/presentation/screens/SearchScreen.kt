package com.example.artphotoframe.core.presentation.screens

import FullPicture
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.artphotoframe.R
import com.example.artphotoframe.core.data.models.Picture
import com.example.artphotoframe.core.presentation.ui.FastSearch
import com.example.artphotoframe.core.presentation.ui.theme.ArtPhotoFrameTheme

@Composable
fun SearchScreen() {

    val pictures = remember { mutableStateListOf<Picture>() } // Список для хранения найденных изображений
    // Добавляем тестовые данные для превью
    LaunchedEffect(Unit) {
        pictures.addAll(
            listOf(
                Picture(
                    id = 1,
                    title = "Sample Picture 1",
                    imageURL = "https://api.europeana.eu/thumbnail/v2/url.json?uri=http%3A%2F%2Fimageapi.khm.at%2Fimages%2F2574%2FGG_9128_Web.jpg&type=IMAGE",
                    body = "Описание изображения 1"
                ),
                Picture(
                    id = 2,
                    title = "Sample Picture 2",
                    imageURL = "https://api.europeana.eu/thumbnail/v2/url.json?uri=https%3A%2F%2Fwww.rijksmuseum.nl%2Fassetimage2.jsp%3Fid%3DSK-A-2344&type=IMAGE",
                    body = "Описание изображения 2"
                ),
                Picture(
                    id = 3,
                    title = "Тестовая картина 3",
                    imageURL = "https://api.europeana.eu/thumbnail/v2/url.json?uri=https%3A%2F%2Fwww.rijksmuseum.nl%2Fassetimage2.jsp%3Fid%3DSK-A-2860&type=IMAGE",
                    body = "Описание третьей тестовой картины"
                ),
                Picture(
                    id = 4,
                    title = "Sample Picture 4",
                    imageURL = "https://api.europeana.eu/thumbnail/v2/url.json?uri=https%3A%2F%2Frs.cms.hu-berlin.de%2Fikb_mediathek%2Fpages%2Fdownload.php%3Fref%3D15830%26size%3Dscr%26ext%3Djpg%26page%3D1%26alternative%3D-1%26k%3D%26noattach%3Dtrue&type=IMAGE",
                    body = "Описание изображения 4"
                ),
                Picture(
                    id = 5,
                    title = "Sample Picture 5",
                    imageURL = "https://api.europeana.eu/thumbnail/v2/url.json?uri=http%3A%2F%2Fhdl.handle.net%2F10622%2F30051000207552%3Flocatt%3Dview%3Alevel3&type=IMAGE",
                    body = "Описание изображения 5"
                )
            )
        )
    }
    ArtPhotoFrameTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color =
                        MaterialTheme.colorScheme.background
                )
                .systemBarsPadding()
        ) {

            // Панель поиска
            FastSearch(
                text = "",
                onValueChange = { newText ->
                    // добавить логику поиска по введенному тексту fetchPictures(newText)
                },
                onSearchClick = { query ->

                },
                modifier =
                    Modifier.padding(bottom = 8.dp)
            )

            // Отображение результатов поиска
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(pictures.size) { index ->
                    val picture = pictures[index]
                    FullPicture(picture = picture)
                    HorizontalDivider() // Разделитель между изображениями
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewSearchScreen() {
    val samplePicture = Picture(
        id = 1,
        title = "Sample Picture",
        imageURL = "https://example.com/image.jpg",
        body = "Описание изображения"
    )

    ArtPhotoFrameTheme {
        SearchScreen()
    }
}