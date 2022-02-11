package com.techtray.avinashMusic.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.techtray.avinashMusic.model.Song
import com.techtray.avinashMusic.model.SongDao

@Database(entities = [Song::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): SongDao
}