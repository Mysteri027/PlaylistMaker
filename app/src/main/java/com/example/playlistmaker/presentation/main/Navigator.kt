package com.example.playlistmaker.presentation.main

import android.content.Context
import android.content.Intent

class Navigator(
    private val context: Context,
) {
    fun startActivity(intent: Intent) {
        context.startActivity(intent)
    }
}