package com.example.artphotoframe.core.presentation.screens

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artphotoframe.core.data.MetRepository
import com.example.artphotoframe.core.data.models.Picture
import com.example.artphotoframe.core.data.models.metropolitan.toPicture
import com.example.artphotoframe.core.domain.search.SearchPicturesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//ViewModel для управления состоянием экрана SearchScreen
//Использует StateFlow для хранения списка изображений
class SearchViewModel(
    private val getSearchPicturesUseCase: SearchPicturesUseCase,
    private val metRepository: MetRepository
) : ViewModel() {

    private val _pictures = MutableStateFlow<List<Picture>>(emptyList())
    val pictures: StateFlow<List<Picture>> = _pictures

    private val _allPictures = mutableStateOf(emptyList<Picture>())
    val allPictures: State<List<Picture>> = _allPictures

    private var ids: List<Int> = emptyList()
    private var page = 0
    private val pageSize = 20 //добавляем данные по 20 элементов

    fun loadAllPictures() = viewModelScope.launch {
        try {
            ids = metRepository.searchIds("*")
            page = 0
            _pictures.value = emptyList()
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
            ids = metRepository.searchIds(query)
            page = 0
            loadMore()
        }
    }

    fun loadMore() = viewModelScope.launch {
        val start = page * pageSize
        val slice = ids.drop(start).take(pageSize)
        if (slice.isEmpty()) return@launch
        val objs = metRepository.getObjectsBatched(slice)
        _pictures.value = _pictures.value + objs.map { it.toPicture() }
        page++
    }
}