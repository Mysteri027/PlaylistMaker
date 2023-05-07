package com.example.playlistmaker.presentation.search

import com.example.playlistmaker.domain.model.Track

sealed class SearchScreenState {
    object Loading : SearchScreenState()
    class Content(val trackList: List<Track>) : SearchScreenState()
    class History(val trackList: List<Track>) : SearchScreenState()
    class Error(val errorType: ErrorType) : SearchScreenState()

}

enum class ErrorType {
    NOT_FOUND,
    NETWORK_ERROR
}