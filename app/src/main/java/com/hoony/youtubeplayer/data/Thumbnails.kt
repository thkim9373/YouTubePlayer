package com.hoony.youtubeplayer.data


import com.fasterxml.jackson.annotation.JsonProperty

data class Thumbnails(
    @JsonProperty("default")
    val default: Default,
    @JsonProperty("high")
    val high: High,
    @JsonProperty("medium")
    val medium: Medium
)