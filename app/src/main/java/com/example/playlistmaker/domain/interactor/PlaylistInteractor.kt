package com.example.playlistmaker.domain.interactor

import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistInteractor(
    private val playlistRepository: PlaylistRepository,
) {
    suspend fun addPlaylist(playlist: Playlist) {
        playlistRepository.addPlaylist(playlist)
    }

    suspend fun deletePlaylist(id: Long) {
        playlistRepository.deletePlaylist(id)
    }

    suspend fun updatePlaylist(playlist: Playlist) {
        playlistRepository.updatePlaylist(playlist)
    }

    suspend fun getSavedPlaylists(): Flow<List<Playlist>> = playlistRepository.getSavedPlaylists()

    suspend fun addTrackToPlayList(track: Track, playlist: Playlist): Flow<Boolean> =
        playlistRepository.addTrackToPlayList(track, playlist)

    suspend fun getPlaylistById(id: Long): Flow<Playlist> = playlistRepository.getPlaylistById(id)

    suspend fun getTracksFromPlaylist(id: Long): Flow<List<Track>> =
        playlistRepository.getTracksFromPlaylist(id)

    suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long) =
        playlistRepository.deleteTrackFromPlaylist(trackId, playlistId)

}