package com.example.playlistmaker.presentation.main

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModel(
    private val navigator: Navigator
) : ViewModel() {

    fun openSearchScreen(intent: Intent) {
        navigator.startActivity(intent)
    }

    fun openMediaLibraryScreen(intent: Intent) {
        navigator.startActivity(intent)
    }

    fun openSettingsScreen(intent: Intent) {
        navigator.startActivity(intent)
    }
}

class MainViewModelFactory(
    private val navigator: Navigator
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(navigator = navigator) as T
    }
}