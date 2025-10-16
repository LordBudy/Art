package com.example.artphotoframe.core.data.models.european

import com.google.gson.annotations.SerializedName

// Промежуточная модель для каждого item в Europeana API
data class EuropeanaItem(
    val id: String,  // ID из API
    @SerializedName("dcTitle")
    val dcTitle: List<String>?,  // Title

    @SerializedName("edmPreview")
    val edmPreview: List<String>?,  // Preview image URL

    @SerializedName("edmHighQuality")
    val edmHighQuality: List<String>?,  // High quality image URL

    @SerializedName("dcDescription")
    val dcDescription: List<String>?  // Description
)
//аннотации @SerializedName для соответствия названиям полей в JSON
//основные поля: ID, заголовок, превью-изображение, описание