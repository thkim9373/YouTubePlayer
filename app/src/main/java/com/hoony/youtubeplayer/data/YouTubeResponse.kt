package com.hoony.youtubeplayer.data

import com.google.gson.annotations.SerializedName

data class YouTubeResponse(
    @SerializedName("etag")
    val etag: String,
    @SerializedName("items")
    val videoInfoList: List<VideoInfo>,
    @SerializedName("kind")
    val kind: String,
    @SerializedName("nextPageToken")
    val nextPageToken: String?,
    @SerializedName("prevPageToken")
    val prevPageToken: String?,
    @SerializedName("pageInfo")
    val pageInfo: PageInfo,
    @SerializedName("regionCode")
    val regionCode: String
)