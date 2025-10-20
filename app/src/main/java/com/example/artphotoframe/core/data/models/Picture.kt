package com.example.artphotoframe.core.data.models

import com.example.artphotoframe.core.data.models.metropolitan.MetObject

//Представляет конечную модель изображения
//Содержит метод для преобразования данных из MetObject


data class Picture(
    val id: Int,
    val title: String?,
    val previewURL: String?,
    val highQualityURL: String?,
    val description: String?
) {
    companion object {
        fun fromMetObject(metObject: MetObject): Picture {
            // Реализуйте маппинг полей, например:
            return Picture(
                id = metObject.objectID,  // Предполагая, что id - Int
                title = metObject.title ?: "Unknown Title",
                previewURL = metObject.primaryImageSmall ?: "",  // Маленькое изображение (превью)
                highQualityURL = metObject.primaryImage ?: "",  // Высококачественное изображение
                description = metObject.artistDisplayName
                    ?: "Unknown Artist"
            )
        }
    }

}
