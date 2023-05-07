package com.example.playlistmaker.presentation.track

sealed class PlayerState {
    object Started : PlayerState()
    object Paused : PlayerState()
    object Prepared : PlayerState()
}
