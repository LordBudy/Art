package com.example.artphotoframe.core.domain.wallpaper



class SetWallpaperUseCase (
    private val repository: WallpaperRepository
) {
    suspend operator fun invoke(data: Any, target: WallpaperTarget) {
        repository.setWallpaper(data, target)
    }
}