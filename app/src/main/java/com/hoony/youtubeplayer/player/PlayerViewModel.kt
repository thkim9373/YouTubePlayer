package com.hoony.youtubeplayer.player

import android.app.Application
import android.text.TextUtils
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

    private val _mediaInfoListLiveData = MutableLiveData<List<YTMedia>>()
    val mediaInfoListLiveData: LiveData<List<YTMedia>>
        get() = _mediaInfoListLiveData

    private val _subtitleListLiveData = MutableLiveData<List<YTSubtitles>>()
    val subtitleListLiveData: LiveData<List<YTSubtitles>>
        get() = _subtitleListLiveData

    private val _metaDataLiveData = MutableLiveData<YoutubeMeta>()
    val metaDataLiveData: LiveData<YoutubeMeta>
        get() = _metaDataLiveData

    init {
        YoutubeStreamExtractor(this).useDefaultLogin().Extract(videoInfo.id.videoId)
    }

    private fun getVideoUrlLink(videoId: String): String {
        return TextUtils.htmlEncode("https://youtube.com/watch?v=$videoId")
    }

    override fun onExtractionGoesWrong(e: ExtractorException?) {
        
    }

    override fun onExtractionDone(
        adativeStream: MutableList<YTMedia>?,
        muxedStream: MutableList<YTMedia>?,
        subList: MutableList<YTSubtitles>?,
        meta: YoutubeMeta?
    ) {
        this._mediaInfoListLiveData.postValue(adativeStream)
        this._subtitleListLiveData.postValue(subList)
        this._metaDataLiveData.postValue(meta)
    }
}