package com.example.playlistmaker.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackItemBinding
import com.example.playlistmaker.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackAdapter : RecyclerView.Adapter<TrackViewHolder>() {

    var trackList: ArrayList<Track> = arrayListOf()

    var trackClickListener: ((Track) -> Unit)? = null
    var trackOnLongClickListener: ((Track, Int) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = TrackItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
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

    override fun getItemCount() = trackList.size
}

class TrackViewHolder(private val binding: TrackItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(track: Track) {

        val cornerRadius =
            itemView.resources.getDimensionPixelSize(R.dimen.search_screen_image_corner_radius)

        Glide.with(itemView).load(track.artworkUrl100).placeholder(R.drawable.track_placeholder)
            .centerCrop().transform(RoundedCorners(cornerRadius)).into(binding.trackCover)

        binding.trackName.text = track.trackName
        binding.trackArtistName.text = track.artistName
        binding.trackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toLong())
    }
}