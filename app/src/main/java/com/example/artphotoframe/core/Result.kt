package com.example.artphotoframe.core

sealed class Result <out T> {
    object Loading : Result<Nothing>()
    data class Success<T>(val data: T) : Result<T>()
    data class Error<T>(val error: T) : Result<T>()
}