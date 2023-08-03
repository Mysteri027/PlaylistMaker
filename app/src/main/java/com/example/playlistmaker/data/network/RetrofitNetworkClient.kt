package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.Response

class RetrofitNetworkClient(
    private val iTunesSearchAPIService: ITunesSearchAPIService
) : NetworkClient {
    override suspend fun doRequest(query: String): Response {
        return try {
            val response = iTunesSearchAPIService.search(query)
            response.apply { resultCode = 200 }
        } catch (e: Throwable) {
            Response().apply { resultCode = 500 }
        }
    }
}