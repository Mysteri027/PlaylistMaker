package com.example.playlistmaker.domain.interactor

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

class TrackDatabaseInteractor(
    private val databaseRepository: DatabaseRepository
) {
    suspend fun addTrack(track: Track) {
        databaseRepository.addTrack(track)
    }

    suspend fun deleteTrack(id: Long) {
        databaseRepository.deleteTrack(id)
    }

    suspend fun getAllTracks(): Flow<List<Track>> = databaseRepository.getAllTracks()
}