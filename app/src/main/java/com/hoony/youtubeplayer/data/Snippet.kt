package com.hoony.youtubeplayer.data


import com.fasterxml.jackson.annotation.JsonProperty

data class Snippet(
    @JsonProperty("channelId")
    val channelId: String,
    @JsonProperty("channelTitle")
    val channelTitle: String,
    @JsonProperty("description")
    val description: String,
    @JsonProperty("liveBroadcastContent")
    val liveBroadcastContent: String,
    @JsonProperty("publishedAt")
    val publishedAt: String,
    @JsonProperty("thumbnails")
    val thumbnails: Thumbnails,
    @JsonProperty("title")
    val title: String
)