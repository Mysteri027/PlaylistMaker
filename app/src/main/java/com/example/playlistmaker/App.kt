package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        Container.themeSharedPreferences = getSharedPreferences(
            APP_SHARED_PREFERENCES_KEY, MODE_PRIVATE
        )
        darkTheme = Container.themeSharedPreferences.getBoolean(DART_THEME_KEY, false)
        switchTheme(darkTheme)

        Container.trackHistorySharedPreferences = getSharedPreferences(
            SEARCH_HISTORY_SHARED_PREFERENCES_KEY,
            MODE_PRIVATE
        )
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

        Container.themeSharedPreferences.edit()
            .putBoolean(DART_THEME_KEY, darkTheme)
            .apply()
    }


    companion object {
        const val APP_SHARED_PREFERENCES_KEY = "app_shared_preferences"
        const val SEARCH_HISTORY_SHARED_PREFERENCES_KEY = "SEARCH_HISTORY_SHARED_PREFERENCES_KEY"
        const val DART_THEME_KEY = "dart_theme_key"
    }
}

object Container {
    lateinit var trackHistorySharedPreferences: SharedPreferences
    lateinit var themeSharedPreferences: SharedPreferences
}