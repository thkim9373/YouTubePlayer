package com.hoony.youtubeplayer.main

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.hoony.youtubeplayer.databinding.ItemMainVideoListBinding

class MainListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var binding: ItemMainVideoListBinding? = null

    init {
        binding = DataBindingUtil.bind(itemView)
    }
}