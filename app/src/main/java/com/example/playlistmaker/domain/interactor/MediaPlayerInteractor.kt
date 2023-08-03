package com.example.playlistmaker.domain.interactor

import com.example.playlistmaker.domain.repository.MediaPlayerRepository

class MediaPlayerInteractor(
    private val mediaPlayerRepository: MediaPlayerRepository
) {
    fun preparePlayer(
        url: String,
        setOnPrepared: () -> Unit,
        setOnCompletion: () -> Unit
    ) {
        mediaPlayerRepository.preparePlayer(url, setOnPrepared, setOnCompletion)
    }

    fun startPlayer(onPlayerStart: () -> Unit) {
        mediaPlayerRepository.startPlayer(onPlayerStart)
    }

    fun pausePlayer(onPlayerPause: () -> Unit) {
        mediaPlayerRepository.pausePlayer(onPlayerPause)
    }

    fun releaseMediaPlayer() {
        mediaPlayerRepository.releasePlayer()
    }

    fun getCurrentPosition(): Int {
        return mediaPlayerRepository.getCurrentPosition()
    }

    fun isPlaying(): Boolean {
        return mediaPlayerRepository.isPlaying()
    }

}