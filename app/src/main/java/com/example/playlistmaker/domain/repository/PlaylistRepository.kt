package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(id: Long)
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun getSavedPlaylists(): Flow<List<Playlist>>
    suspend fun addTrackToPlayList(track: Track, playlist: Playlist): Flow<Boolean>
}