package com.example.playlistmaker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.model.Track

class TrackAdapter(
    private val trackList: List<Track>
) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
    }

    override fun getItemCount() = trackList.size

    class TrackViewHolder(parentView: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parentView.context)
            .inflate(R.layout.track_item, parentView, false)
    ) {

        private val trackCover = itemView.findViewById<ImageView>(R.id.track_cover)
        private val trackName = itemView.findViewById<TextView>(R.id.track_name)
        private val trackArtist = itemView.findViewById<TextView>(R.id.track_artist_name)
        private val trackTime = itemView.findViewById<TextView>(R.id.track_time)

        fun bind(track: Track) {

            val cornerRadius =
                itemView.resources.getDimensionPixelSize(R.dimen.search_screen_image_corner_radius)

            Glide.with(itemView)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.track_placeholder)
                .centerCrop()
                .transform(RoundedCorners(cornerRadius))
                .into(trackCover)

            trackName.text = track.trackName
            trackArtist.text = track.artistName
            trackTime.text = track.trackTime
        }
    }
}