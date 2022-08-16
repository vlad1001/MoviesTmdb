package com.example.moviestmdb.domain

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.moviestmdb.PopularActor
import com.example.moviestmdb.core.data.people.PeopleStore

@OptIn(ExperimentalPagingApi::class)
class PaginatedPopularActorsRemoteMediator(
    private val popularActorStore: PeopleStore,
    private val fetch: suspend (page: Int) -> Unit
) : RemoteMediator<Int, PopularActor>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PopularActor>
    ): MediatorResult {
        val nextPage = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastPage = popularActorStore.getLastPage()
                lastPage + 1
            }
        }
        return try {
            fetch(nextPage)
            MediatorResult.Success(endOfPaginationReached = false)
        } catch (t: Throwable) {
            MediatorResult.Error(t)
        }
    }
}