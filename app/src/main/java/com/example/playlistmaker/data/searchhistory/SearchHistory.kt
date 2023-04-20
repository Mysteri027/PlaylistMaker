package com.example.playlistmaker.data.searchhistory

import android.content.SharedPreferences
import com.example.playlistmaker.domain.model.Track
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class SearchHistory(
    private val sharedPreferences: SharedPreferences,
) {
    private val gson = GsonBuilder().create()
    private val arrayTrackType = object : TypeToken<ArrayList<Track>>() {}.type

    fun getSearchHistory(): ArrayList<Track> {
        val json = sharedPreferences.getString(ARRAY_LIST_TRACK_KEY, null)
        return gson.fromJson(json, arrayTrackType) ?: arrayListOf()
    }

    private fun saveSearchHistory(tracks: ArrayList<Track>) {
        val json = gson.toJson(tracks, arrayTrackType)
        sharedPreferences.edit().putString(ARRAY_LIST_TRACK_KEY, json).apply()
    }

    fun addTrack(track: Track) {
        val tracks = getSearchHistory()

        if (tracks.find { it.trackId == track.trackId } != null) {
            tracks.remove(track)
        }

        tracks.add(0, track)

        while (tracks.size > MAX_SEARCH_HISTORY_SIZE) {
            tracks.removeLast()
        }

        saveSearchHistory(tracks)
    }

    fun clear() {
        sharedPreferences.edit().putString(ARRAY_LIST_TRACK_KEY, null).apply()
    }

    companion object {
        const val ARRAY_LIST_TRACK_KEY = "ARRAY_LIST_TRACK_KEY"
        private const val MAX_SEARCH_HISTORY_SIZE = 10
    }
}