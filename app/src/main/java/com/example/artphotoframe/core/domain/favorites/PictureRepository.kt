package com.example.artphotoframe.core.domain.favorites

import com.example.artphotoframe.core.data.models.Picture
import kotlinx.coroutines.flow.Flow

interface PictureRepository{

    suspend fun addToFavorites(picture: Picture)

    fun getAllPictures(): Flow<List<Picture>>

    suspend fun getPictureById(id: String): Picture?

    suspend fun deletePictureById(id: String): Picture

    suspend fun deleteAllPictures(): Flow<List<Picture>>

    suspend fun updatePictures(picture: Picture)
}