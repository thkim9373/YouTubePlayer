package com.hoony.youtubeplayer.player

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.hoony.youtubeplayer.R
import com.hoony.youtubeplayer.data.VideoInfo
import com.hoony.youtubeplayer.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {

    var binding: ActivityPlayerBinding? = null
    var viewModel: PlayerViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_player)
        viewModel = application!!.let {
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(it))
                .get(PlayerViewModel::class.java)
        }
        
        binding?.tvText?.text = intent.getParcelableExtra<VideoInfo?>("videoInfo").toString()
    }
}