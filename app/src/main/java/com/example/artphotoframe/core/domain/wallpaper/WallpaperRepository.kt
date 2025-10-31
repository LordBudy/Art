package com.example.artphotoframe.core.domain.wallpaper

interface WallpaperRepository {
    suspend fun setWallpaper(data: Any, target: WallpaperTarget)
}