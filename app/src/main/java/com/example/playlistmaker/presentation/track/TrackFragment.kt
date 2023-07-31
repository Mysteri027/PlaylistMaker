@file:Suppress("DEPRECATION")

package com.example.playlistmaker.presentation.track

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentTrackBinding
import com.example.playlistmaker.domain.model.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class TrackFragment : Fragment() {

    companion object {
        const val ARGS_TRACK = "ARGS_TRACK"

        fun createArgs(track: Track): Bundle =
            bundleOf(ARGS_TRACK to track)
    }


    private var _binding: FragmentTrackBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TrackViewModel by viewModel()

    private lateinit var trackPreviewUrl: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.trackScreenBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        val track = requireArguments().getSerializable(ARGS_TRACK) as Track

        val trackCoverUrl = viewModel.getTrackImage(track.artworkUrl100)

        trackPreviewUrl = track.previewUrl

        viewModel.preparePlayer(trackPreviewUrl)

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
                viewModel.onPlayButtonClicked()
            }
        }

        viewModel.playerState.observe(viewLifecycleOwner) {
            binding.trackScreenPlayButton.isEnabled = it.isPlayButtonEnabled
            binding.trackScreenPlayButton.setImageResource(it.buttonImage)
            binding.trackScreenCurrentTime.text = it.progress

            Log.d("PlayerState", it.toString())
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.releasePlayer()
    }
}