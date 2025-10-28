package com.example.artphotoframe.core.presentation.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.artphotoframe.core.presentation.ui.FullPictureFavorite
import com.example.artphotoframe.core.presentation.ui.HomeButton
import org.koin.androidx.compose.koinViewModel


@Composable
fun FavoriteScreen(
    navController: NavController
) {
    val viewModel: FullPicFavoriteViewModel = koinViewModel()

    val pictures by viewModel.favorites.collectAsStateWithLifecycle(
        emptyList()
    )

// Фильтруем дубликаты по ID если есть такие то удаляются
    val uniquePictures = pictures.distinctBy { it.id }
    //запускается каждый раз, когда uniquePictures меняется
    LaunchedEffect(uniquePictures) {
        Log.d("FavoriteScreen", "Pictures IDs: ${uniquePictures.map { it.id }}")
        // Проверяет, были ли дубликаты.
        if (uniquePictures.size < pictures.size) {
            Log.w("FavoriteScreen", "Duplicates removed: ${pictures.size - uniquePictures.size}")
        }
    }
    // Загрузка картинок из БД при входе на экран
    LaunchedEffect(Unit) {
        //функция в ViewModel для загрузки из БД
        viewModel.loadFavoritePictures()
    }

    Scaffold(
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
                    //автоматически добавляет(padding) под системные панели
                    .padding(innerPadding)
            ) {


                if (pictures.isEmpty()) {
                    // Обработка пустого списка
                    Text(
                        text = "Нет избранных картинок 😔",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    // Отображение результатов поиска
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = uniquePictures,
                            key = { picture -> picture.id }
                        ) { picture ->  // ← Теперь передаём pictures и key для стабильности!
                            FullPictureFavorite(
                                picture = picture,
                                onClick = {},
                                isFavorite = true,
                                onAddToFavorites = {},
                                onRemoveFromFavorites = viewModel.onRemoveFromFavorites,
                                onUpdateFavorites = viewModel.onUpdateFavorites
                            )
                            HorizontalDivider() // Разделитель между изображениями
                        }
                    }
                }
            }


        }
    )

}