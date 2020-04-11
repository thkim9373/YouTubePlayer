package com.hoony.youtubeplayer.data


import com.fasterxml.jackson.annotation.JsonProperty

data class PageInfo(
    @JsonProperty("resultsPerPage")
    val resultsPerPage: Int,
    @JsonProperty("totalResults")
    val totalResults: Int
)