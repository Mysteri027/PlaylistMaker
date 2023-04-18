package com.example.playlistmaker.presentation.ui.search

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
import com.example.playlistmaker.data.network.ITunesSearchAPIService
import com.example.playlistmaker.data.repository.LocalStorageRepositoryImpl
import com.example.playlistmaker.data.repository.NetworkRepositoryImpl
import com.example.playlistmaker.data.searchhistory.SearchHistory
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.interactor.LocalStorageInteractor
import com.example.playlistmaker.domain.interactor.NetworkInteractor
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.presenter.search.SearchScreenPresenter
import com.example.playlistmaker.presentation.presenter.search.SearchScreenView
import com.example.playlistmaker.presentation.ui.track.TrackActivity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity(), SearchScreenView {

    private lateinit var binding: ActivitySearchBinding

    private val trackListAdapter = TrackAdapter()
    private val searchHistoryTrackListAdapter = TrackAdapter()

    private val handler = Handler(Looper.getMainLooper())

    private var isClickAllowed = true
    private val searchRunnable = Runnable { findTracks() }

    private lateinit var searchHistory: SearchHistory

    // Retrofit
    private val retrofit =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build()

    private val iTunesSearchAPIService = retrofit.create(ITunesSearchAPIService::class.java)


    private lateinit var presenter: SearchScreenPresenter


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val searchHistorySharedPreferences =
            getSharedPreferences(SEARCH_HISTORY_SHARED_PREFERENCES_KEY, MODE_PRIVATE)
        searchHistory = SearchHistory(searchHistorySharedPreferences)

        val networkRepository = NetworkRepositoryImpl(iTunesSearchAPIService)
        val networkInteractor = NetworkInteractor(networkRepository)
        val localStorageRepository = LocalStorageRepositoryImpl(searchHistory)
        val localStorageInteractor = LocalStorageInteractor(localStorageRepository)

        presenter = SearchScreenPresenter(
            this,
            networkInteractor,
            localStorageInteractor
        )

        searchHistoryTrackListAdapter.trackClickListener = {
            if (clickDebounce()) {
                openTrackScreen(it)
            }
        }

        trackListAdapter.trackClickListener = {
            presenter.addTrackToHistory(it)

            if (clickDebounce()) {
                openTrackScreen(it)
            }

            updateTrackListHistory(searchHistory.getSearchHistory())
        }

        binding.trackListRecyclerView.adapter = trackListAdapter
        binding.searchHistoryListRecyclerView.adapter = searchHistoryTrackListAdapter

        updateTrackListHistory(searchHistory.getSearchHistory())

        binding.searchHistory.visibility =
            if (searchHistoryTrackListAdapter.trackList.isEmpty()) View.GONE else View.VISIBLE

        binding.searchHistoryClearButton.setOnClickListener {
            presenter.clearSearchHistory()
            binding.searchHistory.visibility = View.GONE
        }

        binding.searchTitle.setOnClickListener {
            finish()
        }

        binding.buttonClear.setOnClickListener {
            presenter.clearButtonClicked()
        }

        searchHistorySharedPreferences.registerOnSharedPreferenceChangeListener { _, key ->
            presenter.updateHistory(key, searchHistory.getSearchHistory())
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

    override fun openTrackScreen(track: Track) {
        val trackIntent = Intent(this, TrackActivity::class.java)
        trackIntent.putExtra(TRACK_KEY, track)
        startActivity(trackIntent)
    }

    override fun clearButtonClicked() {
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
        presenter.findTracks(binding.searchText.text.toString())
    }

    override fun setTrackListVisibility(visibility: Int) {
        binding.trackListRecyclerView.visibility = visibility
    }

    override fun setProgressbarListVisibility(visibility: Int) {
        binding.searchProgressBar.visibility = visibility
    }

    override fun hidePlaceHolder() {
        binding.notFoundPlaceholder.visibility = View.GONE
        binding.networkErrorPlaceholder.visibility = View.GONE
    }

    override fun showPlaceHolder(placeHolderType: PlaceHolderType) {
        when (placeHolderType) {
            PlaceHolderType.NOT_FOUND -> {
                binding.notFoundPlaceholder.visibility = View.VISIBLE
            }

            PlaceHolderType.NETWORK_ERROR -> {
                binding.networkErrorPlaceholder.visibility = View.VISIBLE
                binding.updateButton.setOnClickListener {
                    findTracks()
                }
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun updateTrackListHistory(tracks: List<Track>) {
        searchHistoryTrackListAdapter.trackList.clear()
        searchHistoryTrackListAdapter.trackList.addAll(searchHistory.getSearchHistory())
        searchHistoryTrackListAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun updateTrackList(tracks: List<Track>) {
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
        const val BASE_URL = "https://itunes.apple.com"
        const val SEARCH_HISTORY_SHARED_PREFERENCES_KEY = "SEARCH_HISTORY_SHARED_PREFERENCES_KEY"
        const val TRACK_KEY = "TRACK_KEY"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DELAY = 2000L
    }
}