package com.example.playlistmaker.presentation.medialibrary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaLibraryActivity : AppCompatActivity() {

    private val viewModel: MediaLibraryViewModel by viewModel()

    private val binding by lazy {
        ActivityMediaLibraryBinding.inflate(layoutInflater)
    }
    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.mediaLibraryScreenBackButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.mediaLibraryScreenViewPager.adapter = MediaLibraryAdapter(
            supportFragmentManager,
            lifecycle,
        )

        tabLayoutMediator = TabLayoutMediator(
            binding.mediaLibraryScreenTabLayout,
            binding.mediaLibraryScreenViewPager,
        ) { tab, position ->
            when (position) {
                0 -> tab.text = "Избранные треки"
                1 -> tab.text = "Плейлисты"
            }
        }
        tabLayoutMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabLayoutMediator.detach()
    }
}