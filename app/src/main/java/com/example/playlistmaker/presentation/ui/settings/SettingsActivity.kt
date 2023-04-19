package com.example.playlistmaker.presentation.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.App
import com.example.playlistmaker.Container
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.presentation.presenter.settings.SettingsScreenPresenter
import com.example.playlistmaker.presentation.presenter.settings.SettingsScreenView

class SettingsActivity : AppCompatActivity(), SettingsScreenView {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var presenter: SettingsScreenPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = SettingsScreenPresenter(this)

        binding.switchThemeButton.isChecked =
            Container.themeSharedPreferences.getBoolean(DART_THEME_KEY, false)

        binding.switchThemeButton.setOnCheckedChangeListener { _, checked ->
            (application as App).switchTheme(checked)
        }

        binding.settingTitle.setOnClickListener {
            finish()
        }

        binding.shareContainer.setOnClickListener {
            presenter.shareApp()
        }

        binding.supportContainer.setOnClickListener {
            presenter.sendEmailToSupport()
        }

        binding.arrowForwardContainer.setOnClickListener {
            presenter.checkOffer()
        }
    }

    override fun shareApp() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.yandex_practicum_link))
            type = "text/plain"
        }

        startActivity(Intent.createChooser(sendIntent, null))
    }

    override fun sendEmailToSupport() {
        val supportIntent: Intent = Intent().apply {
            action = Intent.ACTION_SENDTO
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(resources.getString(R.string.my_email)))
            putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.email_subject))
        }
        startActivity(supportIntent)
    }

    override fun checkOffer() {
        val arrowForwardIntent: Intent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(resources.getString(R.string.practicum_offer_link))
        }
        startActivity(arrowForwardIntent)
    }

    companion object {
        const val DART_THEME_KEY = "dart_theme_key"
    }

}