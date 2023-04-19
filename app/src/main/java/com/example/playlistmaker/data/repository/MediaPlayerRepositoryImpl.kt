package com.example.playlistmaker.data.repository

import android.media.MediaPlayer
import com.example.playlistmaker.domain.repository.MediaPlayerRepository

class MediaPlayerRepositoryImpl(
    private val mediaPlayer: MediaPlayer
): MediaPlayerRepository {

    override fun preparePlayer(
        url: String,
        setOnPrepared: () -> Unit,
        setOnCompletion: () -> Unit
    ) {
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
        mediaPlayer.release()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }
}