package com.techtray.avinashMusic.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Class which provides a model for song
 * @constructor Sets all properties of the song
 * @property song the identifier of the song
 * @property url the identifier of the song link
 * @property artists the artists of the song
 * @property cover_image the cover image of the song
 */
@Entity
data class Song(
        var song: String, @field:PrimaryKey
        var url: String,
        var artists: String,
        var cover_image: String
)