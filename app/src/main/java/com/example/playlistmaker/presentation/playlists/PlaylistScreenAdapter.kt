package com.example.playlistmaker.presentation.playlists

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistItemBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.utils.inflectTrack
import com.example.playlistmaker.utils.setImage

class PlaylistScreenAdapter : Adapter<PlaylistScreenViewHolder>() {

    val playlists: ArrayList<Playlist> = arrayListOf()
    var onPlaylistClicked: ((Playlist) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistScreenViewHolder {
        val binding =
            PlaylistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistScreenViewHolder(binding)
    }

    override fun getItemCount(): Int = playlists.size

    override fun onBindViewHolder(holder: PlaylistScreenViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.itemView.setOnClickListener {
            onPlaylistClicked?.invoke(playlist)
        }
        holder.bind(playlists[position])
    }
}

class PlaylistScreenViewHolder(private val binding: PlaylistItemBinding) :
    ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(playList: Playlist) {

        val cornerRadius =
            itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_8dp)

        if (playList.imageUri.toString() == "null") {
            binding.playlistItemImage.setImage(cornerRadius)
        } else {
            binding.playlistItemImage.setImage(playList.imageUri!!, cornerRadius)
        }

        binding.playlistItemName.text = playList.name
        binding.playlistItemTracksCount.text = inflectTrack(playList.countTracks)
    }
}