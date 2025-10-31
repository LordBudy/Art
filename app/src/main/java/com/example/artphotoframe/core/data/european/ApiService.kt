package com.example.artphotoframe.core.data.european

import retrofit2.http.GET
import retrofit2.http.Query

//Интерфейс для работы с API Europeana
//Определяет метод поиска изображений
//Используем Retrofit аннотации
interface ApiService {
    @GET("search.json")  // Эндпоинт Europeana
    suspend fun searchPictures(
        @Query("query") query: String,  // Параметр поиска
        @Query("wskey") wskey: String = "ytextrana",  //ключ
        @Query("rows") rows: Int = 100  // Количество результатов
    ): SearchResponse  // Возвращаем объект ответа
}