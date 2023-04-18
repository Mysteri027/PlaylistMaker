package com.example.playlistmaker.presentation.presenter.search

import android.view.View
import com.example.playlistmaker.data.searchhistory.SearchHistory
import com.example.playlistmaker.domain.interactor.LocalStorageInteractor
import com.example.playlistmaker.domain.interactor.NetworkInteractor
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.ui.search.PlaceHolderType

class SearchScreenPresenter(
    private val view: SearchScreenView,
    private val networkIterator: NetworkInteractor,
    private val localStorageInteractor: LocalStorageInteractor,
) {

    fun findTracks(query: String) {
        view.hidePlaceHolder()
        view.setProgressbarListVisibility(View.VISIBLE)
        view.setTrackListVisibility(View.GONE)
        networkIterator.getTracks(
            query = query,
            onSuccess = { tracks ->
                view.setProgressbarListVisibility(View.GONE)
                view.setTrackListVisibility(View.VISIBLE)

                if (tracks.isEmpty()) {
                    view.showPlaceHolder(PlaceHolderType.NOT_FOUND)
                } else {
                    view.updateTrackList(tracks)
                }
            },
            onError = {
                view.showPlaceHolder(PlaceHolderType.NETWORK_ERROR)
            }
        )
    }

    fun clearButtonClicked() {
        view.clearButtonClicked()
    }

    fun updateHistory(key: String, tracks: List<Track>) {
        if (key == SearchHistory.ARRAY_LIST_TRACK_KEY) {
            view.updateTrackListHistory(tracks)
        }
    }

    fun addTrackToHistory(track: Track) {
        localStorageInteractor.addTrack(track)
    }

    fun clearSearchHistory() {
        localStorageInteractor.clear()
    }
}