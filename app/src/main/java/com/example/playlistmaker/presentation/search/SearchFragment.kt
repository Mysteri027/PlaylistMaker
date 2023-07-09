package com.example.playlistmaker.presentation.search

import android.annotation.SuppressLint
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.track.TrackFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val trackListAdapter = TrackAdapter()
    private val searchHistoryTrackListAdapter = TrackAdapter()

    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
    private val searchRunnable = Runnable { findTracks() }

    private val viewModel: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is SearchScreenState.Loading -> setLoadingState()
                is SearchScreenState.Content -> setContentState(screenState.trackList)
                is SearchScreenState.Error -> setErrorState(screenState.errorType)
                is SearchScreenState.History -> setHistoryState(screenState.trackList)
            }
            Log.d("SearchScreenState", screenState.toString())
        }

        setUpListeners()

        binding.trackListRecyclerView.adapter = trackListAdapter
        binding.searchHistoryListRecyclerView.adapter = searchHistoryTrackListAdapter


        viewModel.trackHistory.observe(viewLifecycleOwner) {
            updateTrackListHistory(it)
        }

        binding.searchText.setOnFocusChangeListener { _, hasFocus ->
            binding.searchHistory.visibility =
                if (hasFocus && binding.searchText.text.isEmpty() && searchHistoryTrackListAdapter.trackList.isNotEmpty())
                    View.VISIBLE
                else
                    View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(searchRunnable)
        _binding = null
    }

    private fun setUpListeners() {
        searchHistoryTrackListAdapter.trackClickListener = {
            viewModel.addTrackToHistory(it)
            if (clickDebounce()) {
                openTrackScreen(it)
            }
        }

        trackListAdapter.trackClickListener = {
            viewModel.addTrackToHistory(it)

            if (clickDebounce()) {
                openTrackScreen(it)
            }

            updateTrackListHistory(viewModel.getTrackHistoryList())
        }

        binding.searchHistoryClearButton.setOnClickListener {
            viewModel.clearSearchHistory()
            binding.searchHistory.visibility = View.GONE
        }

        binding.searchTitle.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonClear.setOnClickListener {
            clearButtonClicked()
        }

        binding.searchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.buttonClear.visibility = clearButtonVisibility(s)

                if (s!!.isNotEmpty()) {
                    searchDebounce()
                }

                binding.trackListRecyclerView.visibility = View.VISIBLE

                binding.searchHistory.visibility =
                    if (binding.searchText.hasFocus() && s.isEmpty()) View.VISIBLE
                    else View.GONE
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun setLoadingState() {
        hidePlaceHolder()
        setProgressbarListVisibility(View.VISIBLE)
        setTrackListVisibility(View.GONE)
    }

    private fun setContentState(trackList: List<Track>) {
        setProgressbarListVisibility(View.GONE)
        setTrackListVisibility(View.VISIBLE)
        updateTrackList(trackList)
    }

    private fun setErrorState(errorType: ErrorType) {
        setProgressbarListVisibility(View.GONE)
        showPlaceHolder(errorType)
    }

    private fun setHistoryState(trackList: List<Track>) {
        binding.searchHistory.visibility = if (trackList.isEmpty()) View.GONE else View.VISIBLE
        updateTrackListHistory(trackList)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }

        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DELAY)
    }

    private fun openTrackScreen(track: Track) {
        findNavController().navigate(
            R.id.action_searchFragment_to_trackFragment,
            TrackFragment.createArgs(track)
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearButtonClicked() {
        binding.searchText.setText("")

        hideKeyboard()
        hidePlaceHolder()
        viewModel.setHistoryState()
        trackListAdapter.trackList.clear()
        trackListAdapter.notifyDataSetChanged()
    }

    private fun hideKeyboard() {
        val view = requireActivity().currentFocus
        if (view != null) {
            val imm: InputMethodManager =
                requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun findTracks() {
        val searchQuery = binding.searchText.text.toString()

        if (searchQuery.isNotEmpty()) {
            viewModel.findTracks(searchQuery)
        }
    }

    private fun setTrackListVisibility(visibility: Int) {
        binding.trackListRecyclerView.visibility = visibility
    }

    private fun setProgressbarListVisibility(visibility: Int) {
        binding.searchProgressBar.visibility = visibility
    }

    private fun hidePlaceHolder() {
        binding.notFoundPlaceholder.visibility = View.GONE
        binding.networkErrorPlaceholder.visibility = View.GONE
    }

    private fun showPlaceHolder(errorType: ErrorType) {
        when (errorType) {
            ErrorType.NOT_FOUND -> {
                binding.notFoundPlaceholder.visibility = View.VISIBLE
            }

            ErrorType.NETWORK_ERROR -> {
                binding.networkErrorPlaceholder.visibility = View.VISIBLE
                binding.updateButton.setOnClickListener {
                    findTracks()
                }
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateTrackListHistory(tracks: List<Track>) {
        searchHistoryTrackListAdapter.trackList.clear()
        searchHistoryTrackListAdapter.trackList.addAll(tracks)
        searchHistoryTrackListAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateTrackList(tracks: List<Track>) {
        trackListAdapter.trackList.clear()
        trackListAdapter.trackList.addAll(tracks)
        trackListAdapter.notifyDataSetChanged()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, binding.searchText.text.toString())
    }

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DELAY = 2000L
    }
}