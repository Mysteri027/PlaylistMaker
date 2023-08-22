package com.example.playlistmaker.data.mapper

import androidx.core.net.toUri
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.domain.mapper.BaseMapper
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track

class DatabaseMapperToTrack : BaseMapper<TrackEntity, Track>() {
    override fun map(from: TrackEntity): Track {
        return Track(
            trackId = from.trackId,
            trackName = from.trackName,
            artistName = from.artistName,
            trackTimeMillis = from.trackTimeMillis,
            artworkUrl100 = from.artworkUrl100,
            artworkUrl60 = from.artworkUrl60,
            collectionName = from.collectionName,
            releaseDate = from.releaseDate,
            primaryGenreName = from.primaryGenreName,
            country = from.country,
            previewUrl = from.previewUrl,
            isFavorite = true
        )
    }
}

class DatabaseMapperToEntity : BaseMapper<Track, TrackEntity>() {
    override fun map(from: Track): TrackEntity {
        return TrackEntity(
            trackId = from.trackId,
            trackName = from.trackName,
            artistName = from.artistName,
            trackTimeMillis = from.trackTimeMillis,
            artworkUrl100 = from.artworkUrl100,
            artworkUrl60 = from.artworkUrl60,
            collectionName = from.collectionName,
            releaseDate = from.releaseDate,
            primaryGenreName = from.primaryGenreName,
            country = from.country,
            previewUrl = from.previewUrl,
        )
    }
}

class DatabaseMapperPlayListToEntity : BaseMapper<Playlist, PlaylistEntity>() {
    override fun map(from: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = from.id,
            name = from.name,
            description = from.description,
            imageUri = from.imageUri.toString(),
            trackList = from.trackList,
            countTracks = from.countTracks
        )
    }
}

class DatabaseMapperPlayListToModel : BaseMapper<PlaylistEntity, Playlist>() {
    override fun map(from: PlaylistEntity): Playlist {
        return Playlist(
            id = from.id,
            name = from.name,
            description = from.description,
            imageUri = from.imageUri.toUri(),
            trackList = from.trackList,
            countTracks = from.countTracks
        )
    }
}