package com.example.artphotoframe.core.data.models

//Контейнер для ответа от API
//Содержит список найденных элементов
data class EuropeanaSearchResponse(

    // Массив картинок
    val items: List<EuropeanaItem>?
)
