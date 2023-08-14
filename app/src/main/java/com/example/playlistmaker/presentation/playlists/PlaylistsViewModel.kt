package com.example.playlistmaker.presentation.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.interactor.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val _state = MutableLiveData<PlaylistsScreenState>()
    val state: LiveData<PlaylistsScreenState> = _state

    fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getSavedPlaylists().collect {
                if (it.isEmpty()) {
                    _state.postValue(PlaylistsScreenState.Empty)
                } else {
                    _state.postValue(PlaylistsScreenState.Filled(it))
                }
            }
        }
    }
}