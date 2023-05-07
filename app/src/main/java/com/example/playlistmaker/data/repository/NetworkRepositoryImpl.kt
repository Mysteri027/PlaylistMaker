package com.example.playlistmaker.data.repository

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.NetworkRepository
import com.example.playlistmaker.data.network.ITunesSearchAPIService
import com.example.playlistmaker.data.dto.TrackResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkRepositoryImpl(private val iTunesSearchAPIService: ITunesSearchAPIService) :
    NetworkRepository {

    override fun getTracks(
        query: String,
        onSuccess: (List<Track>) -> Unit,
        onError: () -> Unit
    ) {
        iTunesSearchAPIService.search(query).enqueue(object : Callback<TrackResponse> {
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                if (response.isSuccessful and (response.code() == 200)) {
                    onSuccess.invoke(response.body()?.results ?: listOf())
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                onError.invoke()
            }

        })
    }
}