package com.hoony.youtubeplayer.main

import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hoony.youtubeplayer.R
import com.hoony.youtubeplayer.data.VideoInfo
import com.hoony.youtubeplayer.player.PlayerActivity

class MainListAdapter(val videoInfoList: List<VideoInfo>) :
    RecyclerView.Adapter<MainListViewHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListViewHolder {
        context = parent.context
        return MainListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_main_video_list,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return videoInfoList.size
    }

    override fun onBindViewHolder(holder: MainListViewHolder, position: Int) {
        val videoInfo = videoInfoList[position]
        holder.binding?.let {
            val thumbnails = videoInfo.snippet.thumbnails

            val constraintSet = ConstraintSet()
            constraintSet.clone(it.clContainer)
            constraintSet.setDimensionRatio(
                it.tvThumbnail.id,
                String.format("${thumbnails.high.width}:${thumbnails.high.height}")
            )
            constraintSet.applyTo(it.clContainer)

            Glide.with(context)
                .load(thumbnails.high.url)
                .into(it.tvThumbnail)

//            it.tvTitle.text = URLDecoder.decode(videoInfo.snippet.title, "UTF-8")
            it.tvTitle.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(videoInfo.snippet.title, Html.FROM_HTML_MODE_LEGACY).toString()
            } else {
                Html.fromHtml(videoInfo.snippet.title).toString()
            }

            it.clContainer.setOnClickListener {
                run {
                    val intent = Intent(context, PlayerActivity::class.java)
                    intent.putExtra("videoInfo", videoInfo)
                    context.startActivity(intent)
                }
            }
        }
    }
}