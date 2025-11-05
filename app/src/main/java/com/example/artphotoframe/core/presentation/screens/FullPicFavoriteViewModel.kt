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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FullPicFavoriteViewModel(
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase,
    private val updateFavoriteUseCase: UpdateFavoriteUseCase,
    private val pictureRepository: PictureRepository,
    private val searchRepository: SearchRepository
) : ViewModel() {

    // Состояние для хранения избранных картинок
    private val _favorites = MutableStateFlow<List<Picture>>(emptyList())
    val favorites: StateFlow<List<Picture>> = _favorites.asStateFlow()

    // Состояние для текущей картинки
    private val _currentPicture = MutableStateFlow<Picture?>(null)
    val currentPicture: StateFlow<Picture?> = _currentPicture

    // Состояние для isFavorite
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    // Новый метод для загрузки избранных картинок из БД
    fun loadFavoritePictures() {
        viewModelScope.launch {
            pictureRepository.loadFavoritePictures().collect { pictures ->
                _favorites.value = pictures
                Log.d(
                    "FullPicFavoriteViewModel",
                    "Loaded favorites: ${pictures.size} pictures"
                )
            }
        }
    }

    // Получение картинки по ID
    fun loadPictureById(id: Int) {
        viewModelScope.launch {
            try {
                // Сначала пытаемся загрузить из API
                var picture = searchRepository.getPictureById(id)
                if (picture == null) {
                    // Если не найдено в API, пробуем локальную БД (для favorites)
                    picture = pictureRepository.getPictureById(id)
                }
                _currentPicture.value = picture
                Log.d("FullPicFavoriteViewModel", "Loaded picture: $picture")
                _isFavorite.value = picture?.let { isFavorite(it) } ?: false
            } catch (e: Exception) {
                Log.e("FullPicFavoriteViewModel", "Error loading picture: ${e.message}")
            }
        }
    }

    // является ли картинка избранной
    fun isFavorite(picture: Picture): Boolean {
        return favorites.value.any { it.id == picture.id }
    }


    val onAddToFavorites: (Picture) -> Unit = { picture ->
        viewModelScope.launch {
            addToFavoritesUseCase.invoke(picture)

            // После добавления обновляем список favorites
            loadFavoritePictures()
        }
    }

    val onRemoveFromFavorites: (Picture) -> Unit = { picture ->
        viewModelScope.launch {
            deleteFavoriteUseCase.invoke(picture.id)

            // После удаления обновляем список favorites
            loadFavoritePictures()
        }
    }

    val onUpdateFavorites: (Picture) -> Unit = {picture ->
        viewModelScope.launch {
            updateFavoriteUseCase.invoke(listOf(picture))
            // После обновления перезагружаем список из бд
            loadFavoritePictures()
        }
    }

}