package com.hoony.youtubeplayer.player

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hoony.youtubeplayer.R
import com.hoony.youtubeplayer.common.ToastPrinter
import com.hoony.youtubeplayer.data.VideoInfo
import com.hoony.youtubeplayer.databinding.ActivityPlayerBinding
import com.hoony.youtubeplayer.video_view.VideoView

class PlayerActivity : AppCompatActivity() {

    private var binding: ActivityPlayerBinding? = null
    private var viewModel: PlayerViewModel? = null
    var audioPlayer: VideoView? = null

    private val builder = StringBuffer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_player)
        viewModel = application!!.let {
            ViewModelProvider(
                this,
                PlayerViewModelFactory(it, intent.getParcelableExtra<VideoInfo?>("videoInfo")!!)
            ).get(PlayerViewModel::class.java)
        }
        builder.append(intent.getParcelableExtra<VideoInfo?>("videoInfo").toString())
        binding?.tvText?.text = builder.toString()

        setView()
        setObserve()
    }

    private fun setView() {
        binding?.let {
            it.textureView.setPlaySpeedMode(VideoView.MODE_BOTTOM_SHEET_FRAGMENT_DIALOG)
        }
    }

    private fun setObserve() {
        viewModel?.let { viewModel ->
            binding?.let { binding ->
                viewModel.mediaInfoListLiveData.observe(
                    this,
                    Observer {
                        if (it != null && it.isNotEmpty()) {
                            builder.append("\n\n")
                                .append("mediaInfoListLiveData : ").append(it.toString())
                            binding.tvText.text = builder.toString()

                            var uri = Uri.parse(it[0].url)
                            var audioUri: Uri = Uri.parse(it[0].url)

                            for (media in it) {
                                if (media.itag == 140) {
                                    audioUri = Uri.parse(media.url)
                                    break
                                }
                            }

                            audioPlayer = VideoView(this)

                            binding.textureView.loadMediaFromUri(uri)
                            this.audioPlayer?.loadMediaFromUri(audioUri)
                        } else {
                            ToastPrinter.show(this, "Media info list is null")
                        }
                    }
                )
                viewModel.subtitleListLiveData.observe(
                    this,
                    Observer {
                        if (it != null) {
                            builder.append("\n\n")
                                .append("subtitleListLiveData : ").append(it.toString())

                            binding.tvText.text = builder.toString()
                        } else {
                            ToastPrinter.show(this, "Subtitle list is null")
                        }
                    }
                )
                viewModel.metaDataLiveData.observe(
                    this,
                    Observer {
                        if (it != null) {
                            builder.append("\n\n")
                                .append("metaDataLiveData : ").append(it.toString())

                            binding.tvText.text = builder.toString()
                        } else {
                            ToastPrinter.show(this, "Meta data is null")
                        }
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding?.let {
            it.textureView.releaseMedia()
        }
        audioPlayer?.releaseMedia()
    }
}