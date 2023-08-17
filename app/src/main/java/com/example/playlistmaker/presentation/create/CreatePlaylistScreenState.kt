package com.example.playlistmaker.presentation.create

sealed class CreatePlaylistScreenState() {
    object Empty : CreatePlaylistScreenState()
    object Filled : CreatePlaylistScreenState()
}