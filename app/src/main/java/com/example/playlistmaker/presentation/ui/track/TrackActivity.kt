@file:Suppress("DEPRECATION", "UNCHECKED_CAST")

package com.example.playlistmaker.presentation.ui.track

import android.app.Activity
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityTrackBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.ui.search.SearchActivity
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

class TrackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrackBinding

    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT

    private val handler = Handler(Looper.getMainLooper())

    private val currentTimeRunnable = object : Runnable {
        override fun run() {
            setCurrentTime(mediaPlayer.currentPosition.toLong())
            handler.postDelayed(this, DELAY)
        }
    }

    private lateinit var trackPreviewUrl: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.trackScreenBackButton.setOnClickListener {
            finish()
        }

        val track = getSerializable(this, SearchActivity.TRACK_KEY, Track::class.java)

        val trackCoverUrl = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

        trackPreviewUrl = track.previewUrl

        preparePlayer()

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
                playbackControl()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(trackPreviewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.trackScreenPlayButton.isEnabled = true
            playerState = STATE_PREPARED
        }

        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            binding.trackScreenPlayButton.setImageResource(R.drawable.play_button)
            handler.removeCallbacks(currentTimeRunnable)
            setCurrentTime(0L)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        binding.trackScreenPlayButton.setImageResource(R.drawable.pause_button)
        playerState = STATE_PLAYING
        handler.postDelayed(currentTimeRunnable, DELAY)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        binding.trackScreenPlayButton.setImageResource(R.drawable.play_button)
        playerState = STATE_PAUSED
        handler.removeCallbacks(currentTimeRunnable)
    }

    private fun setCurrentTime(milliseconds: Long) {
        binding.trackScreenCurrentTime.text = SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(milliseconds)
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
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