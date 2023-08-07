package com.example.playlistmaker.data.repository

import android.util.Log
import com.example.playlistmaker.data.db.TrackDatabase
import com.example.playlistmaker.data.searchhistory.SearchHistory
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.LocalStorageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocalStorageRepositoryImpl(
    private val searchHistory: SearchHistory,
    private val trackDatabase: TrackDatabase
) : LocalStorageRepository {
    override fun getSearchHistory(): List<Track> {
        val trackHistory = searchHistory.getSearchHistory()
        trackHistory.map {

        }
        return searchHistory.getSearchHistory()
    }

    override fun addTrack(track: Track) {
        searchHistory.addTrack(track)
    }

    override fun clear() {
        searchHistory.clear()
    }
}