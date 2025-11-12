package com.example.artphotoframe.core.domain.favorites

import com.example.artphotoframe.core.data.models.Picture

class UpdateFavoriteUseCase (private val repository: PictureRepository) {
    suspend fun invoke(params:  List<Picture>) {
        repository.updatePictures(params)
    }
}