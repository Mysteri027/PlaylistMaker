package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {

    private var searchInputText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchText = findViewById<EditText>(R.id.search_text)
        val clearButton = findViewById<ImageView>(R.id.button_clear)
        val searchTitle = findViewById<TextView>(R.id.search_title)

        searchTitle.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            searchText.setText("")
            searchInputText = ""

            // убираем клавиатуру
            val view = this.currentFocus
            if (view != null) {
                val imm: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                clearButton.visibility = clearButtonVisibility(s)

                if (clearButton.visibility == View.VISIBLE) {
                    searchInputText = s.toString()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                // empty
            }

        }

        searchText.addTextChangedListener(simpleTextWatcher)
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

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }

}