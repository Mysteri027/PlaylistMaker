package com.example.playlistmaker.presentation.favorite

import com.example.playlistmaker.domain.model.Track

sealed class FavoriteTracksViewState(tracks: List<Track>?) {
    object Empty: FavoriteTracksViewState(null)
    class Content(val tracks: List<Track>): FavoriteTracksViewState(tracks)
}