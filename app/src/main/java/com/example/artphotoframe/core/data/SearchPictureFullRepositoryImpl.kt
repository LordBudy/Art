package com.example.artphotoframe.core.data

import com.example.artphotoframe.core.data.models.Picture
import com.example.artphotoframe.core.domain.search.SearchRepository

//Получаем данные от API и преобразуем их в удобный формат
class SearchPictureFullRepositoryImpl(
    private val metRepository: MetRepository): SearchRepository {

    override suspend fun getSearchPictures(query: String): List<Picture> {
        // Сначала получаем список ID по запросу
        val ids = metRepository.searchIds(query)
        // Затем получаем объекты по этим ID (с кэшированием и батчингом)
        val objects = metRepository.getObjectsBatched(ids)
        // Преобразуем MetObject в Picture с помощью маппера
        return objects.map { Picture.fromMetObject(it) }
    }
    // получение одной картинки по ID из API
    override suspend fun getPictureById(id: Int): Picture? {
        return try {
            val metObject = metRepository.getObjectById(id)
            // Проверяем, что изображение доступно (как в поиске)
            metObject?.let {
                if (it.primaryImageSmall?.isNotBlank() == true && it.primaryImage?.isNotBlank() == true) {
                    Picture.fromMetObject(it)
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            // Логируем ошибку (например, ID не найден)
            null
        }
    }
}
