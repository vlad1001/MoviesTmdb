package com.example.moviestmdb

data class PopularActorsResponse(
    val page: Int,
    val results: List<PopularActor>,
    val total_pages: Int,
    val total_results: Int
)