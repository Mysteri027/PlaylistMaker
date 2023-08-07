package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {
    suspend fun addTrack(track: Track)
    suspend fun deleteTrack(id: Long)
    suspend fun getAllTracks(): Flow<List<Track>>
}