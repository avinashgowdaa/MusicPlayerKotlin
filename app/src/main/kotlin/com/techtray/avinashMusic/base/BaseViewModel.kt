package com.techtray.avinashMusic.base

import androidx.lifecycle.ViewModel
import com.techtray.avinashMusic.injection.component.DaggerViewModelInjector
import com.techtray.avinashMusic.injection.component.ViewModelInjector
import com.techtray.avinashMusic.injection.module.NetworkModule
import com.techtray.avinashMusic.ui.song.SongListViewModel
import com.techtray.avinashMusic.ui.song.SongViewModel

abstract class BaseViewModel:ViewModel(){
    private val injector: ViewModelInjector = DaggerViewModelInjector
            .builder()
            .networkModule(NetworkModule)
            .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is SongListViewModel -> injector.inject(this)
            is SongViewModel -> injector.inject(this)
        }
    }
}