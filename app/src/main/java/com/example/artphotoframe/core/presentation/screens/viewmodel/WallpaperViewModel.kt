package com.example.artphotoframe.core.presentation.screens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artphotoframe.core.domain.wallpaper.SetWallpaperUseCase
import com.example.artphotoframe.core.domain.wallpaper.WallpaperTarget
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.artphotoframe.core.Result

class WallpaperViewModel (
    private val setWallpaper: SetWallpaperUseCase
) : ViewModel() {

    private val _ui = MutableStateFlow<Result<Wallpaper>>(Result.Loading)
    val ui = _ui.asStateFlow()

    fun apply(data: Any, target: WallpaperTarget) {
        viewModelScope.launch {
            _ui.value = Result.Loading
            try {
                setWallpaper(data, target)
                _ui.value = Result.Success(Wallpaper.Success)
            } catch (e: SecurityException) {
                _ui.value = Result.Error(Wallpaper.PermissionDenied)
            } catch (e: Exception) {
                _ui.value = Result.Error(Wallpaper.Generic)
            }
        }
    }

    fun clearMessage() {
        _ui.value = Result.Loading
    }
}
sealed class Wallpaper {
    object Success : Wallpaper()
    object PermissionDenied : Wallpaper()
    object Generic : Wallpaper()
}