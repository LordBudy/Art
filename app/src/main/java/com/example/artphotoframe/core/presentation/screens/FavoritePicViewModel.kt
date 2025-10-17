package com.example.artphotoframe.core.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artphotoframe.core.data.models.Picture
import com.example.artphotoframe.core.domain.favorites.AddToFavoritesUseCase
import com.example.artphotoframe.core.domain.favorites.DeleteFavoriteUseCase
import com.example.artphotoframe.core.domain.favorites.UpdateFavoriteUseCase
import kotlinx.coroutines.launch

class FavoritePicViewModel (
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase,
    private val updateFavoriteUseCase: UpdateFavoriteUseCase
): ViewModel(){
    // Функция для проверки, является ли картинка избранной (из repository)
    fun isFavorite(picture: Picture): Boolean {
        // Реализуй логику: проверь в repository или кэше
        // Например: return favoritesRepository.getFavoritePicture(picture.id) != null
        return false // Заглушка (инжектируй repository и реализуй)
    }

    // Лямбды для передачи в UI
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

    val onUpdateFavorites: (Picture) -> Unit = { picture ->
        viewModelScope.launch {
            updateFavoriteUseCase.invoke(picture)
        }
    }
}