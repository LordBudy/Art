package com.example.artphotoframe.core.presentation.screens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artphotoframe.core.domain.wallpaper.SetWallpaperUseCase
import com.example.artphotoframe.core.domain.wallpaper.WallpaperTarget
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class WallpaperViewModel (
    private val setWallpaper: SetWallpaperUseCase
) : ViewModel() {

    private val _ui = MutableStateFlow<Wallpaper>(Wallpaper.Loading)
    val ui = _ui.asStateFlow()

    fun apply(data: Any, target: WallpaperTarget) {
        viewModelScope.launch {
            try {
                setWallpaper(data, target)
                _ui.value = Wallpaper.Success
            } catch (e: SecurityException) {
                _ui.value =Wallpaper.PermissionDenied
            } catch (e: Exception) {
                _ui.value = Wallpaper.GenericError(e)
            }
        }
    }

    fun clearMessage() {
        _ui.value = Wallpaper.Loading
    }
}
sealed class Wallpaper {
    object Loading: Wallpaper()
    object Success : Wallpaper()
    object PermissionDenied : Wallpaper()
    data class GenericError(val exception: Exception) : Wallpaper()
}