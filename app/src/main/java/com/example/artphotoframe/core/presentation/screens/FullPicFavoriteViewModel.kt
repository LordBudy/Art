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

    // Состояние для текущей картинки
    private val _currentPicture = MutableStateFlow<Picture?>(null)
    val currentPicture: StateFlow<Picture?> =  _currentPicture.asStateFlow()

    val favorites: StateFlow<List<Picture>> =
        pictureRepository
            .loadFavoritePictures()              // Должен вернуть Flow<List<Picture>>
            .onEach { Log.d("FullPicVM", "favorites size=${it.size}") }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val isFavorite: StateFlow<Boolean> =
        combine(currentPicture, favorites) { pic, favs ->
            pic != null && favs.any { it.id == pic.id }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

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

    fun isFavoriteSync(picture: Picture): Boolean =
        favorites.value.any { it.id == picture.id }

    val onAddToFavorites: (Picture) -> Unit = { picture ->
        viewModelScope.launch {
            addToFavoritesUseCase.invoke(picture)

        }
    }

    val onRemoveFromFavorites: (Picture) -> Unit = { picture ->
        viewModelScope.launch {
            deleteFavoriteUseCase.invoke(picture.id)
        }
    }

    val onUpdateFavorites: (Picture) -> Unit = {picture ->
        viewModelScope.launch {
            updateFavoriteUseCase.invoke(listOf(picture))

        }
    }

}