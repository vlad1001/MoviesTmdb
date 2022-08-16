package com.example.moviestmdb.core.data.people

import com.example.moviestmdb.Movie
import com.example.moviestmdb.PopularActor
import com.example.moviestmdb.PopularActorsResponse
import com.example.moviestmdb.core.data.people.datasources.PeopleLocalDataSource
import com.example.moviestmdb.core.data.people.datasources.PeopleRemoteDataSource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PeopleRepository @Inject constructor(
    private val local: PeopleLocalDataSource,
    private val remote: PeopleRemoteDataSource
) {

    suspend fun getPopularActors(page: Int) =
        flow {
            emit(remote.getPopularActors(page))
        }

    fun savePopularActors(page: Int, popularActors: List<PopularActor>) {
        local.popularActorStore.insert(page, popularActors)
    }

    fun observePopularActors() = local.popularActorStore.observePopularActors()
}