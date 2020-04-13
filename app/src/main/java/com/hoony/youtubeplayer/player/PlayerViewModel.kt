package com.hoony.youtubeplayer.player

import android.app.Application
import android.content.Context
import android.util.SparseArray
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.hoony.youtubeplayer.data.VideoInfo
import java.lang.ref.WeakReference

class PlayerViewModel(application: Application, videoInfo: VideoInfo) :
    AndroidViewModel(application) {

    private val _videoUrlStringLiveData = MutableLiveData<String>()
    val videoUrlStringLiveData: LiveData<String>
        get() = _videoUrlStringLiveData

    init {
        Extractor(application, this).extract(getVideoUrlLink(videoInfo.id.videoId), true, true)
    }

    private fun getVideoUrlLink(videoId: String): String {
        return "http://youtube.com/watch?v=$videoId"
    }

    companion object {
        class Extractor(con: Context, viewModel: PlayerViewModel) : YouTubeExtractor(con) {

            private var weakReference: WeakReference<PlayerViewModel> = WeakReference(viewModel)

            override fun onExtractionComplete(
                ytFiles: SparseArray<YtFile>?,
                videoMeta: VideoMeta?
            ) {
                weakReference.get()?._videoUrlStringLiveData?.postValue(ytFiles!![22].url)
            }
        }
    }
}