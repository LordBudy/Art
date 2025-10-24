package com.example.artphotoframe.core.presentation.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artphotoframe.core.data.models.Picture
import com.example.artphotoframe.core.domain.search.SearchPicturesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val getSearchPicturesUseCase: SearchPicturesUseCase
) : ViewModel() {

    private val _pictures = MutableStateFlow<List<Picture>>(emptyList())
    val pictures: StateFlow<List<Picture>> = _pictures

    private val _favorites = MutableStateFlow<List<Picture>>(emptyList())  // Или загружай из репозитория
    val favorites: StateFlow<List<Picture>> = _favorites.asStateFlow()

    fun isFavorite(picture: Picture): Boolean {
        return favorites.value.any { it.id == picture.id }
    }
    private var page = 0
    private val pageSize = 20 //добавляем данные по 20 элементов
    private var currentQuery: String = "*"
    fun loadAllPictures() = viewModelScope.launch {
        try {
            page = 0
            _pictures.value = emptyList()
            currentQuery = "*"
            loadMore()
        } catch (e: Exception) {
            Log.e("SearchViewModel", "Ошибка загрузки всех изображений: ${e.message}", e)
        }
    }

    //(Debounce) - задержка перед отправкой запроса
    var searchJob: Job? = null
    fun searchPictures(query: String) {
        if (searchJob?.isActive == true) {
            searchJob?.cancel()
        }
        searchJob = viewModelScope.launch {

            delay(300)
            _pictures.value = emptyList()
            page = 0
            currentQuery = query
            loadMore()
        }
    }

    fun loadMore() = viewModelScope.launch {
        val newPictures = getSearchPicturesUseCase(currentQuery, page, pageSize)

        if (newPictures.isNotEmpty()) {
            _pictures.value = _pictures.value + newPictures
            page++
        }
    }
}