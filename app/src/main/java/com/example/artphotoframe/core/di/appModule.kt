package com.example.artphotoframe.core.di


import com.example.artphotoframe.core.data.ApiService
import com.example.artphotoframe.core.data.SearchPictureFullRepositoryImpl
import com.example.artphotoframe.core.domain.search.SearchPicturesUseCase
import com.example.artphotoframe.core.domain.search.SearchRepository
import com.example.artphotoframe.core.presentation.viewModels.SearchViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

                //  Настройка Retrofit и OkHttpClient  //

    single {
        OkHttpClient.Builder().build()
    }

    single {
        Retrofit.Builder()
            // Базовый URL без query
            .baseUrl("https://api.europeana.eu/record/v2/")
            // Вставляем OkHttpClient
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    // ApiService: создаём через Retrofit
    single { get<Retrofit>().create(ApiService::class.java) }

                 // Регистрация компонентов  //

    // Репозиторий: реализация интерфейса
    single<SearchRepository> { SearchPictureFullRepositoryImpl(get()) }
    // UseCase: зависит от репозитория
    single { SearchPicturesUseCase(get()) }
    // ViewModel: зависит от UseCase
    viewModel { SearchViewModel(get()) }

}


