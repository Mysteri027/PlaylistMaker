package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.model.Track

interface LocalStorageRepository {

    fun getSearchHistory(): ArrayList<Track>
    fun addTrack(track: Track)
    fun clear()
}