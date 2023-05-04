package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.model.EmailData

interface ExternalNavigatorRepository {

    fun shareLink(link: String)
    fun openLink(link: String)
    fun openEmail(emailData: EmailData)
}