package com.hoony.youtubeplayer.data


import com.fasterxml.jackson.annotation.JsonProperty

data class Id(
    @JsonProperty("kind")
    val kind: String,
    @JsonProperty("videoId")
    val videoId: String
)