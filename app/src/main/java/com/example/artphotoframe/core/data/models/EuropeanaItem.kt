package com.example.artphotoframe.core.data.models

import com.google.gson.annotations.SerializedName

// Промежуточная модель для каждого item в Europeana API
data class EuropeanaItem(
    val id: String,  // ID из API
    @SerializedName("dcTitle")
    val dcTitle: List<String>?,  // Title (массив или null)

    @SerializedName("edmPreview")
    val edmPreview: List<String>?,  // Preview image URL

    @SerializedName("dcDescription")
    val dcDescription: List<String>?  // Description (массив или null)
)
//аннотации @SerializedName для соответствия названиям полей в JSON
//основные поля: ID, заголовок, превью-изображение, описание