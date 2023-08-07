package com.example.playlistmaker.data.mapper

import com.example.playlistmaker.data.db.TrackEntity
import com.example.playlistmaker.domain.mapper.BaseMapper
import com.example.playlistmaker.domain.model.Track

class DatabaseMapperToTrack: BaseMapper<TrackEntity, Track>() {
    override fun map(from: TrackEntity): Track {
        return Track(
            trackId = from.trackId,
            trackName = from.trackName,
            artistName = from.artistName,
            trackTimeMillis = from.trackTimeMillis,
            artworkUrl100 = from.artworkUrl100,
            collectionName = from.collectionName,
            releaseDate = from.releaseDate,
            primaryGenreName = from.primaryGenreName,
            country = from.country,
            previewUrl = from.previewUrl,
            isFavorite = true
        )
    }
}

class DatabaseMapperToEntity: BaseMapper<Track, TrackEntity>() {
    override fun map(from: Track): TrackEntity {
        return TrackEntity(
            trackId = from.trackId,
            trackName = from.trackName,
            artistName = from.artistName,
            trackTimeMillis = from.trackTimeMillis,
            artworkUrl100 = from.artworkUrl100,
            collectionName = from.collectionName,
            releaseDate = from.releaseDate,
            primaryGenreName = from.primaryGenreName,
            country = from.country,
            previewUrl = from.previewUrl,
        )
    }
}