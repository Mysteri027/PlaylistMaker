package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity)

    @Query("DELETE FROM playlist_table WHERE id = :id")
    suspend fun deletePlaylist(id: Long)

    @Update
    suspend fun updatePlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlist_table ORDER BY id DESC;")
    suspend fun getSavedPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlist_table WHERE id = :id")
    suspend fun getPlaylistById(id: Long): PlaylistEntity

    @Query("SELECT trackList FROM playlist_table WHERE id = :id")
    suspend fun getTracksFromPlaylist(id: Long): String
}