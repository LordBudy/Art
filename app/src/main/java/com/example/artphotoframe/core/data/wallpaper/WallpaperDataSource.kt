package com.example.artphotoframe.core.data.wallpaper

import android.graphics.Bitmap
//интерфейс работает с Android/Coil/Bitmap
interface WallpaperDataSource {
    suspend fun loadBitmap(data: Any): Bitmap
    suspend fun setHome(bitmap: Bitmap)
    suspend fun setLock(bitmap: Bitmap)
    suspend fun setBoth(bitmap: Bitmap)
}