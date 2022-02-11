package com.techtray.avinashMusic.ui.song

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import com.techtray.avinashMusic.R
import com.techtray.avinashMusic.base.BaseViewModel
import com.techtray.avinashMusic.model.Song
import com.techtray.avinashMusic.model.SongDao
import com.techtray.avinashMusic.network.SongApi
import javax.inject.Inject

class SongListViewModel(private val songDao: SongDao) : BaseViewModel() {

    @Inject
    lateinit var songApi: SongApi
    lateinit var context: Context
    val postListAdapter: SongListAdapter = SongListAdapter()
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadPosts() }

    private lateinit var subscription: Disposable

    init {
       loadSongs(context)
       // loadPosts()
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    fun getContext(context: Context) {
        this.context = context
    }

    private fun loadPosts() {
        Log.d("test", "Loading Posts....  ")

        subscription = Observable.fromCallable { songDao.all }
                .concatMap { dbPostList ->
                    if (dbPostList.isEmpty()) {
                        songApi.getPosts().concatMap { apiPostList ->
                            songDao.insertAll(*apiPostList.toTypedArray())
                            Observable.just(apiPostList)
                        }
                    } else {
                        Log.d("test", "loaded Posts....  ")
                        Log.d("test", "loaded Posts song....  " + dbPostList[0].song)

                        Observable.just(dbPostList)
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrievePostListStart() }
                .doOnTerminate { onRetrievePostListFinish() }
                .subscribe(
                        { result -> onRetrievePostListSuccess(result) },
                        { onRetrievePostListError() }
                )
    }

    fun loadSongs(context: Context) {
        Log.d("test", "Loading Songs....  ")
        subscription = Observable.fromCallable { songDao.all }
            .concatMap { dbPostList ->
                if (dbPostList.isEmpty()) {
                    songApi.getSongs(context).concatMap { apiPostList ->
                        songDao.insertAll(*apiPostList.toTypedArray())
                        Observable.just(apiPostList)
                    }
                } else {
                    Log.d("test", "loaded Songs....  " + dbPostList[0].song)

                    Observable.just(dbPostList)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrievePostListStart() }
            .doOnTerminate { onRetrievePostListFinish() }
            .subscribe(
                { result -> onRetrievePostListSuccess(result) },
                { onRetrievePostListError() }
            )
    }

    private fun onRetrievePostListStart() {
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
    }

    private fun onRetrievePostListFinish() {
        loadingVisibility.value = View.GONE
    }

    private fun onRetrievePostListSuccess(songList: List<Song>) {
        Log.d("test", "post list success Posts....  " + songList.get(0).song)
        postListAdapter.updateSongList(songList)
    }

    private fun onRetrievePostListError() {
        errorMessage.value = R.string.post_error
    }
}