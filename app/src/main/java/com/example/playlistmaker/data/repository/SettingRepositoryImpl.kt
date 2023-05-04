package com.example.playlistmaker.data.repository

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.model.ThemeSettings
import com.example.playlistmaker.domain.repository.SettingRepository

class SettingRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : SettingRepository {
    override fun getThemeSettings(): ThemeSettings {
        val isDarkThemeEnabled = sharedPreferences.getBoolean(DART_THEME_KEY, false)
        return ThemeSettings(isDarkThemeEnabled)
    }

    override fun updateThemeSetting(settings: ThemeSettings) {

        AppCompatDelegate.setDefaultNightMode(
            if (settings.isDarkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

        sharedPreferences.edit()
            .putBoolean(DART_THEME_KEY, settings.isDarkThemeEnabled)
            .apply()

    }

    companion object {
        const val DART_THEME_KEY = "dart_theme_key"
    }
}