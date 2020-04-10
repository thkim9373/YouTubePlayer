package com.hoony.youtubeplayer.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoony.youtubeplayer.common.Key
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.net.URL

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val YOUTUBE_URL =
        "https://www.googleapis.com/youtube/v3/search?key=" + Key.YOUTUBE_KEY + "&part=snippet"

    private val _videoDataMutableLiveData = MutableLiveData<String>()

    val videoDataLiveData: LiveData<String>
        get() = _videoDataMutableLiveData

    init {
        viewModelScope.launch {
            try {
                _videoDataMutableLiveData.postValue(getYouTubeInputStream())
            } catch (e: Exception) {
                println(e)
                _videoDataMutableLiveData.postValue("Exception : $e")
            }
        }
    }

    private suspend fun getYouTubeInputStream(): String = withContext(Dispatchers.IO) {
        val url = URL(YOUTUBE_URL)
        val inputStream = url.openStream()
        var result = "init value"

        inputStream.use {
            val reader = BufferedReader(inputStream.reader())
            val content = StringBuilder()
            try {
                var line = reader.readLine()
                while (line != null) {
                    content.append(line)
                    line = reader.readLine()
                }
                result = content.toString()
            } finally {
                reader.close()
            }
        }

        result
    }
}