package com.hoony.youtubeplayer.player

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hoony.youtubeplayer.data.VideoInfo
import com.hoony.youtubeplayer.extractor.ExtractorException
import com.hoony.youtubeplayer.extractor.YoutubeStreamExtractor
import com.hoony.youtubeplayer.extractor.model.YTMedia
import com.hoony.youtubeplayer.extractor.model.YTSubtitles
import com.hoony.youtubeplayer.extractor.model.YoutubeMeta

class PlayerViewModel(application: Application, videoInfo: VideoInfo) :
    AndroidViewModel(application), YoutubeStreamExtractor.ExtractorListener {

    private val _videoUrlStringLiveData = MutableLiveData<String>()
    val videoUrlStringLiveData: LiveData<String>
        get() = _videoUrlStringLiveData

    init {
//        Extractor(application, this).extract(getVideoUrlLink(videoInfo.id.videoId), true, true)

        YoutubeStreamExtractor(this).useDefaultLogin().Extract(videoInfo.id.videoId)
    }

    override fun onExtractionGoesWrong(e: ExtractorException?) {
        println(e)
    }

    override fun onExtractionDone(
        adativeStream: MutableList<YTMedia>?,
        muxedStream: MutableList<YTMedia>?,
        subList: MutableList<YTSubtitles>?,
        meta: YoutubeMeta?
    ) {
        val subString = if (subList != null && subList.size >= 1) {
            subList[0].baseUrl
        } else {
            null
        }
    }
}