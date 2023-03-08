package com.example.playlistmaker.activity

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityTrackBinding
import com.example.playlistmaker.model.Track
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class TrackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrackBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.trackScreenBackButton.setOnClickListener {
            finish()
        }

        val track = getSerializable(this, SearchActivity.TRACK_KEY, Track::class.java)

        val trackCoverUrl = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

        Glide.with(binding.root)
            .load(trackCoverUrl)
            .placeholder(R.drawable.track_placeholder)
            .into(binding.trackScreenCover)

        binding.trackScreenName.text = track.trackName
        binding.trackScreenArtistName.text = track.artistName
        binding.trackScreenDurationValue.text = SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(track.trackTimeMillis.toLong())

        binding.trackScreenAlbumValue.text = track.collectionName
        binding.trackScreenYearValue.text = track.releaseDate.removeRange(4, track.releaseDate.length)
        binding.trackScreenGenreValue.text = track.primaryGenreName
        binding.trackScreenCountryValue.text = track.country


    }
}

fun <T : Serializable?> getSerializable(activity: Activity, name: String, clazz: Class<T>): T {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        activity.intent.getSerializableExtra(name, clazz)!!
    else
        activity.intent.getSerializableExtra(name) as T
}