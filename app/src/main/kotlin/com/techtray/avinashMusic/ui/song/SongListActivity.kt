package com.techtray.avinashMusic.ui.song

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.techtray.avinashMusic.R
import com.techtray.avinashMusic.injection.ViewModelFactory
import com.techtray.avinashMusic.databinding.ActivitySongListBinding

class SongListActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySongListBinding
    private lateinit var viewModel: SongListViewModel
    private var errorSnackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_song_list)
        binding.postList.layoutManager = LinearLayoutManager(this)

        viewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(SongListViewModel::class.java)
        viewModel.errorMessage.observe(this, Observer {
            errorMessage -> if(errorMessage != null) showError(errorMessage) else hideError()
        })
        binding.viewModel = viewModel
        viewModel.getContext(applicationContext)
    }

    private fun showError(@StringRes errorMessage:Int){
        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackbar?.setAction(R.string.retry, viewModel.errorClickListener)
        errorSnackbar?.show()
    }

    private fun hideError(){
        errorSnackbar?.dismiss()
    }
}