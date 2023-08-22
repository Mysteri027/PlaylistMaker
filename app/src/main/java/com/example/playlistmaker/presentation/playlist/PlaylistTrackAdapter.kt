package com.example.playlistmaker.presentation.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.playlistmaker.databinding.TrackItemBinding
import com.example.playlistmaker.domain.model.Track

class PlaylistTrackAdapter : Adapter<PlaylistTrackViewHolder>() {

    var trackList: ArrayList<Track> = arrayListOf()

    var trackClickListener: ((Track) -> Unit)? = null
    var trackOnLongClickListener: ((Track, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistTrackViewHolder {
        val binding = TrackItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistTrackViewHolder(binding)
    }

    override fun getItemCount(): Int = trackList.size

    override fun onBindViewHolder(holder: PlaylistTrackViewHolder, position: Int) {
        val track = trackList[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            trackClickListener?.invoke(track)
        }
        holder.itemView.setOnLongClickListener {
            trackOnLongClickListener?.invoke(track, position)
            true
        }
    }
}