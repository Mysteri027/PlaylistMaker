package com.example.playlistmaker.network

import com.example.playlistmaker.model.Track

data class TrackResponse(
    val resultCount: Int,
    val results: List<Track>,
)