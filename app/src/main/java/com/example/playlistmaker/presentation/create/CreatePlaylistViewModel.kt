package com.example.playlistmaker.presentation.create

import android.Manifest
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.interactor.PlaylistInteractor
import com.example.playlistmaker.domain.model.Playlist
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val _state = MutableLiveData<CreatePlaylistScreenState>(CreatePlaylistScreenState.Empty)
    val state: LiveData<CreatePlaylistScreenState> = _state

    private val _permissionState = MutableLiveData<PermissionResult>()
    val permissionState: LiveData<PermissionResult> = _permissionState

    private val register = PermissionRequester.instance()

    fun setEmptyState() {
        _state.postValue(CreatePlaylistScreenState.Empty)
    }

    fun setFilledState() {
        _state.postValue(CreatePlaylistScreenState.Filled)
    }

    fun onPlaylistCoverClicked() {
        viewModelScope.launch {
            if (Build.VERSION.SDK_INT > 33) {
                register.request(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                register.request(Manifest.permission.READ_EXTERNAL_STORAGE)
            }.collect { result ->
                _permissionState.postValue(result)
            }
        }
    }

    fun savePlayList(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.addPlaylist(playlist)
        }
    }
}