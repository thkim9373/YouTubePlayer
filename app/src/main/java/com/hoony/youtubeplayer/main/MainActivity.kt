package com.hoony.youtubeplayer.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
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

        setView()
        setObserve()
    }

    private fun setView() {
        binding?.let {
            it.rvVideo.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            it.rvVideo.addItemDecoration(
                DividerItemDecoration(
                    this,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun setObserve() {
        viewModel?.let { viewModel ->
            binding?.let { binding ->
                //                it.videoDataLiveData.observe(
//                    this,
//                    Observer { response ->
//                        binding.tvText.text = response
//                    }
//                )
                viewModel.videoInfoListLiveData.observe(
                    this,
                    Observer {
                        binding.rvVideo.adapter = MainListAdapter(it)
                    }
                )
            }
        }
    }
}
