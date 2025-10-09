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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.artphotoframe.core.data.models.Picture
import com.example.artphotoframe.core.presentation.ui.FastSearch
import com.example.artphotoframe.core.presentation.ui.theme.ArtPhotoFrameTheme
import com.example.artphotoframe.core.presentation.viewModels.SearchViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(viewModel: SearchViewModel = koinViewModel()) {
    val pictures by viewModel.pictures
        .collectAsStateWithLifecycle(emptyList())

    val scope = rememberCoroutineScope()

    // Добавлено: SnackbarHostState для уведомлений об ошибках
    val snackbarHostState = remember { SnackbarHostState() }

    // Состояние для текста поиска
    var searchText by remember { mutableStateOf("") }

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
                text = searchText,
                onValueChange = { newText ->
                    searchText = newText
                    if (newText.isNotBlank() && newText.length > 2) {
                        scope.launch {
                            try {
                                viewModel.searchPictures(newText)
                            } catch (e: Exception) {
                                // Показываем ошибку в Snackbar
                                snackbarHostState.showSnackbar("Ошибка поиска: ${e.localizedMessage}")
                            }
                        }
                    }
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

//@PreviewLightDark
//@Composable
//fun PreviewSearchScreen() {
//    val samplePicture = Picture(
//        id = "1",
//        title = "Sample Picture",
//        imageURL = listOf(
//            "https://api.europeana.eu/thumbnail/v2/url.json?uri=http%3A%2F%2Fimageapi.khm.at%2Fimages%2F2574%2FGG_9128_Web.jpg&type=IMAGE"
//        ),
//        description = "Описание изображения"
//    )
//
//    ArtPhotoFrameTheme {
//        SearchScreen()
//    }
//}