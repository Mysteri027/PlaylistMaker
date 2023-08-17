package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.TrackEntity

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(trackEntity: TrackEntity)

    @Query("DELETE FROM track_table WHERE trackId = :id")
    suspend fun deleteTrack(id: Long)

    @Query("SELECT * FROM track_table ORDER BY id DESC")
    suspend fun selectAllTracks(): List<TrackEntity>


    @Query("SELECT * FROM track_table WHERE trackId = :id")
    suspend fun getTracksById(id: Long): List<TrackEntity>


    @Query("SELECT trackId FROM track_table")
    suspend fun selectAllIdTracks(): List<Long>
}