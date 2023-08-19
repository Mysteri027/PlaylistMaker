package com.example.playlistmaker.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.EmailData
import com.example.playlistmaker.domain.repository.ExternalNavigatorRepository

class ExternalNavigatorRepositoryImpl(
    private val context: Context
) : ExternalNavigatorRepository {

    override fun shareLink(link: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, link)
            type = "text/plain"
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(sendIntent)
    }

    override fun openLink(link: String) {
        val arrowForwardIntent: Intent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(link)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(arrowForwardIntent)
    }

    override fun openEmail(emailData: EmailData) {
        val supportIntent: Intent = Intent().apply {
            action = Intent.ACTION_SENDTO
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.email))
            putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(supportIntent)
    }
}