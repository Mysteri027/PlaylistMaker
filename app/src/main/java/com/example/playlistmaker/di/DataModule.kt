package com.example.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.example.playlistmaker.data.network.ITunesSearchAPIService
import com.example.playlistmaker.data.network.NetworkClient
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.repository.ExternalNavigatorRepositoryImpl
import com.example.playlistmaker.data.repository.LocalStorageRepositoryImpl
import com.example.playlistmaker.data.repository.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.repository.NetworkRepositoryImpl
import com.example.playlistmaker.data.repository.SettingRepositoryImpl
import com.example.playlistmaker.data.searchhistory.SearchHistory
import com.example.playlistmaker.domain.repository.ExternalNavigatorRepository
import com.example.playlistmaker.domain.repository.LocalStorageRepository
import com.example.playlistmaker.domain.repository.MediaPlayerRepository
import com.example.playlistmaker.domain.repository.NetworkRepository
import com.example.playlistmaker.domain.repository.SettingRepository
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val BASE_URL = "https://itunes.apple.com"
const val APP_SHARED_PREFERENCES_KEY = "app_shared_preferences"
const val SEARCH_HISTORY_SHARED_PREFERENCES_KEY = "SEARCH_HISTORY_SHARED_PREFERENCES_KEY"

val dataModule = module {

    factory<ExternalNavigatorRepository> {
        ExternalNavigatorRepositoryImpl(
            context = get()
        )
    }

    factory<LocalStorageRepository> {
        LocalStorageRepositoryImpl(
            searchHistory = get()
        )
    }

    factory<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(
            mediaPlayer = get()
        )
    }

    factory<NetworkRepository> {
        NetworkRepositoryImpl(
            networkClient = get()
        )
    }

    factory<NetworkClient> {
        RetrofitNetworkClient(iTunesSearchAPIService = get())
    }

    factory<SettingRepository> {
        SettingRepositoryImpl(
            sharedPreferences = get()
        )
    }

    factory {
        SearchHistory(
            sharedPreferences = get()
        )
    }

    single {
        provideTrackHistorySharedPreferences(context = get())
    }

    single {
        provideThemeSharedPreferences(context = get())
    }

    factory {
        provideItunesApi(retrofit = get())
    }

    single {
        provideRetrofit()
    }

    factory {
        MediaPlayer()
    }
}

fun provideTrackHistorySharedPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences(
        SEARCH_HISTORY_SHARED_PREFERENCES_KEY,
        Context.MODE_PRIVATE
    )
}

fun provideThemeSharedPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences(
        APP_SHARED_PREFERENCES_KEY,
        Context.MODE_PRIVATE
    )
}

fun provideItunesApi(retrofit: Retrofit): ITunesSearchAPIService {
    return retrofit.create(ITunesSearchAPIService::class.java)
}

fun provideRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}