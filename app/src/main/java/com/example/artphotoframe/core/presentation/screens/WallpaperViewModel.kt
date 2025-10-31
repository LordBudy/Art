package com.example.artphotoframe.core.presentation.screens

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
                _ui.value = WallpaperUiState(isLoading = false, message = "Обои установлены")
            } catch (e: SecurityException) {
                _ui.value = WallpaperUiState(isLoading = false, message = "Нет прав на установку обоев")
            } catch (e: Exception) {
                _ui.value = WallpaperUiState(isLoading = false, message = "Не удалось установить обои")
            }
        }
    }

    fun clearMessage() {
        _ui.value = _ui.value.copy(message = null)
    }
}
data class WallpaperUiState(
    val isLoading: Boolean = false,
    val message: String? = null
)