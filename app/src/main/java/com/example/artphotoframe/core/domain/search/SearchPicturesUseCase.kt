package com.example.artphotoframe.core.domain.search

import com.example.artphotoframe.core.data.models.Picture

            //Use Case для поиска изображений
            //Абстрагирует логику поиска
class SearchPicturesUseCase(private val searchRepository: SearchRepository) {

    suspend operator fun invoke(query: String): List<Picture> {
        return searchRepository.getSearchPictures(query)
    }

}