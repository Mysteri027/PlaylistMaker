@file:Suppress("DEPRECATION")

package com.example.playlistmaker.presentation.track

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentTrackBinding
import com.example.playlistmaker.domain.model.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
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

    private val playlistAdapter = PlaylistAdapter()

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

        binding.bottomSheetList.adapter = playlistAdapter

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> { binding.overlay.visibility = View.GONE }
                    else -> { binding.overlay.visibility = View.VISIBLE }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

        })

        binding.trackScreenAddButton.setOnClickListener {
            viewModel.getPlayLists()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        viewModel.isAlreadyInPlaylist.observe(viewLifecycleOwner) {
            val message =
                if (it.second) "Добавлено в плейлист ${it.first}" else "Трек уже добавлен в плейлист ${it.first}"

            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

        }

        viewModel.playlists.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                playlistAdapter.playlists.clear()
                playlistAdapter.playlists.addAll(it)
                playlistAdapter.notifyDataSetChanged()
            }
        }

        binding.bottomSheetAddButton.setOnClickListener {
            findNavController().navigate(R.id.action_trackFragment_to_createPlaylistFragment)
        }

        val track = requireArguments().getSerializable(ARGS_TRACK) as Track
        viewModel.checkIsFavorite(track)
        val trackCoverUrl = viewModel.getTrackImage(track.artworkUrl100)

        playlistAdapter.onPlayListClicked = {
            viewModel.addTrackToPlayList(track, it)
        }

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

            trackScreenLikeButton.setOnClickListener {
                viewModel.onFavoriteClicked(track)
            }
        }

        viewModel.playerState.observe(viewLifecycleOwner) {
            binding.trackScreenPlayButton.isEnabled = it.isPlayButtonEnabled
            binding.trackScreenPlayButton.setImageResource(it.buttonImage)
            binding.trackScreenCurrentTime.text = it.progress

            Log.d("PlayerState", it.toString())
        }

        viewModel.isTrackFavorite.observe(viewLifecycleOwner) { isFavorite ->
            if (isFavorite) {
                binding.trackScreenLikeButton.setImageResource(R.drawable.like_button_filled)
            } else {
                binding.trackScreenLikeButton.setImageResource(R.drawable.like_button)
            }
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