package com.example.playlistmaker.data.repository

import android.media.MediaPlayer
import android.util.Log
import com.example.playlistmaker.domain.repository.MediaPlayerRepository

class MediaPlayerRepositoryImpl(
    private val mediaPlayer: MediaPlayer
): MediaPlayerRepository {

    override fun preparePlayer(
        url: String,
        setOnPrepared: () -> Unit,
        setOnCompletion: () -> Unit
    ) {
        Log.d("preparePlayer url", url)
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()

        mediaPlayer.setOnPreparedListener {
            setOnPrepared.invoke()
        }

        mediaPlayer.setOnCompletionListener {
            setOnCompletion.invoke()
        }
    }

    override fun startPlayer(onPlayerStart: () -> Unit) {
        mediaPlayer.start()
        onPlayerStart.invoke()
    }

    override fun pausePlayer(onPlayerPause: () -> Unit) {
        mediaPlayer.pause()
        onPlayerPause.invoke()
    }

    override fun releasePlayer() {
        mediaPlayer.reset()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }
}