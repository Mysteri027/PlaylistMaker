package com.example.playlistmaker.presentation.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.presentation.create.CreatePlaylistFragment
import com.example.playlistmaker.presentation.track.TrackFragment
import com.example.playlistmaker.utils.setImage
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistFragment : Fragment() {

    companion object {
        const val ARGS_PLAYLIST = "ARGS_PLAYLIST"
    }

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaylistViewModel by viewModel()
    private val adapter by lazy {
        PlaylistTrackAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility =
            View.GONE

        val playlistId = requireArguments().getLong(ARGS_PLAYLIST)
        binding.bottomSheetListTracks.adapter = adapter

        initListeners(playlistId)

        viewModel.getPlaylist(playlistId)
        viewModel.getTracks(playlistId)

        viewModel.playlist.observe(viewLifecycleOwner) { playlist ->
            initUi(playlist)
        }

        viewModel.tracks.observe(viewLifecycleOwner) { tracks ->
            adapter.trackList.clear()
            adapter.trackList.addAll(tracks)
            adapter.notifyDataSetChanged()

            binding.fragmentPlaylistMinutesCount.text = resources.getQuantityString(
                R.plurals.plurals_minutes,
                SimpleDateFormat("mm", Locale.getDefault()).format(
                    viewModel.getDurationOfTracks().toInt()
                ).toInt(),
                SimpleDateFormat("mm", Locale.getDefault()).format(
                    viewModel.getDurationOfTracks().toInt()
                ).toInt()
            )


        }
    }

    private fun initUi(playlist: Playlist) {
        with(binding) {
            fragmentPlaylistName.text = playlist.name
            playlistsBottomSheetMenuName.text = playlist.name
            fragmentPlaylistTrackCount.text =
                resources.getQuantityString(
                    R.plurals.plurals_tracks,
                    playlist.countTracks,
                    playlist.countTracks
                )
            playlistsBottomSheetMenuCountTracks.text =
                resources.getQuantityString(
                    R.plurals.plurals_tracks,
                    playlist.countTracks,
                    playlist.countTracks
                )

            fragmentPlaylistYear.text = playlist.description


            if (playlist.imageUri.toString() != "null") {
                fragmentPlaylistImage.setImageURI(playlist.imageUri)
                playlistsBottomSheetMenuImage.setImageURI(playlist.imageUri)
            } else {
                fragmentPlaylistImage.setImage()
                playlistsBottomSheetMenuImage.setImage()
            }
        }
    }

    private fun shareTracks() {
        if (viewModel.isEmptyTracks()) {
            Toast.makeText(
                requireContext(),
                "В этом плейлисте нет списка треков, которым можно поделиться",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val shareString = viewModel.getShareString(resources)
            viewModel.shareTracks(shareString)
        }
    }

    private fun initListeners(playlistId: Long) {

        binding.fragmentPlaylistBackButton.setOnClickListener {
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility =
                View.VISIBLE
            findNavController().navigateUp()
        }

        adapter.trackClickListener = { track ->
            findNavController().navigate(
                R.id.action_playlistFragment_to_trackFragment,
                bundleOf(TrackFragment.ARGS_TRACK to track)
            )
        }

        adapter.trackOnLongClickListener = { track, _ ->
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Хотите удалить трек?") // Заголовок диалога
                .setNegativeButton("Нет") { _, _ -> }
                .setPositiveButton("Да") { _, _ ->
                    viewModel.deleteTrackFromPlaylist(track, playlistId)
                }.show()
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheetMenu).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.fragmentPlaylistOverlay.visibility = View.GONE
                        binding.bottomSheetListTracks.visibility = View.VISIBLE
                    }

                    else -> {
                        binding.fragmentPlaylistOverlay.visibility = View.VISIBLE
                        binding.bottomSheetListTracks.visibility = View.GONE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility =
                    View.VISIBLE

                findNavController().navigateUp()
            }

        })

        binding.fragmentPlaylistShareButton.setOnClickListener {
            shareTracks()
        }

        binding.playlistsBottomSheetMenuEdit.setOnClickListener {
            findNavController().navigate(
                R.id.action_playlistFragment_to_createPlaylistFragment,
                bundleOf(CreatePlaylistFragment.NAV_ARG to playlistId)
            )
        }

        binding.fragmentPlaylistOverlay.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.fragmentPlaylistMoreButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.playlistsBottomSheetMenuShare.setOnClickListener {
            shareTracks()
        }

        binding.playlistsBottomSheetMenuDelete.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Хотите удалить плейлист ${binding.fragmentPlaylistName.text}?") // Заголовок диалога
                .setNegativeButton("Нет") { _, _ -> }
                .setPositiveButton("Да") { _, _ ->
                    viewModel.deletePlayList(playlistId)
                    requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility =
                        View.VISIBLE
                    findNavController().navigateUp()
                }.show()
        }
    }
}