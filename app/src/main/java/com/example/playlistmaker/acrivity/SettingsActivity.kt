package com.example.playlistmaker.acrivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val shareContainer = findViewById<FrameLayout>(R.id.share_container)
        val supportContainer = findViewById<FrameLayout>(R.id.support_container)
        val arrowForwardContainer = findViewById<FrameLayout>(R.id.arrow_forward_container)
        val settingsTitle = findViewById<TextView>(R.id.setting_title)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.switch_theme_button)

        val sharedPreferences = getSharedPreferences(
            App.APP_SHARED_PREFERENCES_KEY, MODE_PRIVATE
        )
        themeSwitcher.isChecked = sharedPreferences.getBoolean(App.DART_THEME_KEY, false)
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (application as App).switchTheme(checked)
        }


        settingsTitle.setOnClickListener {
            finish()
        }


        shareContainer.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.yandex_practicum_link))
                type = "text/plain"
            }

            startActivity(Intent.createChooser(sendIntent, null))
        }

        supportContainer.setOnClickListener {
            val supportIntent: Intent = Intent().apply {
                action = Intent.ACTION_SENDTO
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(resources.getString(R.string.my_email)))
                putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.email_subject))
            }
            startActivity(supportIntent)
        }

        arrowForwardContainer.setOnClickListener {
            val arrowForwardIntent: Intent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(resources.getString(R.string.practicum_offer_link))
            }
            startActivity(arrowForwardIntent)
        }

    }
}