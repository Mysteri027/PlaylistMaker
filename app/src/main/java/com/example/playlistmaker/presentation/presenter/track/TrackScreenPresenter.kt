package com.example.playlistmaker.presentation.presenter.track

import com.example.playlistmaker.domain.interactor.MediaPlayerInteractor

class TrackScreenPresenter(
    private val view: TrackScreenView,
    private val mediaPlayerInteractor: MediaPlayerInteractor,
) {

    fun preparePlayer(url: String) {
        mediaPlayerInteractor.preparePlayer(
            url,
            setOnPrepared = { view.setOnMediaPlayerPrepared() },
            setOnCompletion = { view.setMediaPlayerOnCompletion() }
        )
    }

    fun startPlayer() {
        mediaPlayerInteractor.startPlayer {
            view.setOnPlayerStarted()
        }
    }

    fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer {
            view.setOnPlayerPaused()
        }
    }

    fun releasePlayer() {
        mediaPlayerInteractor.releaseMediaPlayer()
    }

    fun playbackControl() {
        view.playbackControl()
    }

    fun getCurrentPosition(): Int {
        return mediaPlayerInteractor.getCurrentPosition()
    }

    fun getTrackImage(url: String): String {
        return url.replaceAfterLast('/', "512x512bb.jpg")
    }
}