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
            val arrayTrackType = object : TypeToken<ArrayList<Track>>() {}.type

            val playlistTracks =
                gson.fromJson(playlist.trackList, arrayTrackType) ?: arrayListOf<Track>()


            var isInPlaylist = false

            playlistTracks.forEach {
                if (it.trackId == track.trackId) {
                    isInPlaylist = true
                }
            }

            if (!isInPlaylist) {
                playlistTracks.add(track)
                playlist.trackList = gson.toJson(playlistTracks)
                playlist.countTracks++

                updatePlaylist(playlist)
                emit(true)
            } else {
                emit(false)
            }
        }

    override suspend fun getPlaylistById(id: Long): Flow<Playlist> = flow {
        emit(databaseMapperPlayListToModel.map(database.playlistDao().getPlaylistById(id)))
    }

    override suspend fun getTracksFromPlaylist(id: Long): Flow<List<Track>> = flow {
        val gson = GsonBuilder().create()
        val listTrackType = object : TypeToken<List<Track>>() {}.type

        val tracksString = database.playlistDao().getTracksFromPlaylist(id)
        val tracks = gson.fromJson(tracksString, listTrackType) ?: listOf<Track>()

        emit(tracks)
    }

    override suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long): Flow<Playlist> =
        flow {
            val gson = GsonBuilder().create()
            val listTrackType = object : TypeToken<List<Track>>() {}.type

            val playlist =
                databaseMapperPlayListToModel.map(
                    database.playlistDao().getPlaylistById(playlistId)
                )

            val tracksString = playlist.trackList
            val tracks: MutableList<Track> =
                gson.fromJson<MutableList<Track>?>(tracksString, listTrackType).toMutableList()

            tracks.removeIf { it.trackId == trackId }

            playlist.trackList = gson.toJson(tracks)
            playlist.countTracks--
            updatePlaylist(playlist)

            emit(playlist)
        }
}