package com.example.playlistmaker.presentation.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences(
            App.APP_SHARED_PREFERENCES_KEY, MODE_PRIVATE
        )
        binding.switchThemeButton.isChecked = sharedPreferences.getBoolean(App.DART_THEME_KEY, false)
        binding.switchThemeButton.setOnCheckedChangeListener { _, checked ->
            (application as App).switchTheme(checked)
        }

        binding.settingTitle.setOnClickListener {
            finish()
        }

        binding.shareContainer.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.yandex_practicum_link))
                type = "text/plain"
            }

            startActivity(Intent.createChooser(sendIntent, null))
        }

        binding.supportContainer.setOnClickListener {
            val supportIntent: Intent = Intent().apply {
                action = Intent.ACTION_SENDTO
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(resources.getString(R.string.my_email)))
                putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.email_subject))
            }
            startActivity(supportIntent)
        }

        binding.arrowForwardContainer.setOnClickListener {
            val arrowForwardIntent: Intent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(resources.getString(R.string.practicum_offer_link))
            }
            startActivity(arrowForwardIntent)
        }

    }
}