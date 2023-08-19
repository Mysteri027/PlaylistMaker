package com.example.playlistmaker.presentation.playlists

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.presentation.create.CreatePlaylistFragment
import com.example.playlistmaker.presentation.playlist.PlaylistFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    companion object {
        fun newInstance(): PlaylistsFragment {
            return PlaylistsFragment()
        }

    }

    private val viewModel: PlaylistsViewModel by viewModel()

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val adapter = PlaylistScreenAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentPlaylistsAddButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_createPlaylistFragment,
                CreatePlaylistFragment.newInstance(-1)
            )
        }

        adapter.onPlaylistClicked = { playlist ->
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_playlistFragment,
                PlaylistFragment.createArgs(playlist.id)
            )
        }

        viewModel.getPlaylists()
        binding.fragmentPlaylistsList.layoutManager = GridLayoutManager(requireContext(), 2)
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.grid_spacing)
        binding.fragmentPlaylistsList.addItemDecoration(GridSpacingItemDecoration(spacingInPixels))
        binding.fragmentPlaylistsList.adapter = adapter

        initObservers()
    }

    private fun initObservers() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistsScreenState.Empty -> {
                    binding.fragmentPlaylistsNoFoundImage.visibility = View.VISIBLE
                    binding.fragmentPlaylistsNoFoundText.visibility = View.VISIBLE
                    binding.fragmentPlaylistsList.visibility = View.GONE
                }

                is PlaylistsScreenState.Filled -> {
                    binding.fragmentPlaylistsNoFoundImage.visibility = View.GONE
                    binding.fragmentPlaylistsNoFoundText.visibility = View.GONE
                    binding.fragmentPlaylistsList.visibility = View.VISIBLE
                    renderPlaylists(state.playlists)
                }
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun renderPlaylists(playlists: List<Playlist>) {
        adapter.playlists.clear()
        adapter.playlists.addAll(playlists)
        adapter.notifyDataSetChanged()
    }
}