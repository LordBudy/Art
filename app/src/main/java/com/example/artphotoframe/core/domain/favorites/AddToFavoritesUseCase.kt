package com.example.artphotoframe.core.domain.favorites

import com.example.artphotoframe.core.data.models.Picture

class AddToFavoritesUseCase(private val repository: PictureRepository) {

    suspend fun invoke(params: Picture) {
        repository.addToFavorites(params)
    }
}