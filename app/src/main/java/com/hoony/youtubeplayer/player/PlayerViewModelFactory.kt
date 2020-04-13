package com.hoony.youtubeplayer.player

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hoony.youtubeplayer.data.VideoInfo

class PlayerViewModelFactory(
    private val application: Application,
    private val videoInfo: VideoInfo
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PlayerViewModel(application, videoInfo) as T
    }
}