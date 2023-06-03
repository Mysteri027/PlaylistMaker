package com.example.playlistmaker.presentation.search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.track.TrackActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }

    private val trackListAdapter = TrackAdapter()
    private val searchHistoryTrackListAdapter = TrackAdapter()

    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
    private val searchRunnable = Runnable { findTracks() }

    private val viewModel: SearchViewModel by viewModel()


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.screenState.observe(this) { screenState ->
            when (screenState) {
                is SearchScreenState.Loading -> setLoadingState()
                is SearchScreenState.Content -> setContentState(screenState.trackList)
                is SearchScreenState.Error -> setErrorState(screenState.errorType)
                is SearchScreenState.History -> setHistoryState(screenState.trackList)

            }
        }

        setUpListeners()

        binding.trackListRecyclerView.adapter = trackListAdapter
        binding.searchHistoryListRecyclerView.adapter = searchHistoryTrackListAdapter


        viewModel.trackHistory.observe(this) {
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
    }

    private fun setUpListeners() {
        searchHistoryTrackListAdapter.trackClickListener = {
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
            finish()
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
        val trackIntent = Intent(this, TrackActivity::class.java)
        trackIntent.putExtra(TRACK_KEY, track)
        startActivity(trackIntent)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearButtonClicked() {
        binding.searchText.setText("")

        hideKeyboard()
        hidePlaceHolder()
        trackListAdapter.trackList.clear()
        trackListAdapter.notifyDataSetChanged()
    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm: InputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
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
        viewModel.findTracks(binding.searchText.text.toString())
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

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        findViewById<EditText>(R.id.search_text).setText(
            savedInstanceState.getString("SEARCH_TEXT", "")
        )
    }

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val TRACK_KEY = "TRACK_KEY"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DELAY = 2000L
    }
}