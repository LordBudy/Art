package com.example.artphotoframe.core.data.wallpaper

import com.example.artphotoframe.core.domain.wallpaper.WallpaperRepository
import com.example.artphotoframe.core.domain.wallpaper.WallpaperTarget

//Мост между Domain и Data:
//  Domain вызывает repository.setWallpaper(...),
// тут уже решаем, как загрузить Bitmap и куда его применить

class WallpaperRepositoryImpl (
    private val ds: WallpaperDataSource
) : WallpaperRepository {

    override suspend fun setWallpaper(data: Any, target: WallpaperTarget) {
        val bmp = ds.loadBitmap(data)
        when (target) {
            WallpaperTarget.HOME -> ds.setHome(bmp)
            WallpaperTarget.LOCK -> ds.setLock(bmp)
            WallpaperTarget.BOTH -> ds.setBoth(bmp)
        }
    }
}