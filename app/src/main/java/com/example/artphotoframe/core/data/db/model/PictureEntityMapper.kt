package com.example.artphotoframe.core.data.db.model

import com.example.artphotoframe.core.data.db.model.PictureEntity
import com.example.artphotoframe.core.data.models.Picture

class PictureEntityMapper {

    fun toEntity(picture: Picture): PictureEntity {
        return PictureEntity(
            pictureId = picture.id,
            title = picture.title ?: "",
            previewURL = picture.previewURL ?: "",
            highQualityURL = picture.highQualityURL ?: "",
            description = picture.description ?: "",
            isFavorite = true
        )
    }

    fun fromEntity(entity: PictureEntity): Picture {
        return Picture(
            id = entity.pictureId,
            title = entity.title,
            previewURL = entity.previewURL,
            highQualityURL = entity.highQualityURL,
            description = entity.description
        )
    }
}