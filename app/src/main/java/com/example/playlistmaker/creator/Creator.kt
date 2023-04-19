package com.example.playlistmaker.creator

import android.content.SharedPreferences
import android.media.MediaPlayer
import com.example.playlistmaker.Container
import com.example.playlistmaker.data.network.ITunesSearchAPIService
import com.example.playlistmaker.data.repository.LocalStorageRepositoryImpl
import com.example.playlistmaker.data.repository.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.repository.NetworkRepositoryImpl
import com.example.playlistmaker.data.searchhistory.SearchHistory
import com.example.playlistmaker.domain.interactor.LocalStorageInteractor
import com.example.playlistmaker.domain.interactor.MediaPlayerInteractor
import com.example.playlistmaker.domain.interactor.NetworkInteractor
import com.example.playlistmaker.domain.repository.MediaPlayerRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://itunes.apple.com"

object Creator {

    private fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun provideItunesApi(retrofit: Retrofit): ITunesSearchAPIService {
        return retrofit.create(ITunesSearchAPIService::class.java)
    }

    private fun provideNetworkRepository(): NetworkRepositoryImpl {
        return NetworkRepositoryImpl(
            iTunesSearchAPIService = provideItunesApi(provideRetrofit())
        )
    }


    private fun provideSearchHistory(sharedPreferences: SharedPreferences): SearchHistory {
        return SearchHistory(sharedPreferences)
    }

    private fun provideLocalStorageRepository(): LocalStorageRepositoryImpl {
        return LocalStorageRepositoryImpl(
            searchHistory = provideSearchHistory(sharedPreferences = Container.trackHistorySharedPreferences)
        )
    }

    fun provideNetworkInteractor(): NetworkInteractor {
        return NetworkInteractor(
            networkRepository = provideNetworkRepository()
        )
    }

    fun provideLocalStorageInteractor(): LocalStorageInteractor {
        return LocalStorageInteractor(localStorageRepository = provideLocalStorageRepository())
    }

    private fun provideMediaPlayer(): MediaPlayer {
        return MediaPlayer()
    }

    private fun provideMediaPlayerRepository(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl(
            mediaPlayer = provideMediaPlayer()
        )
    }

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractor(mediaPlayerRepository = provideMediaPlayerRepository())
    }
}