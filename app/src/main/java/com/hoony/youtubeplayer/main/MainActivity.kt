package com.hoony.youtubeplayer.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hoony.youtubeplayer.R
import com.hoony.youtubeplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private var viewModel: MainViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = application!!.let {
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(it))
                .get(MainViewModel::class.java)
        }

        setObserve()
    }

    private fun setObserve() {
        viewModel?.let {
            binding?.let { binding ->
                it.videoDataLiveData.observe(
                    this,
                    Observer { response ->
                        binding.tvText.text = response
                    }
                )
            }
        }
    }
}
