package com.example.playlistmaker.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.SearchHistory
import com.example.playlistmaker.adapter.TrackAdapter
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.model.Track
import com.example.playlistmaker.network.ITunesSearchAPIService
import com.example.playlistmaker.network.PlaceHolderType
import com.example.playlistmaker.network.TrackResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private var searchInputText = ""

    private val trackListAdapter = TrackAdapter()
    private val searchHistoryTrackListAdapter = TrackAdapter()

    private lateinit var searchHistory: SearchHistory

    // Retrofit
    private val retrofit =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build()

    private val iTunesSearchAPIService = retrofit.create(ITunesSearchAPIService::class.java)

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val searchHistorySharedPreferences =
            getSharedPreferences(SEARCH_HISTORY_SHARED_PREFERENCES_KEY, MODE_PRIVATE)

        searchHistory = SearchHistory(searchHistorySharedPreferences)

        searchHistoryTrackListAdapter.trackClickListener = {
            openTrackScreen(it)
        }

        trackListAdapter.trackClickListener = {
            searchHistory.addTrack(it)

            openTrackScreen(it)

            searchHistoryTrackListAdapter.trackList.clear()
            searchHistoryTrackListAdapter.trackList.addAll(searchHistory.getSearchHistory())
            searchHistoryTrackListAdapter.notifyDataSetChanged()
        }

        binding.trackListRecyclerView.adapter = trackListAdapter
        binding.searchHistoryListRecyclerView.adapter = searchHistoryTrackListAdapter

        searchHistoryTrackListAdapter.trackList.clear()
        searchHistoryTrackListAdapter.trackList.addAll(searchHistory.getSearchHistory())
        searchHistoryTrackListAdapter.notifyDataSetChanged()

        binding.searchHistory.visibility =
            if (searchHistoryTrackListAdapter.trackList.isEmpty()) View.GONE else View.VISIBLE

        binding.searchHistoryClearButton.setOnClickListener {
            searchHistory.clear()
            binding.searchHistory.visibility = View.GONE
        }

        binding.searchTitle.setOnClickListener {
            finish()
        }

        binding.buttonClear.setOnClickListener {
            binding.searchText.setText("")
            searchInputText = ""

            hideKeyboard()
            hidePlaceHolder()
            trackListAdapter.trackList.clear()
            trackListAdapter.notifyDataSetChanged()
        }

        searchHistorySharedPreferences.registerOnSharedPreferenceChangeListener { _, key ->
            if (key == SearchHistory.ARRAY_LIST_TRACK_KEY) {
                searchHistoryTrackListAdapter.trackList.clear()
                searchHistoryTrackListAdapter.trackList.addAll(searchHistory.getSearchHistory())
                searchHistoryTrackListAdapter.notifyDataSetChanged()
            }
        }

        binding.searchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.buttonClear.visibility = clearButtonVisibility(s)

                if (binding.buttonClear.visibility == View.VISIBLE) {
                    searchInputText = s.toString()
                }

                binding.searchHistory.visibility =
                    if (binding.searchText.hasFocus() && s?.isEmpty() == true) View.VISIBLE
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

        binding.searchText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                findTracks(searchInputText)
            }
            false
        }
    }

    private fun openTrackScreen(track: Track) {
        val trackIntent = Intent(this, TrackActivity::class.java)
        trackIntent.putExtra(TRACK_KEY, track)
        startActivity(trackIntent)
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

    private fun findTracks(term: String) {
        hidePlaceHolder()
        iTunesSearchAPIService.search(term).enqueue(object : Callback<TrackResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                if (response.code() == 200) {
                    trackListAdapter.trackList.clear()
                    if (response.body()?.results?.isNotEmpty() == true) {
                        val responseList = response.body()?.results ?: emptyList()
                        trackListAdapter.trackList.addAll(responseList)
                        trackListAdapter.notifyDataSetChanged()
                    }
                    if (trackListAdapter.trackList.isEmpty()) {
                        trackListAdapter.trackList.clear()
                        trackListAdapter.notifyDataSetChanged()
                        showPlaceHolder(PlaceHolderType.NOT_FOUND)
                    }
                } else {
                    showPlaceHolder(PlaceHolderType.NETWORK_ERROR)
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                showPlaceHolder(PlaceHolderType.NETWORK_ERROR)
            }

        })
    }

    private fun hidePlaceHolder() {
        binding.notFoundPlaceholder.visibility = View.GONE
        binding.networkErrorPlaceholder.visibility = View.GONE
    }

    private fun showPlaceHolder(placeHolderType: PlaceHolderType) {
        when (placeHolderType) {
            PlaceHolderType.NOT_FOUND -> {
                binding.notFoundPlaceholder.visibility = View.VISIBLE
            }
            PlaceHolderType.NETWORK_ERROR -> {
                binding.networkErrorPlaceholder.visibility = View.VISIBLE
                binding.updateButton.setOnClickListener {
                    findTracks(searchInputText)
                }
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchInputText)
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
    }
}