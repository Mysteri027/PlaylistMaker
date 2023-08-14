package com.example.playlistmaker.presentation.track

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistItemBottomSheetBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.utils.setImage

class PlaylistAdapter : RecyclerView.Adapter<PlaylistViewHolder>() {

    val playlists: ArrayList<Playlist> = arrayListOf()

    var onPlayListClicked: ((playlist: Playlist) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = PlaylistItemBottomSheetBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return PlaylistViewHolder(binding)
    }

    override fun getItemCount(): Int = playlists.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.binding.root.setOnClickListener {
            onPlayListClicked?.invoke(playlist)
        }
        holder.bind(playlist)
    }
}

class PlaylistViewHolder(val binding: PlaylistItemBottomSheetBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(playlist: Playlist) {

        val cornerRadius =
            itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_8dp)


        if (playlist.imageUri.toString() == "null") {
            binding.cover.setImage("", cornerRadius)
        } else {
            binding.cover.setImage(playlist.imageUri!!, cornerRadius)
        }

        binding.playlistName.text = playlist.name
        binding.trakcsCount.text = "${playlist.countTracks} треков"
    }

}