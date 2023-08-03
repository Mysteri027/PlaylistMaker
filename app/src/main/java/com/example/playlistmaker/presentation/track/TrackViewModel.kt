package com.example.playlistmaker.presentation.track

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.interactor.MediaPlayerInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor
) : ViewModel() {

    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState> = _playerState

    private var timerJob: Job? = null

    fun preparePlayer(url: String) {
        mediaPlayerInteractor.preparePlayer(
            url,
            setOnPrepared = { _playerState.postValue(PlayerState.Prepared()) },
            setOnCompletion = {
                _playerState.postValue(PlayerState.Prepared())
                timerJob?.cancel()
            }
        )
    }

    private fun startPlayer() {
        mediaPlayerInteractor.startPlayer {
            _playerState.postValue(PlayerState.Playing(getCurrentPosition()))
            startTimer()
        }
    }

    fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer {
            _playerState.postValue(PlayerState.Paused(getCurrentPosition()))
            timerJob?.cancel()
        }
    }

    fun releasePlayer() {
        mediaPlayerInteractor.releaseMediaPlayer()
    }

    private fun getCurrentPosition(): String {
        return SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(mediaPlayerInteractor.getCurrentPosition()) ?: "00:00"

    }

    fun getTrackImage(url: String): String {
        return url.replaceAfterLast('/', "512x512bb.jpg")
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (mediaPlayerInteractor.isPlaying()) {
                delay(300L)
                _playerState.postValue(PlayerState.Playing(getCurrentPosition()))
            }
        }
    }

    fun onPlayButtonClicked() {
        when (playerState.value) {
            is PlayerState.Playing -> {
                pausePlayer()
            }

            is PlayerState.Prepared, is PlayerState.Paused -> {
                startPlayer()
            }

            else -> {}
        }
    }
}