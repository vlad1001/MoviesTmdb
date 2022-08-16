package com.example.moviestmdb.core.network

import com.example.moviestmdb.GenreResponse
import com.example.moviestmdb.MovieCredit
import com.example.moviestmdb.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface MovieService {

    @GET("movie/upcoming")
    fun getUpcoming(
        @Query("page") page: Int
    ) : Call<MovieResponse>

    @GET("movie/top_rated")
    fun getTopRated(
        @Query("page") page: Int
    ) : Call<MovieResponse>

    @GET("movie/popular")
    fun getPopular(
        @Query("page") page: Int
    ) : Call<MovieResponse>

    @GET("movie/now_playing")
    fun getNowPlaying(
        @Query("page") page: Int
    ) : Call<MovieResponse>

    @GET("movie/{movieId}/credits")
    fun getCredits(
        @Path("movieId") id: Int
    ) : Call<MovieCredit>

    @GET("movie/{movieId}/recommendations")
    fun getRecommendations(
        @Path("movieId") id: Int
    ) : Call<MovieResponse>

    @GET("genre/movie/list")
    fun getGenre(
    ) : Call<GenreResponse>

    @GET("discover/movie")
    fun getDiscover(
        @Query("page") page: Int,
        @QueryMap queries : Map<String, String>
    ) : Call<MovieResponse>
}