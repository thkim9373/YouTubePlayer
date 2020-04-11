package com.hoony.youtubeplayer.data


import com.fasterxml.jackson.annotation.JsonProperty

data class Item(
    @JsonProperty("etag")
    val etag: String,
    @JsonProperty("id")
    val id: Id,
    @JsonProperty("kind")
    val kind: String,
    @JsonProperty("snippet")
    val snippet: Snippet
)