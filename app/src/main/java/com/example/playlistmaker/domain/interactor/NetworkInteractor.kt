package com.example.playlistmaker.domain.interactor

import com.example.playlistmaker.domain.repository.NetworkRepository

class NetworkInteractor(
    private val networkRepository: NetworkRepository
) {

    fun getTracks(query: String) = networkRepository.getTracks(query)
}