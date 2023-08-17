package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.domainModule
import com.example.playlistmaker.di.presentationModule
import com.markodevcic.peko.PermissionRequester
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        val themeSharedPreferences = getSharedPreferences(
            APP_SHARED_PREFERENCES_KEY, MODE_PRIVATE
        )

        darkTheme = themeSharedPreferences.getBoolean(DART_THEME_KEY, false)
        switchTheme(darkTheme, themeSharedPreferences)

        startKoin {
            androidLogger(level = Level.DEBUG)
            androidContext(this@App)
            modules(
                listOf(presentationModule, dataModule, domainModule)
            )
        }

        PermissionRequester.initialize(applicationContext)
    }

    private fun switchTheme(
        darkThemeEnabled: Boolean,
        themeSharedPreferences: SharedPreferences
    ) {
        darkTheme = darkThemeEnabled

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

        themeSharedPreferences.edit()
            .putBoolean(DART_THEME_KEY, darkTheme)
            .apply()
    }


    companion object {
        const val APP_SHARED_PREFERENCES_KEY = "app_shared_preferences"
        const val DART_THEME_KEY = "dart_theme_key"
    }
}