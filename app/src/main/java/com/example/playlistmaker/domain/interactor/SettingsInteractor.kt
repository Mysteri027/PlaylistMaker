package com.example.playlistmaker.domain.interactor

import com.example.playlistmaker.domain.model.ThemeSettings
import com.example.playlistmaker.domain.repository.SettingRepository

class SettingsInteractor(
    private val settingsRepository: SettingRepository
) {

    fun getThemeSettings(): ThemeSettings {
        return settingsRepository.getThemeSettings()
    }

    fun updateThemeSetting(settings: ThemeSettings) {
        settingsRepository.updateThemeSetting(settings)
    }
}