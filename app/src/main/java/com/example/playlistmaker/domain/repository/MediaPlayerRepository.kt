package com.example.playlistmaker.domain.repository

interface MediaPlayerRepository {

    fun preparePlayer(
        url: String,
        setOnPrepared: () -> Unit,
        setOnCompletion: () -> Unit
    )

    fun startPlayer(onPlayerStart: () -> Unit)

    fun pausePlayer(onPlayerPause: () -> Unit)

    fun releasePlayer()

    fun getCurrentPosition(): Int

    fun isPlaying(): Boolean
}