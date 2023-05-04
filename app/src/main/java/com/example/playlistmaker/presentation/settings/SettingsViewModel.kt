package com.example.playlistmaker.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.domain.interactor.SettingsInteractor
import com.example.playlistmaker.domain.interactor.SharingInteractor
import com.example.playlistmaker.domain.model.EmailData
import com.example.playlistmaker.domain.model.ThemeSettings

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {

    fun getThemeSettings() : Boolean {
        return settingsInteractor.getThemeSettings().isDarkThemeEnabled
    }

    fun switchTheme(isChecked: Boolean) {
        settingsInteractor.updateThemeSetting(ThemeSettings(isDarkThemeEnabled = isChecked))
    }

    fun shareApp(link: String) {
        sharingInteractor.shareApp(link)
    }

    fun sendEmailToSupport(email: String, subject: String) {
        sharingInteractor.openSupport(EmailData(email, subject))
    }

    fun checkOffer(link: String) {
        sharingInteractor.openTerms(link)
    }
}

class SettingsViewModelFactory(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(sharingInteractor, settingsInteractor) as T
    }

}