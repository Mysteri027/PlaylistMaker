package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    private var darkTheme = false

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()

        sharedPreferences = getSharedPreferences(
            APP_SHARED_PREFERENCES_KEY, MODE_PRIVATE
        )
        darkTheme = sharedPreferences.getBoolean(DART_THEME_KEY, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled

        AppCompatDelegate.setDefaultNightMode(
            if(darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

        sharedPreferences.edit()
            .putBoolean(DART_THEME_KEY, darkTheme)
            .apply()
    }


    companion object {
        const val APP_SHARED_PREFERENCES_KEY = "app_shared_preferences"
        const val DART_THEME_KEY = "dart_theme_key"
    }
}