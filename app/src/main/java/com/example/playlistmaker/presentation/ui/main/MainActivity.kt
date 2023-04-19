package com.example.playlistmaker.presentation.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.presentation.presenter.main.MainScreenPresenter
import com.example.playlistmaker.presentation.presenter.main.MainScreenView
import com.example.playlistmaker.presentation.ui.medialibrary.MediaLibraryActivity
import com.example.playlistmaker.presentation.ui.search.SearchActivity
import com.example.playlistmaker.presentation.ui.settings.SettingsActivity

class MainActivity : AppCompatActivity(), MainScreenView {

    private lateinit var binding: ActivityMainBinding
    private lateinit var presenter: MainScreenPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = MainScreenPresenter(this)

        binding.searchButton.setOnClickListener {
            presenter.openSearchScreen()
        }

        binding.mediaLibraryButton.setOnClickListener {
            presenter.openMediaLibraryScreen()
        }

        binding.settingsButton.setOnClickListener {
            presenter.openSettingsScreen()
        }

    }

    override fun openSearchScreen() {
        val searchIntent = Intent(this, SearchActivity::class.java)
        startActivity(searchIntent)
    }

    override fun openMediaLibraryScreen() {
        val mediaLibraryIntent = Intent(this, MediaLibraryActivity::class.java)
        startActivity(mediaLibraryIntent)
    }

    override fun openSettingsScreen() {
        val settingsIntent = Intent(this, SettingsActivity::class.java)
        startActivity(settingsIntent)
    }
}