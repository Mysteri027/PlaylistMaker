package com.example.playlistmaker.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.interactor.LocalStorageInteractor
import com.example.playlistmaker.domain.interactor.NetworkInteractor
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.launch

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

        viewModelScope.launch {
            networkIterator.getTracks(query).collect { result ->
                if (result.second != null) {
                    _screenState.postValue(SearchScreenState.Error(ErrorType.NETWORK_ERROR))
                } else if (result.first!!.isEmpty()) {
                    _screenState.postValue(SearchScreenState.Error(ErrorType.NOT_FOUND))
                } else {
                    _screenState.postValue(SearchScreenState.Content(result.first!!))
                }
            }
        }
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

    fun setHistoryState() {
        _screenState.postValue(SearchScreenState.History(getTrackHistoryList()))
    }
}
