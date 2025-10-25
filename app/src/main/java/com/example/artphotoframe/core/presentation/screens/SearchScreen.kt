package com.example.artphotoframe.core.presentation.screens

import FullPicture
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.room.util.query
import com.example.artphotoframe.core.presentation.ui.FastSearch
import com.example.artphotoframe.core.presentation.ui.FavoritesButton
import com.example.artphotoframe.core.presentation.ui.FullPictureFavorite
import com.example.artphotoframe.core.presentation.ui.theme.ArtPhotoFrameTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = koinViewModel()
) {
    val pictures by viewModel.pictures
        //для автоматического управления жизненным циклом collectAsStateWithLifecycle()
        .collectAsStateWithLifecycle(emptyList())

    val listState: LazyListState = rememberLazyListState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val total = listState.layoutInfo.totalItemsCount
            total > 0 && lastVisible >= total - 6
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) viewModel.loadMore()
    }

    // Состояние для текста поиска
    var searchText by remember { mutableStateOf("") }

    ArtPhotoFrameTheme {
        Scaffold(
            floatingActionButton = {
                FavoritesButton(
                    color = MaterialTheme.colorScheme.onBackground,
                    //переход в избранное
                    onClick = { navController.navigate("favorite_screen") },
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

                    // Панель поиска
                    FastSearch(
                        text = searchText,
                        onValueChange = { newText ->
                            searchText = newText
                        },
                        onSearchClick = { query ->
                            if (query.isBlank()) {
                                Log.d("LaunchedEffect", "вызываем loadAllPictures")
                                viewModel.loadAllPictures()
                            } else {
                                Log.d("LaunchedEffect", "вызываем searchPictures")
                                viewModel.searchPictures(query)
                            }
                        },
                        modifier =
                            Modifier.padding(bottom = 8.dp)
                    )

                    // Отображение результатов поиска
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        items(
                            pictures,
                            key = { picture -> picture.id }
                        ) { picture ->
                            FullPicture(
                                picture = picture,
                                onClick = {
                                    navController
                                        .navigate("picture_screen/${picture.id}")
                                }
                            )
                            HorizontalDivider() // Разделитель между изображениями
                        }
                    }
                }
            }
        )
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