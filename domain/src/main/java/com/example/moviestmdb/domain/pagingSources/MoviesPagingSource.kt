package com.example.moviestmdb.domain.pagingSources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviestmdb.Movie
import com.example.moviestmdb.core.data.movies.MoviesStore
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class MoviesPagingSource(
    private val moviesStore: MoviesStore
) : PagingSource<Int, Movie>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val pageNumber = params.key ?: 1

            val movies = moviesStore.getMoviesForPage(pageNumber) ?: emptyList()

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

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

}

//class MoviesPagingSource @Inject constructor(
//    val moviesStore: MoviesStore
//) : PagingSource<Int, Movie>() {
//
//    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
//        return state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//        }
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
//        val page = params.key ?: 1
//
//        return try {
//            val movies = moviesStore.getMoviesForPage(page) ?: emptyList()
//            val prevKey = if (page > 1) page - 1 else null
//            val nextKey = if (movies.isNotEmpty()) page + 1 else null
//
//            LoadResult.Page(
//                data = movies,
//                prevKey = prevKey,
//                nextKey = nextKey
//            )
//        } catch (exception: IOException) {
//            return LoadResult.Error(exception)
//        }
//    }
//}