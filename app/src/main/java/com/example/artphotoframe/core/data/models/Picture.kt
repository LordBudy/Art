package com.example.artphotoframe.core.data.models

import com.example.artphotoframe.core.data.models.european.EuropeanaItem

//Представляет конечную модель изображения
//Содержит метод для преобразования данных из EuropeanaItem


data class Picture(
    val id: String,
    val title: String?,
    val previewURL:String?,
    val highQualityURL: String?,
    val description: String?
) {
    companion object {
        // Функция для преобразования EuropeanaItem в Picture
        // (аналогично JS-логике из почтальона)
        fun fromEuropeanaItem(item: EuropeanaItem): Picture {
            // Handle title: массив -> join(", "),
            // строка -> как есть, иначе fallback  Обработка заголовка
            val title = when {
                item.dcTitle?.isNotEmpty() == true -> item.dcTitle.joinToString(", ")
                else -> "No title available"
            }

            // Handle description: аналогично title  Обработка описания
            val body = when {
                item.dcDescription?.isNotEmpty() == true -> item.dcDescription.joinToString(", ")
                else -> "No description available"
            }

            return Picture(
                id = item.id,
                title = title,
                previewURL = item.edmPreview?.firstOrNull() ?: "No preview image URL",
                highQualityURL = item.edmHighQuality?.firstOrNull() ?: "No high quality image URL",
                description = body
            )
        }
    }
}
