package com.example.playlistmaker.presentation.presenter.settings

class SettingsScreenPresenter(
    private val view: SettingsScreenView
) {

    fun shareApp() {
        view.shareApp()
    }

    fun sendEmailToSupport() {
        view.sendEmailToSupport()
    }

    fun checkOffer() {
        view.checkOffer()
    }
}