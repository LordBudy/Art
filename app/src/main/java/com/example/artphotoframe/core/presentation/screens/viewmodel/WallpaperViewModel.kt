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

    private val _ui = MutableStateFlow(WallpaperUiState())
    val ui = _ui.asStateFlow()

    fun apply(data: Any, target: WallpaperTarget) {
        viewModelScope.launch {
            _ui.value = WallpaperUiState(isLoading = true)
            try {
                setWallpaper(data, target)
                _ui.value = WallpaperUiState(result = WallpaperResult.SUCCESS)
            } catch (e: SecurityException) {
                _ui.value = WallpaperUiState(result = WallpaperResult.PERMISSION_DENIED)
            } catch (e: Exception) {
                _ui.value = WallpaperUiState(result = WallpaperResult.ERROR)
            }
        }
    }

    fun clearMessage() {
        _ui.value = WallpaperUiState()
    }
}
data class WallpaperUiState(
    val isLoading: Boolean = false,
    val result: WallpaperResult? = null
)
enum class WallpaperResult { SUCCESS, PERMISSION_DENIED, ERROR }