package com.hoony.youtubeplayer.data


import com.fasterxml.jackson.annotation.JsonProperty

data class YouTubeResponse(
    @JsonProperty("etag")
    val etag: String,
    @JsonProperty("items")
    val items: List<Item>,
    @JsonProperty("kind")
    val kind: String,
    @JsonProperty("nextPageToken")
    val nextPageToken: String?,
    @JsonProperty("prevPageToken")
    val prevPageToken: String?,
    @JsonProperty("pageInfo")
    val pageInfo: PageInfo,
    @JsonProperty("regionCode")
    val regionCode: String
)