package com.example.artphotoframe.core.di


import com.example.artphotoframe.core.data.ApiService
import com.example.artphotoframe.core.data.MetApi
import com.example.artphotoframe.core.data.MetRepository
import com.example.artphotoframe.core.data.SearchPictureFullRepositoryImpl
import com.example.artphotoframe.core.domain.search.SearchPicturesUseCase
import com.example.artphotoframe.core.domain.search.SearchRepository
import com.example.artphotoframe.core.presentation.screens.SearchViewModel
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

val EuropeanaRetrofit = named("europeanaRetrofit")
val MetRetrofit = named("metRetrofit")
val appModule = module {

    //  Настройка Retrofit и OkHttpClient  //
    single {
        OkHttpClient
            .Builder()
            .cache(Cache(File(androidContext().cacheDir, "http"), 50L * 1024 * 1024))
            .build()
    }

    single(EuropeanaRetrofit) {
        Retrofit.Builder()
            .baseUrl("https://api.europeana.eu/record/v2/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single { get<Retrofit>(EuropeanaRetrofit).create(ApiService::class.java) }

    single(MetRetrofit) {
        Retrofit.Builder()
            .baseUrl("https://collectionapi.metmuseum.org/") // Met base
            .client(get()) // same OkHttp
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single<MetApi> { get<Retrofit>(MetRetrofit).create(MetApi::class.java) }


    // Europeana
    // Репозиторий: реализация интерфейса
    single<SearchRepository> { SearchPictureFullRepositoryImpl(get()) }
    // UseCase: зависит от репозитория
    single { SearchPicturesUseCase(get()) }
    // ViewModel: зависит от UseCase
    viewModel { SearchViewModel(get(), get()) }

    // Metropolitan
    single<MetRepository> { MetRepository(get<MetApi>()) }
}