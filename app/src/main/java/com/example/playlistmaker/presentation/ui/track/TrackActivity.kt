@file:Suppress("DEPRECATION", "UNCHECKED_CAST")

package com.example.playlistmaker.presentation.ui.track

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivityTrackBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.presenter.track.TrackScreenPresenter
import com.example.playlistmaker.presentation.presenter.track.TrackScreenView
import com.example.playlistmaker.presentation.ui.search.SearchActivity
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

class TrackActivity : AppCompatActivity(), TrackScreenView {

    private lateinit var binding: ActivityTrackBinding
    private lateinit var presenter: TrackScreenPresenter

    private var playerState = STATE_DEFAULT

    private val handler = Handler(Looper.getMainLooper())

    private val currentTimeRunnable = object : Runnable {
        override fun run() {
            setCurrentTime(presenter.getCurrentPosition().toLong())
            handler.postDelayed(this, DELAY)
        }
    }

    private lateinit var trackPreviewUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = TrackScreenPresenter(
            this,
            Creator.provideMediaPlayerInteractor()
        )

        binding.trackScreenBackButton.setOnClickListener {
            finish()
        }

        val track = getSerializable(this, SearchActivity.TRACK_KEY, Track::class.java)

        val trackCoverUrl = presenter.getTrackImage(track.artworkUrl100)

        trackPreviewUrl = track.previewUrl

        presenter.preparePlayer(trackPreviewUrl)

        val trackCoverCornerRadius =
            binding.root.resources.getDimensionPixelSize(R.dimen.track_cover_corner_radius)

        Glide.with(binding.root)
            .load(trackCoverUrl)
            .placeholder(R.drawable.track_placeholder)
            .transform(RoundedCorners(trackCoverCornerRadius))
            .into(binding.trackScreenCover)

        with(binding) {
            trackScreenName.text = track.trackName
            trackScreenArtistName.text = track.artistName
            trackScreenDurationValue.text = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(track.trackTimeMillis.toLong())

            trackScreenAlbumValue.text = track.collectionName
            trackScreenYearValue.text =
                track.releaseDate.removeRange(4, track.releaseDate.length)
            trackScreenGenreValue.text = track.primaryGenreName
            trackScreenCountryValue.text = track.country

            trackScreenPlayButton.setOnClickListener {
                presenter.playbackControl()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        presenter.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.releasePlayer()
    }

    override fun setCurrentTime(milliseconds: Long) {
        binding.trackScreenCurrentTime.text = SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(milliseconds)
    }

    override fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                presenter.pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                presenter.startPlayer()
            }
        }
    }

    override fun setOnMediaPlayerPrepared() {
        binding.trackScreenPlayButton.isEnabled = true
        playerState = STATE_PREPARED
    }

    override fun setMediaPlayerOnCompletion() {
        playerState = STATE_PREPARED
        binding.trackScreenPlayButton.setImageResource(R.drawable.play_button)
        handler.removeCallbacks(currentTimeRunnable)
    }

    override fun setOnPlayerStarted() {
        binding.trackScreenPlayButton.setImageResource(R.drawable.pause_button)
        playerState = STATE_PLAYING
        handler.postDelayed(currentTimeRunnable, DELAY)
    }

    override fun setOnPlayerPaused() {
        binding.trackScreenPlayButton.setImageResource(R.drawable.play_button)
        playerState = STATE_PAUSED
        handler.removeCallbacks(currentTimeRunnable)
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 1000L
    }
}

fun <T : Serializable?> getSerializable(activity: Activity, name: String, clazz: Class<T>): T {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        activity.intent.getSerializableExtra(name, clazz)!!
    else
        activity.intent.getSerializableExtra(name) as T
}