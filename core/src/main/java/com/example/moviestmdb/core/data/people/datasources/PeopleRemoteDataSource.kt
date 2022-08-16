package com.example.moviestmdb.core.data.people.datasources

import com.example.moviestmdb.MovieResponse
import com.example.moviestmdb.PopularActorsResponse
import com.example.moviestmdb.core.extensions.executeWithRetry
import com.example.moviestmdb.core.extensions.toResult
import com.example.moviestmdb.core.network.MovieService
import com.example.moviestmdb.core.network.PopularActorsService
import com.example.moviestmdb.core.result.Result
import com.example.moviestmdb.core.util.safeApiCall
import javax.inject.Inject

class PeopleRemoteDataSource @Inject constructor(
    private val poplarActorsService: PopularActorsService
){
    suspend fun getPopularActors(page: Int): Result<PopularActorsResponse> {
        return safeApiCall {
            poplarActorsService
                .getPopularActors(page)
                .executeWithRetry()
                .toResult()
        }
    }
}