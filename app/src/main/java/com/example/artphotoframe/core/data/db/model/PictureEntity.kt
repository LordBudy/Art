package com.example.artphotoframe.core.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pictures")
data class PictureEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "picture_id")
    val pictureId: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "preview_url")
    val previewURL: String,

    @ColumnInfo(name = "high_quality_url")
    val highQualityURL: String,

    @ColumnInfo(name = "description")
    val description: String
)