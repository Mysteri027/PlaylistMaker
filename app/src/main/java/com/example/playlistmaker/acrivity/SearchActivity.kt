package com.example.playlistmaker.acrivity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.adapter.TrackAdapter
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

    private var searchInputText = ""

    private val trackList = arrayListOf<Track>()
    private val trackListAdapter = TrackAdapter(trackList)

    private lateinit var updateButton: Button
    private lateinit var networkErrorPlaceholder: LinearLayout
    private lateinit var notFoundErrorPlaceholder: LinearLayout

    // Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesSearchAPIService = retrofit.create(ITunesSearchAPIService::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchText = findViewById<EditText>(R.id.search_text)
        val clearButton = findViewById<ImageView>(R.id.button_clear)
        val searchTitle = findViewById<TextView>(R.id.search_title)
        val trackListRecyclerView = findViewById<RecyclerView>(R.id.track_list_recycler_view)

        updateButton = findViewById(R.id.update_button)
        networkErrorPlaceholder = findViewById(R.id.network_error_placeholder)
        notFoundErrorPlaceholder = findViewById(R.id.not_found_placeholder)

        trackListRecyclerView.adapter = trackListAdapter

        searchTitle.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            searchText.setText("")
            searchInputText = ""

            hideKeyboard()
            hidePlaceHolder()
            trackList.clear()
            trackListAdapter.notifyDataSetChanged()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                clearButton.visibility = clearButtonVisibility(s)

                if (clearButton.visibility == View.VISIBLE) {
                    searchInputText = s.toString()
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        }

        searchText.addTextChangedListener(simpleTextWatcher)

        searchText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                findTracks(searchInputText)
                true
            }
            false
        }
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
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                if (response.code() == 200) {
                    trackList.clear()
                    if (response.body()?.results?.isNotEmpty() == true) {
                        trackList.addAll(response.body()!!.results)
                        trackListAdapter.notifyDataSetChanged()
                    }
                    if (trackList.isEmpty()) {
                        trackList.clear()
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
        notFoundErrorPlaceholder.visibility = View.GONE
        networkErrorPlaceholder.visibility = View.GONE
    }

    private fun showPlaceHolder(placeHolderType: PlaceHolderType) {
        when (placeHolderType) {
            PlaceHolderType.NOT_FOUND -> {
                notFoundErrorPlaceholder.visibility = View.VISIBLE
            }
            PlaceHolderType.NETWORK_ERROR -> {
                networkErrorPlaceholder.visibility = View.VISIBLE
                updateButton.setOnClickListener {
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
    }
}