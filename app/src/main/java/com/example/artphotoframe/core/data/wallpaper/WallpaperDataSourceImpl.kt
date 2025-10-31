package com.example.artphotoframe.core.data.wallpaper

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WallpaperDataSourceImpl(
    private val appContext: Context
) : WallpaperDataSource {

    override suspend fun loadBitmap(data: Any): Bitmap = withContext(Dispatchers.IO) {
        val target = resolveTargetSize() // =  W x H экрана (без параллакса)

        // Подсказываем лаунчеру желаемый размер
        WallpaperManager.getInstance(appContext)
            .suggestDesiredDimensions(target.w, target.h)

        // Просим Coil отдать картинку сразу нужного размера
        val loader = ImageLoader(appContext)
        val request = ImageRequest.Builder(appContext)
            .data(data)                                 // String(URL)/Uri/File
            .allowHardware(false)               // software Bitmap
            .size(target.w, target.h)   // ВАЖНО: декодируем примерно под экран
            .build()

        val result = loader.execute(request)
        val drawable = (result as? SuccessResult)?.drawable
            ?: error("Не удалось загрузить изображение для обоев")

        val raw = (drawable as? BitmapDrawable)?.bitmap ?: drawable.toBitmap()

        // Доводим до точного размера экрана (center-crop без искажений)
        centerCropTo(raw, target)
    }


    override suspend fun setHome(bitmap: Bitmap) {
        withContext(Dispatchers.IO) {
            val wm = WallpaperManager.getInstance(appContext)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                wm.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM)
            } else {
                wm.setBitmap(bitmap)
            }
        }
    }

    override suspend fun setLock(bitmap: Bitmap) {
        withContext(Dispatchers.IO) {
            val wm = WallpaperManager.getInstance(appContext)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                wm.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK)
            } else {
                wm.setBitmap(bitmap)
            }
        }
    }

    override suspend fun setBoth(bitmap: Bitmap) {
        withContext(Dispatchers.IO) {
            val wm = WallpaperManager.getInstance(appContext)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                wm.setBitmap(
                    bitmap, null, true,
                    WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK
                )
            } else {
                wm.setBitmap(bitmap)
            }
        }
    }

    private data class Size(val w: Int, val h: Int)

    private fun getScreenSize(): Size {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val wm = appContext.getSystemService(android.view.WindowManager::class.java)
            val b = wm.currentWindowMetrics.bounds
            Size(b.width(), b.height())
        } else {
            val dm = appContext.resources.displayMetrics
            Size(dm.widthPixels, dm.heightPixels)
        }
    }


     // target = размер экрана (без параллакса).
     //Если понадобится параллакс — умножить ширину на 2.

    private fun resolveTargetSize(parallax: Boolean = false): Size {
        val s = getScreenSize()
        return if (parallax) Size(s.w * 2, s.h) else Size(s.w, s.h)
    }

   //Центр-кроп под target без искажения пропорций
    private fun centerCropTo(src: Bitmap, target: Size): Bitmap {
        val tw = target.w
        val th = target.h
        if (src.width == tw && src.height == th) return src

        val scale = maxOf(
            tw.toFloat() / src.width.toFloat(),
            th.toFloat() / src.height.toFloat()
        )

        val scaledW = (src.width * scale).toInt().coerceAtLeast(1)
        val scaledH = (src.height * scale).toInt().coerceAtLeast(1)

        val matrix = Matrix().apply { postScale(scale, scale) }
        val scaled = Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)

        val x = ((scaledW - tw) / 2).coerceAtLeast(0)
        val y = ((scaledH - th) / 2).coerceAtLeast(0)

        val cropW = minOf(tw, scaled.width)
        val cropH = minOf(th, scaled.height)

        // Защита от выхода за границы
        val safeX = x.coerceIn(0, (scaled.width - cropW).coerceAtLeast(0))
        val safeY = y.coerceIn(0, (scaled.height - cropH).coerceAtLeast(0))

        return Bitmap.createBitmap(scaled, safeX, safeY, cropW, cropH)
    }
}