package com.example.playlistmaker.presentation.track

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.interactor.MediaPlayerInteractor
import com.example.playlistmaker.domain.interactor.PlaylistInteractor
import com.example.playlistmaker.domain.interactor.TrackDatabaseInteractor
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val trackDatabaseInteractor: TrackDatabaseInteractor,
    private val playlistInteractor: PlaylistInteractor,
) : ViewModel() {

    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState> = _playerState

    private val _isTrackFavorite = MutableLiveData(false)
    val isTrackFavorite: LiveData<Boolean> = _isTrackFavorite

    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> = _playlists

    private val _isAlreadyInPlaylist = MutableLiveData<Pair<String, Boolean>>()
    val isAlreadyInPlaylist: LiveData<Pair<String, Boolean>> = _isAlreadyInPlaylist

    private var timerJob: Job? = null


    fun checkIsFavorite(track: Track) {
        viewModelScope.launch {
            trackDatabaseInteractor.getAllTracks().collect { favoriteTracks ->
                favoriteTracks.forEach {
                    if (it.trackId == track.trackId) {
                        _isTrackFavorite.postValue(true)
                        track.isFavorite = true
                    }
                }
            }
        }

    }

    fun getPlayLists() {
        viewModelScope.launch {
            playlistInteractor.getSavedPlaylists().collect {
                _playlists.postValue(it)
            }
        }
    }

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

    fun onFavoriteClicked(track: Track) {
        viewModelScope.launch {
            if (track.isFavorite)
                trackDatabaseInteractor.deleteTrack(track.trackId)
            else
                trackDatabaseInteractor.addTrack(track)
        }

        track.isFavorite = !track.isFavorite
        _isTrackFavorite.postValue(track.isFavorite)
    }

    fun addTrackToPlayList(track: Track, playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.addTrackToPlayList(track, playlist).collect {
                _isAlreadyInPlaylist.postValue(Pair(playlist.name, it))
            }
        }
    }
}