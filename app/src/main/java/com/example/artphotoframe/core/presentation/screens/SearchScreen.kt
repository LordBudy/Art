package com.example.artphotoframe.core.presentation.screens

import FullPicture
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.artphotoframe.core.data.models.Picture
import com.example.artphotoframe.core.presentation.ui.FastSearch
import com.example.artphotoframe.core.presentation.ui.theme.ArtPhotoFrameTheme
import com.example.artphotoframe.core.presentation.viewModels.SearchViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(viewModel: SearchViewModel = koinViewModel()) {
    val pictures by viewModel.pictures
        .collectAsStateWithLifecycle(emptyList())

    // можно добавить тестовые данные для превью сюда:

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
                    viewModel.searchPictures(newText)
                },
                onSearchClick = { query ->
                    viewModel.searchPictures(query)
                },
                modifier =
                    Modifier.padding(bottom = 8.dp)
            )

            // Отображение результатов поиска
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(pictures) { picture ->
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
        id = "1",
        title = "Sample Picture",
        imageURL = listOf(
            "https://api.europeana.eu/thumbnail/v2/url.json?uri=http%3A%2F%2Fimageapi.khm.at%2Fimages%2F2574%2FGG_9128_Web.jpg&type=IMAGE"),
        description = "Описание изображения"
    )

    ArtPhotoFrameTheme {
        SearchScreen()
    }
}