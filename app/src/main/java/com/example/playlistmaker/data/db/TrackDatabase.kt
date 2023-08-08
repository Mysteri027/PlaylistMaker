package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 2,
    entities = [TrackEntity::class],
)
abstract class TrackDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao
}