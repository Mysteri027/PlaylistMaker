package com.example.playlistmaker.presentation.presenter.search

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.ui.search.PlaceHolderType

interface SearchScreenView {

    fun setTrackListVisibility(visibility: Int)
    fun setProgressbarListVisibility(visibility: Int)
    fun hidePlaceHolder()
    fun showPlaceHolder(placeHolderType: PlaceHolderType)
    fun updateTrackList(tracks: List<Track>)
    fun updateTrackListHistory(tracks: List<Track>)
    fun openTrackScreen(track: Track)
    fun clearButtonClicked()
}