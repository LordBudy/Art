package com.example.artphotoframe.core.data

import com.example.artphotoframe.core.data.models.metropolitan.MetObject
import com.example.artphotoframe.core.data.models.metropolitan.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MetApi {
    @GET("public/collection/v1/search")
    suspend fun search(
        @Query("q") q: String,
        @Query("hasImages") hasImages: Boolean = true,
        @Query("departmentId") departmentId: Int? = null
    ): SearchResponse

    @GET("public/collection/v1/objects/{id}")
    suspend fun objectDetails(@Path("id") id: Int): MetObject
}