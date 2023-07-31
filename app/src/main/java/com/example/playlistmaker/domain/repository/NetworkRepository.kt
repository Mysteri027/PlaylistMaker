package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface NetworkRepository {

    fun getTracks(query: String): Flow<Pair<List<Track>?, String?>>
}