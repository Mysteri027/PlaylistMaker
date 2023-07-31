package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.dto.TrackResponse
import com.example.playlistmaker.data.network.NetworkClient
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NetworkRepositoryImpl(private val networkClient: NetworkClient) :
    NetworkRepository {

    override fun getTracks(query: String): Flow<Pair<List<Track>?, String?>> = flow {
        val result = networkClient.doRequest(query)
        when (result.resultCode) {
            200 -> {
                with(result as TrackResponse) {
                    emit(Pair(result.results, null))
                }
            }

            else -> {
                emit(Pair(null, "error"))
            }
        }
    }
}