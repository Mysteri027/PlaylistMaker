package com.example.playlistmaker.presentation.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.interactor.TrackDatabaseInteractor
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val databaseInteractor: TrackDatabaseInteractor,
) : ViewModel() {

    private val _state = MutableLiveData<FavoriteTracksViewState>()
    val state: LiveData<FavoriteTracksViewState> = _state

    init {
        updateState()
    }

    private fun setState(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            _state.postValue(FavoriteTracksViewState.Empty)
        } else {
            _state.postValue(FavoriteTracksViewState.Content(tracks))
        }
    }

    fun updateState() {
        viewModelScope.launch {
            databaseInteractor.getAllTracks().collect { result ->
                setState(result)
            }
        }
    }
}