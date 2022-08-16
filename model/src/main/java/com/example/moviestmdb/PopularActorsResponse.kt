package com.example.moviestmdb

import com.google.gson.annotations.SerializedName

data class PopularActorsResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<PopularActor>,
    @SerializedName("total_pages")
    val total_pages: Int,
    @SerializedName("total_results")
    val total_results: Int
)