package com.example.playlistmaker.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.interactor.LocalStorageInteractor
import com.example.playlistmaker.domain.interactor.NetworkInteractor
import com.example.playlistmaker.domain.model.Track

class SearchViewModel(
    private val networkIterator: NetworkInteractor,
    private val localStorageInteractor: LocalStorageInteractor,
) : ViewModel() {

    private val _screenState = MutableLiveData<SearchScreenState>()
    val screenState: LiveData<SearchScreenState> = _screenState

    private val _trackHistory = MutableLiveData(getTrackHistoryList())
    val trackHistory: LiveData<List<Track>> = _trackHistory

    init {
        _screenState.postValue(SearchScreenState.History(getTrackHistoryList()))
        getTrackHistoryList()
    }

    fun findTracks(query: String) {
        _screenState.postValue(SearchScreenState.Loading)
        networkIterator.getTracks(
            query = query,
            onSuccess = { tracks ->

                if (tracks.isEmpty()) {
                    _screenState.postValue(SearchScreenState.Error(ErrorType.NOT_FOUND))
                } else {
                    _screenState.postValue(SearchScreenState.Content(tracks))
                }
            },
            onError = {
                _screenState.postValue(SearchScreenState.Error(ErrorType.NETWORK_ERROR))
            }
        )
    }

    fun addTrackToHistory(track: Track) {
        localStorageInteractor.addTrack(track)
        _trackHistory.postValue(getTrackHistoryList())
    }

    fun clearSearchHistory() {
        localStorageInteractor.clear()
        _trackHistory.postValue(getTrackHistoryList())
    }

    fun getTrackHistoryList(): List<Track> {
        return localStorageInteractor.getSearchHistory()
    }
}
