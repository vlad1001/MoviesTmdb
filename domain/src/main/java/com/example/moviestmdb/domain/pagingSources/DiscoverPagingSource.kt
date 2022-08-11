package com.example.moviestmdb.domain.pagingSources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviestmdb.Movie
import com.example.moviestmdb.core.data.movies.MoviesRepository
import com.example.moviestmdb.core.result.Result
import com.example.moviestmdb.core.util.AppCoroutineDispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class DiscoverPagingSource @Inject constructor(
    val movieRepository: MoviesRepository,
    val dispatchers: AppCoroutineDispatchers,
    val paramMap : Map<String, String>
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val pageNumber = params.key ?: 1

            val movieList: MutableList<Movie> = mutableListOf()
            withContext(dispatchers.io){
                val result = movieRepository.getFilteredMovieList(pageNumber, paramMap).first()

                when (result) {
                    is Result.Error -> Timber.i(result.exception)
                    is Result.Success -> {
                        movieList.addAll(result.data.movieList)
                    }
                }
            }

            val prevKey = if (pageNumber >= 1) pageNumber - 1 else null
            val nextKey = if (movieList.isNotEmpty()) pageNumber + 1 else null

            LoadResult.Page(
                data = movieList,
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

    data class Params(val requestMap: Map<String, String>)
}