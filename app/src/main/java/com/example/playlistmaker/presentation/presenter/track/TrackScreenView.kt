package com.example.playlistmaker.presentation.presenter.track

interface TrackScreenView {
    fun setOnMediaPlayerPrepared()
    fun setMediaPlayerOnCompletion()
    fun setOnPlayerStarted()
    fun setOnPlayerPaused()
    fun playbackControl()
    fun setCurrentTime(milliseconds: Long)
}