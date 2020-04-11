package com.hoony.youtubeplayer.data


import com.fasterxml.jackson.annotation.JsonProperty

data class High(
    @JsonProperty("height")
    val height: Int,
    @JsonProperty("url")
    val url: String,
    @JsonProperty("width")
    val width: Int
)