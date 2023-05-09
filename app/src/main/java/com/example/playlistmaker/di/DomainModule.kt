package com.example.playlistmaker.di

import com.example.playlistmaker.domain.interactor.LocalStorageInteractor
import com.example.playlistmaker.domain.interactor.MediaPlayerInteractor
import com.example.playlistmaker.domain.interactor.NetworkInteractor
import com.example.playlistmaker.domain.interactor.SettingsInteractor
import com.example.playlistmaker.domain.interactor.SharingInteractor
import org.koin.dsl.module

val domainModule = module {
    factory {
        LocalStorageInteractor(
            localStorageRepository = get()
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
}