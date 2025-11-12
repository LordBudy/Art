package com.example.artphotoframe.core.domain.favorites

import com.example.artphotoframe.core.data.models.Picture
import kotlinx.coroutines.flow.Flow

class DeleteAllFavoritesUseCase(private val repository: PictureRepository) {

    suspend fun invoke(): Flow<List<Picture>>{
        return repository.deleteAllPictures()
    }
}