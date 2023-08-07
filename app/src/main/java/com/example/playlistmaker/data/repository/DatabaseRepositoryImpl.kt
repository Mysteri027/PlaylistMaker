package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.db.TrackDatabase
import com.example.playlistmaker.data.mapper.DatabaseMapperToEntity
import com.example.playlistmaker.data.mapper.DatabaseMapperToTrack
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.DatabaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class DatabaseRepositoryImpl(
    private val trackDatabase: TrackDatabase,
    private val trackToEntity: DatabaseMapperToEntity,
    private val entityToTrack: DatabaseMapperToTrack
) : DatabaseRepository {

    override suspend fun addTrack(track: Track) {
        trackDatabase.trackDao().insertTrack(trackToEntity.map(track))
    }

    override suspend fun deleteTrack(id: Long) {
        trackDatabase.trackDao().deleteTrack(id)
    }

    override suspend fun getAllTracks(): Flow<List<Track>> = flow {
        emit(trackDatabase.trackDao().selectAllTracks().map {
            entityToTrack.map(it)
        })
    }
}