package com.example.playlistmaker.domain.interactor

import com.example.playlistmaker.domain.model.EmailData
import com.example.playlistmaker.domain.repository.ExternalNavigatorRepository

class SharingInteractor(
    private val externalNavigatorRepository: ExternalNavigatorRepository
) {

    fun shareApp(link: String) {
        externalNavigatorRepository.shareLink(link)
    }

    fun sharePlaylist(playlist: String) {
        externalNavigatorRepository.sharePlaylist(playlist)
    }

    fun openTerms(link: String) {
        externalNavigatorRepository.openLink(link)

    }

    fun openSupport(emailData: EmailData) {
        externalNavigatorRepository.openEmail(emailData)
    }
}