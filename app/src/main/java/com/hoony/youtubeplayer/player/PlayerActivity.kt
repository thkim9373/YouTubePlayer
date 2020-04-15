package com.hoony.youtubeplayer.player

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hoony.youtubeplayer.R
import com.hoony.youtubeplayer.data.VideoInfo
import com.hoony.youtubeplayer.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {

    var binding: ActivityPlayerBinding? = null
    var viewModel: PlayerViewModel? = null

    val builder = StringBuffer()

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

        setObserve()
    }

    private fun setObserve() {
        viewModel?.let { viewModel ->
            binding?.let { binding ->
                viewModel.mediaInfoListLiveData.observe(
                    this,
                    Observer {
                        builder.append("\n\n")
                            .append("mediaInfoListLiveData : ").append(it.toString())

                        binding.tvText.text = builder.toString()
                    }
                )
                viewModel.subtitleListLiveData.observe(
                    this,
                    Observer {
                        builder.append("\n\n")
                            .append("subtitleListLiveData : ").append(it.toString())

                        binding.tvText.text = builder.toString()
                    }
                )
                viewModel.metaDataLiveData.observe(
                    this,
                    Observer {
                        builder.append("\n\n")
                            .append("metaDataLiveData : ").append(it.toString())

                        binding.tvText.text = builder.toString()
                    }
                )
            }
        }
    }
}