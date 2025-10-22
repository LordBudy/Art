package com.example.artphotoframe.core.domain.search

import com.example.artphotoframe.core.data.models.Picture

         //Интерфейс репозитория
interface SearchRepository {
   suspend fun getSearchPictures(query: String): List<Picture>

   suspend fun getPictureById(id: Int): Picture?
}