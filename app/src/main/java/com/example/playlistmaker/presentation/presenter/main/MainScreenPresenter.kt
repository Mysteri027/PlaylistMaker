package com.example.playlistmaker.presentation.presenter.main

class MainScreenPresenter(
    private val view: MainScreenView
) {

    fun openSearchScreen() {
        view.openSearchScreen()
    }

    fun openMediaLibraryScreen() {
        view.openMediaLibraryScreen()
    }

    fun openSettingsScreen() {
        view.openSettingsScreen()
    }
}