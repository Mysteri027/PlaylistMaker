package com.example.playlistmaker.domain.model

import android.net.Uri

data class Playlist(
    val id: Long = 0,
    val name: String,
    val description: String,
    val imageUri: Uri? = null,
    var trackList: String,
    var countTracks: Int,
)