package com.example.artphotoframe.core.data.models

//Представляет конечную модель изображения
//Содержит метод для преобразования данных из EuropeanaItem


data class Picture(
    val id: String,
    val title: String?,
    val imageURL: List<String>, // Храним список URL превью
    val description: String?
) {
    companion object {
        // Функция для преобразования EuropeanaItem в Picture
        // (аналогично JS-логике из почтальона)
        fun fromEuropeanaItem(item: EuropeanaItem): Picture {
            // Handle title: массив -> join(", "), строка -> как есть, иначе fallback
            val title = when {
                item.dcTitle?.isNotEmpty() == true -> item.dcTitle.joinToString(", ")
                else -> "No title available"
            }

            // Image URL: напрямую, с возможным null
            val imageURL = item.edmPreview ?: "Нет предварительного изображения URL"

            // Handle description: аналогично title
            val body = when {
                item.dcDescription?.isNotEmpty() == true -> item.dcDescription.joinToString(", ")
                else -> "No description available"
            }

            return Picture(
                id = item.id,
                title = title,
                imageURL = item.edmPreview ?: emptyList(),
                description = body
            )
        }
    }
}
