package com.example.playlistmaker.presentation.track

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.interactor.MediaPlayerInteractor

class TrackViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor
) : ViewModel() {

    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState> = _playerState

    private val _isMediaPlayerComplete = MutableLiveData(false)
    val isMediaPlayerComplete: LiveData<Boolean> = _isMediaPlayerComplete

    fun preparePlayer(url: String) {
        mediaPlayerInteractor.preparePlayer(
            url,
            setOnPrepared = { _playerState.postValue(PlayerState.Prepared) },
            setOnCompletion = { _isMediaPlayerComplete.postValue(true) }
        )
    }

    fun startPlayer() {
        mediaPlayerInteractor.startPlayer {
            _playerState.postValue(PlayerState.Started)
        }
    }

    fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer {
            _playerState.postValue(PlayerState.Paused)
        }
    }

    fun releasePlayer() {
        mediaPlayerInteractor.releaseMediaPlayer()
    }

    fun getCurrentPosition(): Int {
        return mediaPlayerInteractor.getCurrentPosition()
    }

    fun getTrackImage(url: String): String {
        return url.replaceAfterLast('/', "512x512bb.jpg")
    }

    fun setStateToPrepared() {
        _playerState.postValue(PlayerState.Prepared)
        _isMediaPlayerComplete.postValue(false)
    }
}