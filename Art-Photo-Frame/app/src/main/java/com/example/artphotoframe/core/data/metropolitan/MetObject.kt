package com.example.artphotoframe.core.data.metropolitan

import com.example.artphotoframe.core.data.models.Picture

data class MetObject(
    val objectID: Int,
    val title: String?,
    val artistDisplayName: String?,
    val artistDisplayBio: String?,
    val primaryImageSmall: String?,
    val primaryImage: String?,
    val objectURL: String?
)

fun MetObject.toPicture(): Picture =
    Picture(
        id = objectID,
        title = title?.ifBlank { null },
        previewURL = primaryImageSmall?.ifBlank { null },
        highQualityURL = primaryImage?.ifBlank { null },
        description = listOfNotNull(
            artistDisplayName?.takeIf { it.isNotBlank() },
            artistDisplayBio?.takeIf { it.isNotBlank() }
        ).joinToString(" — ").ifBlank { null }
    )