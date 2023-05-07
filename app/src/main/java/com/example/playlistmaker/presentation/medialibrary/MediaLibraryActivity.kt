package com.example.playlistmaker.presentation.medialibrary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R

class MediaLibraryActivity : AppCompatActivity() {

    private lateinit var viewModel: MediaLibraryViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_library)

        viewModel = ViewModelProvider(this)[MediaLibraryViewModel::class.java]
    }
}