package com.hoony.youtubeplayer.data

import com.google.gson.annotations.SerializedName

data class VideoInfo(
    @SerializedName("etag")
    val etag: String,
    @SerializedName("id")
    val id: Id,
    @SerializedName("kind")
    val kind: String,
    @SerializedName("snippet")
    val snippet: Snippet
)