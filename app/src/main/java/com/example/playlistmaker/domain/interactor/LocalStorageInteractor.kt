package com.example.playlistmaker.domain.interactor

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.LocalStorageRepository

class LocalStorageInteractor(
    private val localStorageRepository: LocalStorageRepository
) {

    fun getSearchHistory(): ArrayList<Track> {
        return localStorageRepository.getSearchHistory()
    }

    fun addTrack(track: Track) {
        localStorageRepository.addTrack(track)
    }

    fun clear() {
        localStorageRepository.clear()
    }
}