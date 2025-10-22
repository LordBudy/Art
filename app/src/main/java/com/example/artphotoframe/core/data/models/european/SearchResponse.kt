package com.example.artphotoframe.core.data.models.european

//Контейнер для ответа от API
//Содержит список найденных элементов
data class SearchResponse(

    // Массив картинок
    val items: List<EuropeanaItem>?
)
