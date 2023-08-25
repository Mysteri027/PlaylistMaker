package com.example.playlistmaker.di

import com.example.playlistmaker.presentation.create.CreatePlaylistViewModel
import com.example.playlistmaker.presentation.favorite.FavoriteTracksViewModel
import com.example.playlistmaker.presentation.main.MainViewModel
import com.example.playlistmaker.presentation.playlist.PlaylistViewModel
import com.example.playlistmaker.presentation.playlists.PlaylistsViewModel
import com.example.playlistmaker.presentation.search.SearchViewModel
import com.example.playlistmaker.presentation.settings.SettingsViewModel
import com.example.playlistmaker.presentation.track.TrackViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel {
        MainViewModel()
    }

    viewModel {
        SearchViewModel(
            networkIterator = get(),
            localStorageInteractor = get(),
        )
    }

    viewModel {
        SettingsViewModel(
            sharingInteractor = get(),
            settingsInteractor = get()
        )
    }

    viewModel {
        TrackViewModel(
            mediaPlayerInteractor = get(),
            trackDatabaseInteractor = get(),
            playlistInteractor = get()
        )
    }

    viewModel {
        FavoriteTracksViewModel(
            databaseInteractor = get(),
        )
    }

    viewModel {
        PlaylistsViewModel(
            playlistInteractor = get()
        )
    }

    viewModel {
        CreatePlaylistViewModel(
            playlistInteractor = get()
        )
    }

    viewModel {
        PlaylistViewModel(
            playlistInteractor = get(),
            sharingInteractor = get()
        )
    }
}