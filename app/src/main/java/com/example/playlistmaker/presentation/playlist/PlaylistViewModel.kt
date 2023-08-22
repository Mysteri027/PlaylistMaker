package com.example.playlistmaker.presentation.playlist

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.interactor.PlaylistInteractor
import com.example.playlistmaker.domain.interactor.SharingInteractor
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private val _playlist = MutableLiveData<Playlist>()
    val playlist: LiveData<Playlist> = _playlist

    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> = _tracks

    fun getPlaylist(id: Long) {
        viewModelScope.launch {
            playlistInteractor.getPlaylistById(id).collect { playlist ->
                _playlist.postValue(playlist)
            }
        }
    }

    fun getTracks(id: Long) {
        viewModelScope.launch {
            playlistInteractor.getTracksFromPlaylist(id).collect {
                _tracks.postValue(it)
            }
        }
    }

    fun getDurationOfTracks(): Long {
        var duration = 0L
        tracks.value?.forEach { track ->
            duration += track.trackTimeMillis.toLong()
        }

        return duration
    }

    fun deleteTrackFromPlaylist(track: Track, playlistId: Long) {
        viewModelScope.launch {
            playlistInteractor.deleteTrackFromPlaylist(track.trackId, playlistId)
                .collect { playlist ->
                    _playlist.postValue(playlist)
                    playlistInteractor.getTracksFromPlaylist(playlistId).collect { tracks ->
                        _tracks.postValue(tracks)
                    }

                }
        }
    }

    fun isEmptyTracks(): Boolean {
        return tracks.value?.isEmpty() ?: true
    }

    fun getShareString(resources: Resources): String {
        var shareString =
            playlist.value?.name + "\n" +
            playlist.value?.description + "\n" +
            resources.getQuantityString(R.plurals.plurals_tracks, tracks.value?.size ?: 0, tracks.value?.size ?: 0) + "\n"

        Log.d("playlist.value?.description", playlist.value?.description.toString())
        tracks.value?.forEachIndexed { index, track ->
            shareString += "${index + 1}. ${track.artistName} - ${track.trackName} (" + resources.getQuantityString(
                R.plurals.plurals_minutes,
                SimpleDateFormat("mm", Locale.getDefault()).format(track.trackTimeMillis.toLong())
                    .toInt(),
                SimpleDateFormat("mm", Locale.getDefault()).format(track.trackTimeMillis.toLong())
                    .toInt()
            ) + ") \n"
        }

        return shareString
    }

    fun shareTracks(tracks: String) {
        sharingInteractor.shareApp(tracks)
    }

    fun deletePlayList(playlistId: Long) {
        viewModelScope.launch {
            playlistInteractor.deletePlaylist(playlistId)
        }
    }
}