package com.example.artphotoframe.core.data

import com.example.artphotoframe.core.data.db.PictureDao
import com.example.artphotoframe.core.data.db.model.PictureEntityMapper
import com.example.artphotoframe.core.data.models.Picture
import com.example.artphotoframe.core.domain.favorites.PictureRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FavoritePictureRepositoryImpl (
    private val dao: PictureDao,
    private val mapper: PictureEntityMapper
) : PictureRepository {

    override suspend fun addToFavorites(picture: Picture) {
        dao.insert(mapper.toEntity(picture))
    }

    override fun getAllPictures(): Flow<List<Picture>> {
        return dao.getAllPictures().map { list ->
            list.map { mapper.fromEntity(it) }
        }
    }

    override suspend fun getPictureById(id: String): Picture? {
        return dao.getPictureById(id)?.let { mapper.fromEntity(it) }
    }

    override suspend fun deletePictureById(id: String): Picture {
        val picture = getPictureById(id)
        dao.deletePictureById(id)
        return picture ?: throw IllegalArgumentException("Picture not found")
    }

    override suspend fun deleteAllPictures(): Flow<List<Picture>> {
        val pictures = getAllPictures().first()
        dao.deleteAllPictures()
        return flow { emit(pictures) }
    }

    override suspend fun updatePictures(picture: Picture) {
        dao.updatePictures(mapper.toEntity(picture))
    }
}