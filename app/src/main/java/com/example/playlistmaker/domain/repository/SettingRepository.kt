package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.model.ThemeSettings

interface SettingRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}