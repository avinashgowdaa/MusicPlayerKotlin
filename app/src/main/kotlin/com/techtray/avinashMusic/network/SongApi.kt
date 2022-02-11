package com.techtray.avinashMusic.network

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import io.reactivex.Observable
import com.techtray.avinashMusic.model.Song
import retrofit2.http.GET

/**
 * The interface which provides methods to get result of webservices
 */
interface SongApi {
    /**
     * Get the list of the pots from the API
     */
    @GET("/studio")
    fun getPosts(): Observable<List<Song>>

    fun getSongs(context: Context): Observable<List<Song>> {
        val tempAudioList: List<Song> = ArrayList()
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf<String>(
            MediaStore.Audio.AudioColumns.DATA,
            MediaStore.Audio.AudioColumns.TITLE,
            MediaStore.Audio.ArtistColumns.ARTIST
        )
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        /*val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION
        )*/
        val c: Cursor? = context.contentResolver.query(
            uri, projection, selection, null, null
        )
        if (c != null) {
            while (c.moveToNext()) {
                val path = c.getString(0)
                val name = c.getString(1)
                val artist = c.getString(2)
                val audioModel = Song(name, artist, path, "")
                if (path != null && (path.endsWith(".aac")
                            || path.endsWith(".mp3")
                            || path.endsWith(".wav")
                            || path.endsWith(".ogg")
                            || path.endsWith(".ac3")
                            || path.endsWith(".m4a"))
                ) {

                    // Log.v(mTAG, "trackName :${audioModel.trackName}, trackDetail :${audioModel.trackDetail}, trackUrl :${audioModel.trackUrl}")
                    tempAudioList.toMutableList().add(audioModel)
                }
            }
            c.close()
        }

        val songs: Observable<List<Song>> = Observable.fromArray(tempAudioList)
        return songs
    }
}