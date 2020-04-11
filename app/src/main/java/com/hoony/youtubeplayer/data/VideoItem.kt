package com.hoony.youtubeplayer.data

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class VideoItem(
    @JsonProperty("kind") val kind: String,
    val etag: String,
    val id: ItemId,
    val snippet: Snippet
) {
    data class ItemId(
        val kind: String,
        val videoId: String
    )

    data class Snippet(
        val publishedAt: Date,
        val channelId: String,
        val title: String,
        val description: String,
        val thumbnails: HashMap<String, Thumbnail>
    ) {
        data class Thumbnail(
            val urlString: String,
            val width: Int,
            val height: Int
        )
    }
}