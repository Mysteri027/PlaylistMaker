package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.db.TrackDatabase
import com.example.playlistmaker.data.mapper.DatabaseMapperPlayListToEntity
import com.example.playlistmaker.data.mapper.DatabaseMapperPlayListToModel
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.PlaylistRepository
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val database: TrackDatabase,
    private val databaseMapperPlayListToEntity: DatabaseMapperPlayListToEntity,
    private val databaseMapperPlayListToModel: DatabaseMapperPlayListToModel,
) : PlaylistRepository {

    override suspend fun addPlaylist(playlist: Playlist) {
        database.playlistDao().insertPlaylist(databaseMapperPlayListToEntity.map(playlist))
    }

    override suspend fun deletePlaylist(id: Long) {
        database.playlistDao().deletePlaylist(id)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        database.playlistDao().updatePlaylist(databaseMapperPlayListToEntity.map(playlist))
    }

    override suspend fun getSavedPlaylists(): Flow<List<Playlist>> = flow {
        emit(database.playlistDao().getSavedPlaylists().map {
            databaseMapperPlayListToModel.map(it)
        })
    }

    override suspend fun addTrackToPlayList(track: Track, playlist: Playlist): Flow<Boolean> =
        flow {
            val gson = GsonBuilder().create()
            val arrayTrackType = object : TypeToken<ArrayList<Long>>() {}.type

            val playlistTracks =
                gson.fromJson(playlist.trackList, arrayTrackType) ?: arrayListOf<Long>()

            if (!playlistTracks.contains(track.trackId)) {
                playlistTracks.add(track.trackId)
                playlist.trackList = gson.toJson(playlistTracks)
                playlist.countTracks++

                updatePlaylist(playlist)
                emit(true)
            } else {
                emit(false)
            }
        }
}