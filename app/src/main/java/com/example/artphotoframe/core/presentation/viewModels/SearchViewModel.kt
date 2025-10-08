package com.example.artphotoframe.core.presentation.viewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artphotoframe.core.data.models.Picture
import com.example.artphotoframe.core.domain.search.SearchPicturesUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//ViewModel для управления состоянием экрана SearchScreen
//Использует StateFlow для хранения списка изображений
class SearchViewModel(
    private val getSearchPicturesUseCase: SearchPicturesUseCase
) : ViewModel() {

    private val _pictures = MutableStateFlow<List<Picture>>(emptyList())
    val pictures: StateFlow<List<Picture>> = _pictures

    private val _allPictures = mutableStateOf(emptyList<Picture>())
    val allPictures: State<List<Picture>> = _allPictures

    init {
        loadAllPictures() // Загружаем все картины при создании ViewModel
    }

    private fun loadAllPictures() = viewModelScope.launch {
        try {
            // Здесь нужно определить, как получить все картины
            // Например, можно сделать общий запрос без параметров
            val result = getSearchPicturesUseCase("*") // или другой универсальный запрос
            _allPictures.value = result
            _pictures.value = result
        } catch (e: Exception) {
            Log.e("SearchViewModel", "Ошибка загрузки всех изображений: ${e.message}", e)
        }
    }

    //Дебаунс (Debounce) - задержка перед отправкой запроса
    fun searchPictures(query: String) = viewModelScope.launch {
        // Задержка в 300 мс
        delay(300)
        try {
            val result = getSearchPicturesUseCase(query)
            _pictures.value = result
        } catch (e: Exception) {
            Log.e("SearchViewModel", "Ошибка поиска изображений: ${e.message}", e)
        }
    }
}