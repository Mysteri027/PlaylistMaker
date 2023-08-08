package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.db.TrackDatabase
import com.example.playlistmaker.data.dto.TrackResponse
import com.example.playlistmaker.data.network.NetworkClient
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NetworkRepositoryImpl(
    private val networkClient: NetworkClient,
    private val trackDatabase: TrackDatabase
) :
    NetworkRepository {

    override fun getTracks(query: String): Flow<Pair<List<Track>?, String?>> = flow {
        val result = networkClient.doRequest(query)

        when (result.resultCode) {
            200 -> {
                val databaseTracksId = trackDatabase.trackDao().selectAllIdTracks()
                with(result as TrackResponse) {
                    result.results.forEach {
                        if (databaseTracksId.contains(it.trackId)) {
                            it.isFavorite = true
                        }
                    }
                    emit(Pair(result.results, null))
                }
            }

            else -> {
                emit(Pair(null, "error"))
            }
        }
    }
}