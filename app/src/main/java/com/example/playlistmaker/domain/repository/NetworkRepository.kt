package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.model.Track

interface NetworkRepository {

    fun getTracks(query: String, onSuccess: (List<Track>) -> Unit, onError: () -> Unit)
}