package com.example.artphotoframe.core.domain.search

import com.example.artphotoframe.core.data.models.Picture

class GetPictureByIdUseCase (private val searchRepository: SearchRepository){
    suspend operator fun invoke(id: Int): Picture? {
        return searchRepository.getPictureById(id)
    }
}