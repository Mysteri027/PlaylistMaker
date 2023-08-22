package com.example.playlistmaker.presentation.playlist

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackItemBinding
import com.example.playlistmaker.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistTrackViewHolder(private val binding: TrackItemBinding) : ViewHolder(binding.root) {
    fun bind(track: Track) {
        val cornerRadius =
            itemView.resources.getDimensionPixelSize(R.dimen.search_screen_image_corner_radius)

        Glide.with(itemView).load(track.artworkUrl60).placeholder(R.drawable.track_placeholder)
            .centerCrop().transform(RoundedCorners(cornerRadius)).into(binding.trackCover)

        binding.trackName.text = track.trackName
        binding.trackArtistName.text = track.artistName
        binding.trackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toLong())
    }
}