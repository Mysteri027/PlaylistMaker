package com.example.playlistmaker.presentation.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this, SettingsViewModelFactory(
                settingsInteractor = Creator.provideSettingsInteractor(),
                sharingInteractor = Creator.provideSharingInteractor(context = this),
            )
        )[SettingsViewModel::class.java]

        binding.switchThemeButton.isChecked = viewModel.getThemeSettings()

        binding.switchThemeButton.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked)
        }

        binding.settingTitle.setOnClickListener {
            finish()
        }

        binding.shareContainer.setOnClickListener {
            val link = resources.getString(R.string.yandex_practicum_link)
            viewModel.shareApp(link)
        }

        binding.supportContainer.setOnClickListener {
            val email = resources.getString(R.string.my_email)
            val subject = resources.getString(R.string.email_subject)
            viewModel.sendEmailToSupport(email, subject)
        }

        binding.arrowForwardContainer.setOnClickListener {
            val link = resources.getString(R.string.practicum_offer_link)
            viewModel.checkOffer(link)
        }
    }
}