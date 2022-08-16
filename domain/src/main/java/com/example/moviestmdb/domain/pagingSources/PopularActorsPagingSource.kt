package com.example.moviestmdb.domain.pagingSources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviestmdb.PopularActor
import com.example.moviestmdb.core.data.people.PeopleStore
import timber.log.Timber
import java.io.IOException

class PopularActorsPagingSource(
    private val popularActorStore: PeopleStore
) : PagingSource<Int, PopularActor>(){

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PopularActor> {
        return try {
            val pageNumber = params.key ?: 1

            val movies = popularActorStore.getActorsForPage(pageNumber) ?: emptyList()

            val prevKey = if (pageNumber > 1) pageNumber - 1 else null
            val nextKey = if (movies.isNotEmpty()) pageNumber + 1 else null

            LoadResult.Page(
                data = movies,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            Timber.e(e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PopularActor>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}