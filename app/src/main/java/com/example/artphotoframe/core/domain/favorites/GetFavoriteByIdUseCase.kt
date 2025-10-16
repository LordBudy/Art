package com.example.artphotoframe.core.domain.favorites

import com.example.artphotoframe.core.data.models.Picture

class GetFavoriteByIdUseCase(private val repository: PictureRepository) {

    suspend fun invoke(params: String): Picture? {
        return repository.getPictureById(params)
    }
}