package com.example.moviestmdb.core.network

import com.example.moviestmdb.PopularActorsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PopularActorsService {

    @GET("person/popular")
    fun getPopularActors(
        @Query("page") page: Int
    ) : Call<PopularActorsResponse>
}