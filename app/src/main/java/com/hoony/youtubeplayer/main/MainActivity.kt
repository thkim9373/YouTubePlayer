package com.hoony.youtubeplayer.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hoony.youtubeplayer.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var viewModel: MainViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = application!!.let {
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(it))
                .get(MainViewModel::class.java)
        }

        setObserve()
    }

    private fun setObserve() {
        viewModel?.let {
            it.videoDataLiveData.observe(
                this,
                Observer { response ->
                    tv_text.text = response
                }
            )
        }
    }
}
