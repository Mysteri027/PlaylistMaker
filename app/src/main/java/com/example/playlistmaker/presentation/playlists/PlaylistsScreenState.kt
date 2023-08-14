package com.example.playlistmaker.presentation.playlists

import com.example.playlistmaker.domain.model.Playlist

sealed class PlaylistsScreenState(playlists: List<Playlist>?) {
    object Empty : PlaylistsScreenState(null)
    class Filled(val playlists: List<Playlist>) : PlaylistsScreenState(playlists)
}