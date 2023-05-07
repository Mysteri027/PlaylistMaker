package com.example.playlistmaker.domain.interactor

import com.example.playlistmaker.domain.model.EmailData
import com.example.playlistmaker.domain.repository.ExternalNavigatorRepository
import com.example.playlistmaker.domain.repository.SettingRepository

class SharingInteractor(
    private val externalNavigatorRepository: ExternalNavigatorRepository
) {

    fun shareApp(link: String) {
        externalNavigatorRepository.shareLink(link)

    }

    fun openTerms(link: String) {
        externalNavigatorRepository.openLink(link)

    }

    fun openSupport(emailData: EmailData) {
        externalNavigatorRepository.openEmail(emailData)
    }
}