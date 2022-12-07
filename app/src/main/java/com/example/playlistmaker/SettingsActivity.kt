package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val shareButton = findViewById<ImageView>(R.id.share_button)
        val supportButton = findViewById<ImageView>(R.id.support_button)
        val arrowForwardButton = findViewById<ImageView>(R.id.arrow_forward_button)
        val settingsTitle = findViewById<TextView>(R.id.setting_title)


        settingsTitle.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        shareButton.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.yandex_practicum_link))
                type = "text/plain"
            }

            startActivity(Intent.createChooser(sendIntent, null))
        }

        supportButton.setOnClickListener {
            val supportIntent: Intent = Intent().apply {
                action = Intent.ACTION_SENDTO
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(resources.getString(R.string.my_email)))
                putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.email_subject))
            }
            startActivity(supportIntent)
        }

        arrowForwardButton.setOnClickListener {
            val arrowForwardIntent: Intent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(resources.getString(R.string.practicum_offer_link))
            }
            startActivity(arrowForwardIntent)
        }

    }
}