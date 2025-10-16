package com.example.artphotoframe.core.data

import com.example.artphotoframe.core.data.models.european.SearchResponse
import com.example.artphotoframe.core.data.models.Picture
import com.example.artphotoframe.core.domain.search.SearchRepository

//Получаем данные от API и преобразуем их в удобный формат
class SearchPictureFullRepositoryImpl(private val apiService: ApiService): SearchRepository {
    override suspend fun getSearchPictures(query: String): List<Picture> {
        val response: SearchResponse = apiService.searchPictures(query)

        return response.items?.map {
            Picture.fromEuropeanaItem(it) } ?: emptyList()
    }
}