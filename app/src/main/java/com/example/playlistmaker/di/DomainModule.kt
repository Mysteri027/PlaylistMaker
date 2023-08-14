package com.example.playlistmaker.di

import com.example.playlistmaker.domain.interactor.LocalStorageInteractor
import com.example.playlistmaker.domain.interactor.MediaPlayerInteractor
import com.example.playlistmaker.domain.interactor.NetworkInteractor
import com.example.playlistmaker.domain.interactor.PlaylistInteractor
import com.example.playlistmaker.domain.interactor.SettingsInteractor
import com.example.playlistmaker.domain.interactor.SharingInteractor
import com.example.playlistmaker.domain.interactor.TrackDatabaseInteractor
import org.koin.dsl.module

val domainModule = module {
    factory {
        LocalStorageInteractor(
            localStorageRepository = get(),
        )
    }

    factory {
        MediaPlayerInteractor(
            mediaPlayerRepository = get()
        )
    }

    factory {
        NetworkInteractor(
            networkRepository = get()
        )
    }

    factory {
        SettingsInteractor(
            settingsRepository = get()
        )
    }

    factory {
        SharingInteractor(
            externalNavigatorRepository = get()
        )
    }

    factory {
        TrackDatabaseInteractor(
            databaseRepository = get()
        )
    }

    factory {
        PlaylistInteractor(
            playlistRepository = get()
        )
    }
}