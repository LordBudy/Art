package com.example.artphotoframe.core.domain.favorites

import com.example.artphotoframe.core.data.models.Picture
import kotlinx.coroutines.flow.Flow

class GetAllFavoritesUseCase(private val repository: PictureRepository) {

    fun invoke(): Flow<List<Picture>> {
        return repository.getAllPictures()
    }
}