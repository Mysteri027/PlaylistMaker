package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.searchhistory.SearchHistory
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.LocalStorageRepository

class LocalStorageRepositoryImpl(
    private val searchHistory: SearchHistory
) : LocalStorageRepository {
    override fun getSearchHistory(): ArrayList<Track> {
        return searchHistory.getSearchHistory()
    }

    override fun addTrack(track: Track) {
        searchHistory.addTrack(track)
    }

    override fun clear() {
        searchHistory.clear()
    }
}