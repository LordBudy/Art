package com.example.artphotoframe.core.presentation.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artphotoframe.core.data.models.Picture
import com.example.artphotoframe.core.domain.favorites.AddToFavoritesUseCase
import com.example.artphotoframe.core.domain.favorites.DeleteFavoriteUseCase
import com.example.artphotoframe.core.domain.favorites.PictureRepository
import com.example.artphotoframe.core.domain.favorites.UpdateFavoriteUseCase
import com.example.artphotoframe.core.domain.search.SearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FullPicFavoriteViewModel(
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase,
    private val updateFavoriteUseCase: UpdateFavoriteUseCase,
    private val pictureRepository: PictureRepository,
    private val searchRepository: SearchRepository
) : ViewModel() {

    // Текущая открытая картинка на экране PictureScreen
    private val _currentPicture = MutableStateFlow<Picture?>(null)
    val currentPicture: StateFlow<Picture?> =  _currentPicture.asStateFlow()

    // Подписка на список избранных из базы
    val favorites: StateFlow<List<Picture>> =
        pictureRepository
            .loadFavoritePictures()  // БД отдаёт Flow<List<Picture>>
            .onEach { Log.d("FullPicVM", "favorites size=${it.size}") }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
    // isFavorite = true, если текущая картинка есть в списке избранных
    val isFavorite: StateFlow<Boolean> =
        combine(currentPicture, favorites) { pic, favs ->
            pic != null && favs.any { it.id == pic.id }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )
    // Загружаем картинку по ID: сначала API → если нет — берём из БД
    fun loadPictureById(id: Int) {
        viewModelScope.launch {
            try {
                // Сначала пробуем API
                val fromApi = searchRepository.getPictureById(id)
                // Если не нашли — смотрим локальную БД (например, favorites)
                val picture = fromApi ?: pictureRepository.getPictureById(id)
                _currentPicture.value = picture
                Log.d("FullPicFavoriteViewModel", "Loaded picture: $picture")
            } catch (e: Exception) {
                Log.e("FullPicFavoriteViewModel", "Error loading picture: ${e.message}")
            }
        }
    }
    // Добавление в избранное
    val onAddToFavorites: (Picture) -> Unit = { picture ->
        viewModelScope.launch {
            addToFavoritesUseCase.invoke(picture)

        }
    }
    // Удаление из избранного
    val onRemoveFromFavorites: (Picture) -> Unit = { picture ->
        viewModelScope.launch {
            deleteFavoriteUseCase.invoke(picture.id)
        }
    }
    // Обновление записи в избранном
    val onUpdateFavorites: (Picture) -> Unit = {picture ->
        viewModelScope.launch {
            updateFavoriteUseCase.invoke(listOf(picture))

        }
    }

}