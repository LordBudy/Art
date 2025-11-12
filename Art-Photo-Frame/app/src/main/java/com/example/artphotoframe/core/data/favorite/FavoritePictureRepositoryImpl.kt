package com.example.artphotoframe.core.data.favorite

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

    // добавляет в бд
    override suspend fun addToFavorites(picture: Picture) {
        dao.insert(mapper.toEntity(picture))
    }
    // Загружает все избранные картинки, Flow позволяет наблюдать за
    //  изменениями в реальном времени  при обновлении бд
    override fun loadFavoritePictures(): Flow<List<Picture>> {
        return dao.getAllPictures().map { list ->
            // Преобразует каждый entity в Picture
            list.map { mapper.fromEntity(it) }
        }
    }
    // Получает картинку по ID: если найдена в базе
    override suspend fun getPictureById(id: Int): Picture? {
        return dao.getPictureById(id)?.let { mapper.fromEntity(it) }
    }
    // Сначала получает картинку, затем удаляет из бд
    // Если не найдена, бросаем исключение
    override suspend fun deletePictureById(id: Int): Picture {
        val picture = getPictureById(id)
        dao.deletePictureById(id) // Удаление из бд
        return picture ?: throw IllegalArgumentException("Picture not found")
    }

    override suspend fun deleteAllPictures(): Flow<List<Picture>> {
        // Получаем текущий список
        val pictures = loadFavoritePictures().first()
        // Удаляем все записи из базы
        dao.deleteAllPictures()
        return flow { emit(pictures) }
    }

    override suspend fun updatePictures(pictures: List<Picture>) {
        val entities = pictures.map { mapper.toEntity(it) }
        dao.updatePictures(entities)
    }
}