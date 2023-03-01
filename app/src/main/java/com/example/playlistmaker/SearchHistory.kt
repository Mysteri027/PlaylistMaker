package com.example.playlistmaker

import android.content.SharedPreferences
import com.example.playlistmaker.model.Track
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

    fun saveSearchHistory(tracks: ArrayList<Track>) {
        val json = gson.toJson(tracks, arrayTrackType)
        sharedPreferences.edit().putString(ARRAY_LIST_TRACK_KEY, json).apply()
    }

    fun addTrack(track: Track) {
        val tracks = getSearchHistory()

        if (tracks.find { it.trackId == track.trackId } != null) {
            tracks.remove(track)
        }

        tracks.add(0, track)

        while(tracks.size > 10) {
            tracks.removeLast()
        }

        saveSearchHistory(tracks)
    }

    fun clear() {
        sharedPreferences.edit().remove(ARRAY_LIST_TRACK_KEY).apply()
    }

    companion object {
        const val ARRAY_LIST_TRACK_KEY = "ARRAY_LIST_TRACK_KEY"
    }
}