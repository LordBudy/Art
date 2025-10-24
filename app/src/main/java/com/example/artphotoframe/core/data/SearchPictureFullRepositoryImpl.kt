package com.example.artphotoframe.core.data

import com.example.artphotoframe.core.data.models.Picture
import com.example.artphotoframe.core.domain.search.SearchRepository

//Получаем данные от API и преобразуем их в удобный формат
class SearchPictureFullRepositoryImpl(
    private val metRepository: MetRepository): SearchRepository {

    override suspend fun getSearchPictures(query: String, page: Int, pageSize: Int): List<Picture> {
        // Сначала получаем список ID по запросу
        val ids = metRepository.searchIds(query)
        if (ids.isEmpty()) return emptyList()

        // Вычисляем индексы для слайса (0-based)
        val startIndex = page * pageSize
        val endIndex = (startIndex + pageSize)
            .coerceAtMost(ids.size)

        // Берем слайс ID для текущей страницы
        val pageIds = ids.slice(startIndex until endIndex)
        if (pageIds.isEmpty()) return emptyList()

        // Получаем объекты по слайсу ID (с кэшированием и батчингом)
        val objects = metRepository.getObjectsBatched(pageIds)

        // Преобразуем MetObject в Picture (только с изображениями)
        return objects
            .filter { it.primaryImageSmall?.isNotBlank() == true && it.primaryImage?.isNotBlank() == true }
            .map { Picture.fromMetObject(it) }

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
