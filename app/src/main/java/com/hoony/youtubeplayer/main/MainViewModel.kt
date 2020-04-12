package com.hoony.youtubeplayer.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.hoony.youtubeplayer.common.Key
import com.hoony.youtubeplayer.data.YouTubeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val YOUTUBE_URL = "https://www.googleapis.com/youtube/v3/search?" +
            "key=" + Key.YOUTUBE_KEY +
            "&part=snippet" +
            "&maxResult=10"

    private val _videoDataMutableLiveData = MutableLiveData<String>()

    val videoDataLiveData: LiveData<String>
        get() = _videoDataMutableLiveData

    private var nextPageToken: String? = null
    private var prevPageToken: String? = null

    init {
        viewModelScope.launch {
            try {
                val responseJSONObject = getYouTubeJsonObject()
                _videoDataMutableLiveData.postValue(responseJSONObject.toString())

                responseJSONObject?.let {
                    val gson = Gson()
                    val youTubeResponse = gson.fromJson(it.toString(), YouTubeResponse::class.java)

                    nextPageToken = youTubeResponse.nextPageToken
                    prevPageToken = youTubeResponse.prevPageToken
                }
            } catch (e: Exception) {
                println(e)
                _videoDataMutableLiveData.postValue("Exception : $e")
            }
        }
    }

    private suspend fun getYouTubeJsonObject(): JSONObject? = withContext(Dispatchers.IO) {
        val url = URL(YOUTUBE_URL)
        val inputStream = url.openStream()
        var result: JSONObject? = null

        inputStream.use {
            val reader = BufferedReader(InputStreamReader(it))
            val content = StringBuilder()
            reader.use {
                var line = it.readLine()
                while (line != null) {
                    content.append(line)
                    line = it.readLine()
                }
                val inputString = content.toString()
                result = JSONObject(inputString)
            }
        }

        result
    }
}