package com.example.playlistmaker.presentation.favorite

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.search.TrackAdapter
import com.example.playlistmaker.presentation.track.TrackFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment(R.layout.fragment_favorite_tracks) {

    private val viewModel: FavoriteTracksViewModel by viewModel()

    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!

    private val trackAdapter = TrackAdapter()

    companion object {
        fun newInstance(): FavoriteTracksFragment {
            return FavoriteTracksFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteTracksBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.favoriteTrackScreenTrackList.adapter = trackAdapter

        trackAdapter.trackClickListener = { track ->
            track.isFavorite = true
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_trackFragment,
                TrackFragment.createArgs(track)
            )
        }

        viewModel.updateState()
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is FavoriteTracksViewState.Empty -> {
                    setPlaceHolder()
                }

                is FavoriteTracksViewState.Content -> {
                    setContent(it.tracks)
                }
            }
            Log.d("FavoriteTracksViewState", it.toString())
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setContent(trackList: List<Track>) {
        trackAdapter.trackList.clear()
        trackAdapter.trackList.addAll(trackList)
        trackAdapter.notifyDataSetChanged()

        binding.favoriteTrackScreenTrackList.visibility = View.VISIBLE
        binding.favoriteTrackScreenNotFoundImage.visibility = View.GONE
        binding.favoriteTrackScreenNotFoundText.visibility = View.GONE
    }

    private fun setPlaceHolder() {
        binding.favoriteTrackScreenTrackList.visibility = View.GONE
        binding.favoriteTrackScreenNotFoundImage.visibility = View.VISIBLE
        binding.favoriteTrackScreenNotFoundText.visibility = View.VISIBLE
    }
}